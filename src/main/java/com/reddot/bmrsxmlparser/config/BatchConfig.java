package com.reddot.bmrsxmlparser.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.reddot.bmrsxmlparser.domain.dto.BillInfoDTO;
import com.reddot.bmrsxmlparser.domain.entity.*;
import com.reddot.bmrsxmlparser.mapper.BillInfoToEntityMapper;
import com.reddot.bmrsxmlparser.repository.*;
import com.reddot.bmrsxmlparser.service.*;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
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

import javax.sql.DataSource;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;

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
    private final SubscriberInfoService subscriberInfoService; // Assuming you have this service
    private final DetailChargeContainerService detailChargeContainerService; // Assuming you have this service
    private final ChargeLineService chargeLineService; // Assuming you have this service

    // Autowire AuditLogRepository directly in BatchConfig for use in processor
    private final AuditLogRepository auditLogRepository;

    // Inject the mapper
    private final BillInfoToEntityMapper billInfoToEntityMapper;

    // Inject other new repositories for direct saving where cascading might not be ideal or explicitly needed
    private final BillCycleRepository billCycleRepository; // Already used in mapper's findOrCreate
    private final NameRepository nameRepository; // Already used in mapper's findOrCreate
    private final CustomInfoRepository customInfoRepository;
    private final PrevBillRepository prevBillRepository;
    private final CurBillRepository curBillRepository;
    private final CustChargeRepository custChargeRepository;
    private final SumFeeRepository sumFeeRepository;
    private final SubsSumFeeRepository subsSumFeeRepository;
    private final RatingTaxRepository ratingTaxRepository;
    // Add repositories for AddressType, BankAccountInfo, AccountSummaryFee if they are not cascaded
    // and need explicit saving, or create services for them.

    @Value("${xml-file-location}")
    public String xmlFIlePath;

    @Bean
    public Job xmlProcessingJob() throws IOException {
        return new JobBuilder("xmlProcessingJob", jobRepository)
                .start(processDataStep()) // Directly start with the data persistence step
                .build();
    }

    // Removed the extractAndSaveToAuditTableStep bean as it's no longer part of the job flow.

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

    // Removed auditLogProcessor and auditLogWriter beans.

//    ---
//            ## Data Persistence Step
//    ---
    @Bean
    public Step processDataStep() throws IOException {
        return new StepBuilder("processDataStep", jobRepository)
                .<BillInfoDTO, BillInfo>chunk(1000, transactionManager) // Input: BillInfoDTO, Output: BillInfo Entity
                .reader(multiFileItemReader()) // Read BillInfoDTOs from XML files
                .processor(billInfoEntityProcessor()) // Process: Convert DTOs to Entities
                .writer(billInfoEntityWriter())       // Write: Persist Entities to DB and Audit Log
                .build();
    }

    @Bean
    public ItemProcessor<BillInfoDTO, BillInfo> billInfoEntityProcessor() {
        return item -> billInfoToEntityMapper.toBillInfoEntity(item); // Use the mapper to convert
    }

    @Bean
    public ItemWriter<BillInfo> billInfoEntityWriter() {
        return items -> {
            System.out.println("Persisting " + items.size() + " BillInfo entity graphs to database.");
            for (BillInfo billInfo : items) {
                try {
                    // Ensure correct saving order for complex entity graphs.
                    // THIS SECTION ASSUMES CASCADING IS NOT FULLY CONFIGURED,
                    // OR YOU NEED EXPLICIT CONTROL OVER SAVE ORDER.
                    // IF CascadeType.ALL IS USED EXTENSIVELY AND CORRECTLY,
                    // MANY OF THESE EXPLICIT SAVES CAN BE REMOVED.

                    // 1. Save BillCycle if it's new (mapper's findOrCreate handles this and saves it)
                    // No need for explicit save here if mapper correctly uses repository.save() for new entities.

                    // 2. Process BillRun and its direct dependencies
                    if (billInfo.getBillRun() != null) {
                        BillRun billRun = billInfo.getBillRun();

                        // Ensure relationship to BillCycle is set for BillRun
                        if(billRun.getBillCycleInfo() == null && billInfo.getBillCycleInfo() != null) {
                            billRun.setBillCycleInfo(billInfo.getBillCycleInfo());
                        }

                        // Save CustomInfo and its Name (assuming CustomInfo is saved independently)
                        if (billRun.getCustomInfo() != null) {
                            CustomInfo customInfo = billRun.getCustomInfo();
                            if (customInfo.getCustName() != null && customInfo.getCustName().getId() == null) {
                                nameRepository.save(customInfo.getCustName()); // Save Name if new
                            }
                            customInfoRepository.save(customInfo); // Save CustomInfo
                        }

                        // Save AccountInfo and its direct dependencies
                        if (billRun.getAccountInfo() != null) {
                            AccountInfo accountInfo = billRun.getAccountInfo();

                            // Save Name for AccountInfo
                            if (accountInfo.getAcctName() != null && accountInfo.getAcctName().getId() == null) {
                                nameRepository.save(accountInfo.getAcctName());
                            }

                            // Save one-to-one related entities of AccountInfo (if not cascaded from AccountInfo)
                            if (accountInfo.getAddressType() != null && accountInfo.getAddressType().getId() == null) { /* addressTypeRepository.save(accountInfo.getAddressType()); */ }
                            if (accountInfo.getBankAccountInfo() != null && accountInfo.getBankAccountInfo().getId() == null) { /* bankAccountInfoRepository.save(accountInfo.getBankAccountInfo()); */ }
                            if (accountInfo.getAcctSumFee() != null && accountInfo.getAcctSumFee().getId() == null) { /* accountSummaryFeeRepository.save(accountInfo.getAcctSumFee()); */ }

                            if (accountInfo.getPrevBill() != null && accountInfo.getPrevBill().getId() == null) { prevBillRepository.save(accountInfo.getPrevBill()); }
                            if (accountInfo.getCurBill() != null && accountInfo.getCurBill().getId() == null) { curBillRepository.save(accountInfo.getCurBill()); }
                            if (accountInfo.getCustCharge() != null && accountInfo.getCustCharge().getId() == null) { custChargeRepository.save(accountInfo.getCustCharge()); }


                            // Save SubscriberInfo and its dependencies (nested lists)
                            if (accountInfo.getSubsInfo() != null) {
                                SubscriberInfo subscriberInfo = accountInfo.getSubsInfo();
                                if (subscriberInfo.getSubName() != null && subscriberInfo.getSubName().getId() == null) {
                                    nameRepository.save(subscriberInfo.getSubName());
                                }
                                // Save SubsSumFees (linked to SubscriberInfo)
                                if (subscriberInfo.getSubsSumFees() != null) {
                                    for (com.reddot.bmrsxmlparser.domain.entity.SubsSumFee subsSumFee : subscriberInfo.getSubsSumFees()) {
                                        subsSumFee.setSubscriberInfo(subscriberInfo); // Set parent before saving
                                        subsSumFeeRepository.save(subsSumFee); // Assuming SubsSumFee is handled by SumFeeRepository
                                    }
                                }
                                // Save DetailChargeContainer and its nested FeeCategories & CdrInfos
                                if (subscriberInfo.getDetailChargeContainer() != null) {
                                    DetailChargeContainer detailChargeContainer = subscriberInfo.getDetailChargeContainer();
                                    if (detailChargeContainer.getFeeCategories() != null) {
                                        for (FeeCategory feeCategory : detailChargeContainer.getFeeCategories()) {
                                            feeCategory.setDetailChargeContainer(detailChargeContainer); // Set parent
                                            if (feeCategory.getDetailCharges() != null) {
                                                for (ChargeLine chargeLine : feeCategory.getDetailCharges()) {
                                                    chargeLine.setFeeCategory(feeCategory); // Set parent
                                                    chargeLineService.save(chargeLine); // Save ChargeLine
                                                }
                                            }
                                            feeCategoryService.save(feeCategory); // Save FeeCategory
                                        }
                                    }
                                    if (detailChargeContainer.getCdrInfos() != null) {
                                        for (CdrInfo cdrInfo : detailChargeContainer.getCdrInfos()) {
                                            cdrInfo.setDetailChargeContainer(detailChargeContainer); // Set parent
                                            cdrInfoService.save(cdrInfo); // Save CdrInfo
                                        }
                                    }
                                    detailChargeContainerService.save(detailChargeContainer); // Save DetailChargeContainer
                                }
                                subscriberInfoService.save(subscriberInfo); // Save SubscriberInfo
                            }

                            // Save Lists directly associated with AccountInfo (if not cascaded from AccountInfo)
                            if (accountInfo.getSumFees() != null) {
                                for (SumFee sumFee : accountInfo.getSumFees()) {
                                    sumFee.setAccountInfo(accountInfo); // Set parent
                                    sumFeeRepository.save(sumFee);
                                }
                            }
                            if (accountInfo.getRatingTaxs() != null) {
                                for (RatingTax ratingTax : accountInfo.getRatingTaxs()) {
                                    ratingTax.setAccountInfo(accountInfo); // Set parent
                                    ratingTaxRepository.save(ratingTax);
                                }
                            }

                            // Save AccountInfo's FEE_CATEGORY and CDR_INFO lists (if not cascaded from AccountInfo)
                            if (accountInfo.getFeeCategories() != null) {
                                for (FeeCategory feeCategory : accountInfo.getFeeCategories()) {
                                    feeCategory.setAccountInfo(accountInfo); // Set parent
                                    if (feeCategory.getDetailCharges() != null) {
                                        for (ChargeLine chargeLine : feeCategory.getDetailCharges()) {
                                            chargeLine.setFeeCategory(feeCategory);
                                            chargeLineService.save(chargeLine);
                                        }
                                    }
                                    feeCategoryService.save(feeCategory);
                                }
                            }
                            if (accountInfo.getCdrInfos() != null) {
                                for (CdrInfo cdrInfo : accountInfo.getCdrInfos()) {
                                    cdrInfo.setAccountInfo(accountInfo); // Set parent
                                    cdrInfoService.save(cdrInfo);
                                }
                            }

                            accountInfoService.save(accountInfo); // Save AccountInfo itself AFTER all its children are processed
                        }

                        // Save MktMsg if present (if not cascaded from BillRun)
                        // mktMsgRepository.save(billRun.getMktMsg()); // Uncomment if MktMsg is separate and needs saving

                        // Finally, save BillRun after all its dependencies are handled
                        billRunService.save(billRun);
                    }

                    // 3. Save the main BillInfo (after its direct dependencies are saved)
                    billInfoService.save(billInfo);

                    // --- AUDIT LOGGING: NOW AFTER SUCCESSFUL PERSISTENCE ---
                    AuditLog auditLog = new AuditLog();
                    auditLog.setXmlFileName(billInfo.getBillRun().getInvoiceNo()); // Assuming invoiceNo is unique and represents the file
                    auditLog.setStatus("SUCCESS");
//                    auditLog.setStartTime(LocalDateTime.now());
//                    auditLog.setEndTime(LocalDateTime.now());
                    auditLogRepository.save(auditLog);

                } catch (Exception e) {
                    // Log the error for this specific item/file
                    System.err.println("Failed to persist data for BillInfo with invoice ID: " +
                            (billInfo.getBillRun() != null ? billInfo.getBillRun().getInvoiceNo() : "N/A") +
                            ". Error: " + e.getMessage());

                    // Optionally, update audit log with FAILURE status for this file
                    AuditLog auditLog = new AuditLog();
                    auditLog.setXmlFileName(billInfo.getBillRun() != null ? billInfo.getBillRun().getInvoiceNo() : "ERROR_FILE");
                    auditLog.setStatus("FAILED");
//                    auditLog.setStartTime(LocalDateTime.now());
//                    auditLog.setEndTime(LocalDateTime.now());
                    auditLog.setErrorMessage(e.getMessage()); // Store error message
                    auditLogRepository.save(auditLog);

                    // Re-throw to indicate a chunk failure, Spring Batch will handle retries/skips if configured
                    throw e;
                }
            }
        };
    }
}
