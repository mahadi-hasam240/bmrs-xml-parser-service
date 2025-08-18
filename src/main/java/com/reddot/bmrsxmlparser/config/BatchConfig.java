package com.reddot.bmrsxmlparser.config;

import com.reddot.bmrsxmlparser.domain.entity.AuditLog;
import com.reddot.bmrsxmlparser.repository.AuditLogRepository;
import com.reddot.bmrsxmlparser.service.*;
import com.reddot.bmrsxmlparser.domain.dto.BillInfoDTO;

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
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.oxm.Unmarshaller; // Import Spring OXM Unmarshaller
import org.springframework.oxm.XmlMappingException;
import org.springframework.transaction.PlatformTransactionManager;

import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stax.StAXSource;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

import javax.sql.DataSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;

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

    // Service classes for saving data to entities (will be used in Step 2)
    private final BillInfoService billInfoService;
    private final BillCycleService billCycleService;
    private final BillRunService billRunService;
    private final AccountInfoService accountInfoService;
    private final FeeCategoryService feeCategoryService;
    private final CdrInfoService cdrInfoService;

    // Autowire AuditLogRepository directly in BatchConfig for use in processor
    private final AuditLogRepository auditLogRepository;

    @Value("${xml-file-location}")
    public String xmlFIlePath; // This should now point to the directory, e.g., /home/asif-akter/test_xml/

    @Bean
    public Job xmlProcessingJob() throws IOException {
        return new JobBuilder("xmlProcessingJob", jobRepository)
                .start(extractAndSaveToAuditTableStep())
                .next(processDataStep())
                .build();
    }

    @Bean
    public Step extractAndSaveToAuditTableStep() throws IOException {
        return new StepBuilder("extractAndSaveToAuditTableStep", jobRepository)
                .<BillInfoDTO, BillInfoDTO>chunk(10, transactionManager)
                .reader(multiFileItemReader()) // Use the new MultiResourceItemReader
                .processor(auditLogProcessor(auditLogRepository))
                .writer(auditLogWriter())
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

    // This is now the DELEGATE reader, responsible for reading a single XML fragment
    @Bean
    public StaxEventItemReader<BillInfoDTO> delegateXmlItemReader() {
        StaxEventItemReader<BillInfoDTO> reader = new StaxEventItemReader<>();
        // No resource set here; it will be set by MultiResourceItemReader
        reader.setFragmentRootElementName("BILL_INFO");
        reader.setUnmarshaller(jacksonXmlUnmarshaller(xmlMapper()));
        return reader;
    }

    // NEW: MultiResourceItemReader to read multiple XML files
    @Bean
    public MultiResourceItemReader<BillInfoDTO> multiFileItemReader() throws IOException {
        MultiResourceItemReader<BillInfoDTO> reader = new MultiResourceItemReader<>();
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

        // IMPORTANT: Ensure xmlFIlePath is the directory, and the wildcard matches your files.
        // If your files are .XML (uppercase), change "*.xml" to "*.XML" or "{*.xml,*.XML}"
        Resource[] resources = resourcePatternResolver.getResources("file:" + xmlFIlePath + "/*.XML");

        reader.setResources(resources);
        reader.setDelegate(delegateXmlItemReader()); // Set the StaxEventItemReader as the delegate
        return reader;
    }


    // Processor to extract metadata (filename, status, processing time) for audit
    @Bean
    public ItemProcessor<BillInfoDTO, BillInfoDTO> auditLogProcessor(AuditLogRepository auditLogRepository) {
        return item -> {
            String fileName = item.getBillRun().getBillProp().getInvoiceNo(); // Example: using invoice number as filename
            String status = "SUCCESS";

            AuditLog auditLog = new AuditLog();
            auditLog.setXmlFileName(fileName);
            auditLog.setStatus(status);
//            auditLog.setStartTime(LocalDateTime.now());
//            auditLog.setEndTime(LocalDateTime.now());

            auditLogRepository.save(auditLog);

            return item;
        };
    }

    // Writer to save audit data to the AuditLog table
    @Bean
    public ItemWriter<BillInfoDTO> auditLogWriter() {
        return items -> {
            System.out.println("Saving audit data: " + items.size() + " items processed in audit step.");
        };
    }
    // Step 2: Process Data - now correctly receiving BillInfoDTO
    @Bean
    public Step processDataStep() throws IOException {
        // NOTE: For now, this step still re-reads the same files.
        // In a full implementation, you might want to process different data,
        // or ensure files processed in Step 1 are moved/marked to avoid re-processing.
        return new StepBuilder("processDataStep", jobRepository)
                .<BillInfoDTO, BillInfoDTO>chunk(10, transactionManager)
                .reader(multiFileItemReader()) // Use the MultiResourceItemReader here too
                .processor(new ItemProcessor<BillInfoDTO, BillInfoDTO>() {
                    @Override
                    public BillInfoDTO process(BillInfoDTO item) throws Exception {
                        System.out.println("Processing BillInfoDTO in Step 2 for Invoice ID: " + item.getBillRun().getBillProp().getInvoiceId());
                        return item;
                    }
                })
                .writer(new ItemWriter<BillInfoDTO>() {
                    @Override
                    public void write(Chunk<? extends BillInfoDTO> chunk) throws Exception {

                    }

                    public void write(java.util.List<? extends BillInfoDTO> items) throws Exception {
                        System.out.println("Writing " + items.size() + " BillInfoDTOs from Step 2.");
                        for (BillInfoDTO item : items) {
                            // Your entity conversion and saving logic would go here.
                        }
                    }
                })
                .build();
    }
}
