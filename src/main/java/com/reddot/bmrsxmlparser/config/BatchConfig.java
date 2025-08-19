package com.reddot.bmrsxmlparser.config;

import com.reddot.bmrsxmlparser.domain.entity.AuditLog;
import com.reddot.bmrsxmlparser.repository.AuditLogRepository;
import com.reddot.bmrsxmlparser.service.*;
import com.reddot.bmrsxmlparser.domain.dto.BillInfoDTO;
import com.reddot.bmrsxmlparser.domain.entity.BillInfo;


import com.reddot.bmrsxmlparser.mapper.BillInfoToEntityMapper;

// REPOSITORY IMPORTS (Only those still directly used in mapper's findOrCreate, or specific cases)
import com.reddot.bmrsxmlparser.repository.BillCycleRepository;
import com.reddot.bmrsxmlparser.repository.NameRepository;


import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.transaction.PlatformTransactionManager;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stax.StAXSource;

import javax.sql.DataSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;

// Jackson imports for XML mapping
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;

    // Only the top-level service for BillInfo is needed here for persistence
    private final BillInfoService billInfoService;
    // AuditLogRepository is used directly for logging success/failure
    private final AuditLogRepository auditLogRepository;

    // The mapper is responsible for building the entity graph
    private final BillInfoToEntityMapper billInfoToEntityMapper;

    // Repositories used by the mapper's findOrCreate logic (not for direct saving in writer)
    private final BillCycleRepository billCycleRepository;
    private final NameRepository nameRepository;

    @Value("${xml-file-location}")
    public String xmlFIlePath;

    @Bean
    public Job xmlProcessingJob() throws IOException {
        return new JobBuilder("xmlProcessingJob", jobRepository)
                .start(processDataStep())
                .build();
    }

    @Bean
    public XmlMapper xmlMapper() {
        return XmlMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .build();
    }

    @Bean
    public Unmarshaller jacksonXmlUnmarshaller(XmlMapper xmlMapper) {
        return new Unmarshaller() {
            @Override
            public boolean supports(Class<?> aClass) {
                return BillInfoDTO.class.isAssignableFrom(aClass);
            }

            @Override
            public Object unmarshal(Source source) throws IOException, XmlMappingException {
                if (source instanceof StAXSource) {
                    StAXSource staxSource = (StAXSource) source;
                    try {
                        Transformer transformer = TransformerFactory.newInstance().newTransformer();
                        StringWriter writer = new StringWriter();
                        transformer.transform(staxSource, new StreamResult(writer));
                        String xmlString = writer.toString();

                        return xmlMapper.readValue(xmlString, BillInfoDTO.class);
                    } catch (TransformerException e) {
                        throw new RuntimeException("Error transforming XML source during unmarshalling", e);
                    }
                }
                throw new UnsupportedOperationException("Only StAXSource is supported for unmarshalling by this custom Unmarshaller.");
            }
        };
    }

    @Bean
    public StaxEventItemReader<BillInfoDTO> delegateXmlItemReader() {
        StaxEventItemReader<BillInfoDTO> reader = new StaxEventItemReader<>();
        reader.setFragmentRootElementName("BILL_INFO");
        reader.setUnmarshaller(jacksonXmlUnmarshaller(xmlMapper()));
        return reader;
    }

    @Bean
    public MultiResourceItemReader<BillInfoDTO> multiFileItemReader() throws IOException {
        MultiResourceItemReader<BillInfoDTO> reader = new MultiResourceItemReader<>();
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

        Resource[] resources = resourcePatternResolver.getResources("file:" + xmlFIlePath + "/*.XML");

        reader.setResources(resources);
        reader.setDelegate(delegateXmlItemReader());
        return reader;
    }

    // --- Data Persistence Step ---
    @Bean
    public Step processDataStep() throws IOException {
        return new StepBuilder("processDataStep", jobRepository)
                .<BillInfoDTO, BillInfo>chunk(10, transactionManager)
                .reader(multiFileItemReader())
                .processor(billInfoEntityProcessor())
                .writer(billInfoEntityWriter())
                .build();
    }

    @Bean
    public ItemProcessor<BillInfoDTO, BillInfo> billInfoEntityProcessor() {
        return item -> billInfoToEntityMapper.toBillInfoEntity(item);
    }

    @Bean
    public ItemWriter<BillInfo> billInfoEntityWriter() {
        return items -> {
            System.out.println("Persisting " + items.size() + " BillInfo entity graphs to database.");
            for (BillInfo billInfo : items) {
                String invoiceNo = "N/A"; // Default for logging if billInfo.getBillRun() is null
                try {
                    if (billInfo.getBillRun() != null) {
                        invoiceNo = billInfo.getBillRun().getInvoiceNo();
                    }

                    // This single save call will now cascade all persistence operations
                    // to the entire entity graph (BillInfo -> BillRun -> AccountInfo -> ... and all their children)
                    // provided that CascadeType.ALL is correctly configured on all relationships.
                    billInfoService.save(billInfo);

                    // --- AUDIT LOGGING: NOW AFTER SUCCESSFUL PERSISTENCE ---
                    AuditLog auditLog = new AuditLog();
                    auditLog.setXmlFileName(invoiceNo);
                    auditLog.setStatus("SUCCESS");

                    auditLogRepository.save(auditLog);

                } catch (Exception e) {
                    System.err.println("Failed to persist data for BillInfo with invoice ID: " + invoiceNo + ". Error: " + e.getMessage());

                    AuditLog auditLog = new AuditLog();
                    auditLog.setXmlFileName(invoiceNo);
                    auditLog.setStatus("FAILED");
                    auditLog.setErrorMessage(e.getMessage());
                    auditLogRepository.save(auditLog);

                    // Re-throw to indicate a chunk failure, Spring Batch will handle retries/skips if configured
                    throw e;
                }
            }
        };
    }
}
