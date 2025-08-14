package com.reddot.bmrsxmlparser.config;

import com.reddot.bmrsxmlparser.domain.dto.BillInfoDTO;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {


    // Inject JobRepository and PlatformTransactionManager instead of JobBuilderFactory and StepBuilderFactory
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory; // Keep this if you're using JPA for item readers/writers

    @Bean
    public Job xmlProcessingJob() {
        // Use the JobBuilder directly with JobRepository
        return new JobBuilder("xmlProcessingJob", jobRepository)
                .start(parseXMLStep())
                .next(processDataStep())
                .next(writeDataStep())
                .build();
    }

    @Bean
    public Step parseXMLStep() {
        return stepBuilderFactory.get("parseXMLStep")
                .<String, BillInfoDTO>chunk(10)
                .reader(xmlFileReader())
                .processor(xmlProcessor())
                .writer(xmlWriter())
                .faultTolerant()  // Enable fault tolerance
                .retry(Exception.class)  // Retry on exceptions
                .retryLimit(3)  // Retry 3 times
                .skip(Exception.class)  // Skip on failures after retries
                .skipLimit(5)  // Skip 5 items on failure
                .build();
    }


    @Bean
    public Step processDataStep() {
        // Use the StepBuilder directly with JobRepository and PlatformTransactionManager
        return new StepBuilder("processDataStep", jobRepository)
                .<BillInfoDTO, BillInfoDTO>chunk(10, transactionManager) // Pass transactionManager here
                .reader(processedDataReader())
                .writer(processedDataWriter())
                .build();
    }

    @Bean
    public Step writeDataStep() {
        // Use the StepBuilder directly for tasklets
        return new StepBuilder("writeDataStep", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        // Logic for final data processing and handling, like marking as processed, etc.
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager) // Pass transactionManager here
                .build();
    }

    // --- Placeholder Beans for Readers, Processors, and Writers ---
    // You will replace these with your actual implementations.
    // These are just here to make the BatchConfig compile.

    @Bean
    public StaxEventItemReader<BillInfoDTO> xmlFileReader() {
        StaxEventItemReader<BillInfoDTO> reader = new StaxEventItemReader<>();
        reader.setResource(new FileSystemResource("path/to/xml/files"));
        reader.setFragmentRootElementName("BILL_INFO");

        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(BillInfoDTO.class);
        reader.setUnmarshaller(marshaller);

        return reader;
    }

    @Bean
    public ItemProcessor<String, BillInfoDTO> xmlProcessor() {
        return item -> {
            // Your XML processing logic here
            // Convert String to BillInfoDTO
            return new BillInfoDTO(); // Placeholder
        };
    }

    @Bean
    public ItemWriter<BillInfoDTO> xmlWriter() {
        return new ItemWriter<BillInfoDTO>() {
            @Autowired
            private BillInfoRepository billInfoRepository;

            @Override
            public void write(List<? extends BillInfoDTO> items) throws Exception {
                billInfoRepository.saveAll(items);  // Save processed data
            }
        };
    }


    @Bean
    public ItemReader<BillInfoDTO> processedDataReader() {
        return () -> {
            // Your processed data reading logic here
            // e.g., read BillInfoDTOs from a temporary storage or database
            return null; // Placeholder
        };
    }

    @Bean
    public ItemWriter<BillInfoDTO> processedDataWriter() {
        return items -> {
            // Your processed data writing/saving logic here
            // e.g., save BillInfoDTO objects to final database tables
        };
    }
}
