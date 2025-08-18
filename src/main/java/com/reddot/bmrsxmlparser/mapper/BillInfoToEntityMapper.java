package com.reddot.bmrsxmlparser.mapper;

import com.reddot.bmrsxmlparser.domain.dto.BillInfoDTO;
import com.reddot.bmrsxmlparser.domain.entity.*;
import com.reddot.bmrsxmlparser.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BillInfoToEntityMapper {

    // Define date/time formatters based on your XML data
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Repositories for looking up existing entities
    private final BillCycleRepository billCycleRepository;
    private final NameRepository nameRepository;
    private final BillRunRepository billRunRepository;
    private final AccountInfoRepository accountInfoRepository; // Will be used if AccountInfo is persisted independently
    private final DetailChargeContainerRepository detailChargeContainerRepository;
    // NEW: Repositories for newly identified entities (assuming they might need lookups or independent saves)
    private final CustomInfoRepository customInfoRepository;
    private final PrevBillRepository prevBillRepository;
    private final CurBillRepository curBillRepository;
    private final CustChargeRepository custChargeRepository;
    private final SumFeeRepository sumFeeRepository;
    private final RatingTaxRepository ratingTaxRepository;


    // --- Main mapping method ---
    public BillInfo toBillInfoEntity(BillInfoDTO dto) {
        if (dto == null) {
            return null;
        }

        BillInfo billInfo = new BillInfo();
        billInfo.setXmlInfo(dto.getXmlInfo());
        billInfo.setOperInfo(dto.getOperInfo());

        // Convert and link BillCycleInfo (from root)
        if (dto.getBillCycleInfo() != null) {
            BillCycle billCycle = findOrCreateBillCycle(dto.getBillCycleInfo());
            billInfo.setBillCycleInfo(billCycle);
        }

        // Convert and link BillRun
        if (dto.getBillRun() != null) {
            BillRun billRun = toBillRunEntity(dto.getBillRun(), billInfo.getBillCycleInfo());
            billInfo.setBillRun(billRun);
        }

        return billInfo;
    }

    // --- Helper mapping methods ---

    private BillCycle findOrCreateBillCycle(BillInfoDTO.BillCycleInfoDTO dto) {
        if (dto == null || dto.getBillCycleId() == null || dto.getBillCycleId().isEmpty()) {
            return null;
        }
        Optional<BillCycle> existingBillCycle = billCycleRepository.findByBillCycleId(dto.getBillCycleId());
        if (existingBillCycle.isPresent()) {
            return existingBillCycle.get();
        } else {
            BillCycle billCycle = new BillCycle();
            billCycle.setBillCycleId(dto.getBillCycleId());
            billCycle.setCycleBegin(parseDateTime(dto.getBillCycleBegin()));
            billCycle.setCycleEnd(parseDateTime(dto.getBillCycleEnd()));
            billCycle.setDueDate(parseDateTime(dto.getDueDate()));
            return billCycleRepository.save(billCycle);
        }
    }

    private BillRun toBillRunEntity(BillInfoDTO.BillRunDTO dto, BillCycle billCycle) {
        if (dto == null) {
            return null;
        }

        BillRun billRun = new BillRun();

        // Map BillPropDTO to BillRun fields
        if (dto.getBillProp() != null) {
            billRun.setInvoiceId(dto.getBillProp().getInvoiceId());
            billRun.setInvoiceDate(parseDateTime(dto.getBillProp().getInvoiceDate()));
            billRun.setInvoiceNo(dto.getBillProp().getInvoiceNo());
            billRun.setBarCodeNumber(dto.getBillProp().getBarCodeNumber());
            // Map other BillProp fields here if they go directly into BillRun entity
            billRun.setStatementDate(parseDateTime(dto.getBillProp().getStatementDate()));
            billRun.setBillPeriod(dto.getBillProp().getBillPeriod());
            billRun.setFromDate(parseDateTime(dto.getBillProp().getFromDate()));
            billRun.setToDate(parseDateTime(dto.getBillProp().getToDate()));
            billRun.setDueDate(parseDateTime(dto.getBillProp().getDueDate())); // BillProp also has dueDate
            billRun.setLang(dto.getBillProp().getLang());
            billRun.setCurrency(dto.getBillProp().getCurrency());
            billRun.setBillMedium(dto.getBillProp().getBillMedium());
            billRun.setFeePrecision(dto.getBillProp().getFeePrecision());
            billRun.setBillInsert(dto.getBillProp().getBillInsert());
            billRun.setBillType(dto.getBillProp().getBillType());
            billRun.setMobileNumber(dto.getBillProp().getMobileNumber());
            billRun.setEndDate(parseDateTime(dto.getBillProp().getEndDate()));
            billRun.setMainOfferingName(dto.getBillProp().getMainOfferingName());
        }

        // Link BillCycle if provided (from root BillInfoDTO)
        billRun.setBillCycleInfo(billCycle);

        // Convert and link AccountInfo
        if (dto.getAccountInfo() != null) {
            AccountInfo accountInfo = toAccountInfoEntity(dto.getAccountInfo());
            billRun.setAccountInfo(accountInfo); // Assuming BillRun has ManyToOne relationship to AccountInfo
        }

        // Convert and link CustomInfo
        if (dto.getCustomInfo() != null) {
            CustomInfo customInfo = toCustomInfoEntity(dto.getCustomInfo());
            billRun.setCustomInfo(customInfo); // Assuming BillRun has ManyToOne relationship to CustomInfo
        }

        return billRun;
    }

    private CustomInfo toCustomInfoEntity(BillInfoDTO.CustomInfoDTO dto) {
        if (dto == null) return null;
        CustomInfo entity = new CustomInfo();
        entity.setCustGender(dto.getCustGender());
        entity.setCustType(dto.getCustType());
        entity.setCustLevel(dto.getCustLevel());
        entity.setCustId(dto.getCustId());
        entity.setVatNumber(dto.getVatNumber());
        entity.setCustCode(dto.getCustCode());
        if (dto.getCustName() != null) {
            entity.setCustName(findOrCreateName(dto.getCustName()));
        }
        return customInfoRepository.save(entity); // Assuming CustomInfo is saved independently
    }

    private AccountInfo toAccountInfoEntity(BillInfoDTO.AccountInfoDTO dto) {
        if (dto == null) {
            return null;
        }
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setAcctId(dto.getAcctId()); // Ensure AccountInfo entity has acctId
        accountInfo.setAcctCode(dto.getAcctCode());
        accountInfo.setAcctNumber(dto.getAcctNumber());
        accountInfo.setDeposit(dto.getDeposit());
        accountInfo.setPreviousBalance(dto.getPreviousBalance());
        accountInfo.setPayments(dto.getPayments());
        accountInfo.setAdjustments(dto.getAdjustments());
        accountInfo.setCurrentCharges(dto.getCurrentCharges());
        accountInfo.setAmountDue(dto.getAmountDue());
        accountInfo.setCreditLimit(dto.getCreditLimit());
        accountInfo.setPayMethod(dto.getPayMethod());
        accountInfo.setPrintVatNo(dto.getPrintVatNo());

        if (dto.getAcctName() != null) {
            Name acctName = findOrCreateName(dto.getAcctName());
            accountInfo.setAcctName(acctName);
        }

        // Convert AddressType
        if (dto.getAddressType() != null) {
            AddressType addressType = toAddressTypeEntity(dto.getAddressType());
            accountInfo.setAddressType(addressType);
        }
        // Convert BankAccountInfo
        if (dto.getBankAccountInfo() != null) {
            BankAccountInfo bankAccountInfo = toBankAccountInfoEntity(dto.getBankAccountInfo());
            accountInfo.setBankAccountInfo(bankAccountInfo);
        }
        // Convert AccountSummaryFee
        if (dto.getAcctSumFee() != null) {
            AccountSummaryFee accountSummaryFee = toAccountSummaryFeeEntity(dto.getAcctSumFee());
            accountInfo.setAcctSumFee(accountSummaryFee);
        }

        // Process SubscriberInfo
        if (dto.getSubsInfo() != null) {
            SubscriberInfo subscriberInfo = toSubscriberInfoEntity(dto.getSubsInfo());
            accountInfo.setSubsInfo(subscriberInfo);
        }

        // NEW: Mapping for previously unmapped fields
        if (dto.getPrevBill() != null) { // Assuming PrevBillDTO exists
            PrevBill prevBill = toPrevBillEntity(dto.getPrevBill());
            accountInfo.setPrevBill(prevBill); // Assuming AccountInfo has a PrevBill field
        }
        if (dto.getCurBill() != null) { // Assuming CurBillDTO exists
            CurBill curBill = toCurBillEntity(dto.getCurBill());
            accountInfo.setCurBill(curBill); // Assuming AccountInfo has a CurBill field
        }
        if (dto.getCustCharge() != null) { // Assuming CustChargeDTO exists
            CustCharge custCharge = toCustChargeEntity(dto.getCustCharge());
            accountInfo.setCustCharge(custCharge); // Assuming AccountInfo has a CustCharge field
        }
        if (dto.getSumFees() != null) { // Assuming List<SumFeeDTO> exists
            List<SumFee> sumFees = dto.getSumFees().stream()
                    .map(this::toSumFeeEntity)
                    .collect(Collectors.toList());
            accountInfo.setSumFees(sumFees); // Assuming AccountInfo has a List<SumFee> field
        }
        if (dto.getRatingTaxs() != null) { // Assuming List<RatingTaxDTO> exists
            List<RatingTax> ratingTaxs = dto.getRatingTaxs().stream()
                    .map(this::toRatingTaxEntity)
                    .collect(Collectors.toList());
            accountInfo.setRatingTaxs(ratingTaxs); // Assuming AccountInfo has a List<RatingTax> field
        }

        return accountInfo;
    }

    private Name findOrCreateName(BillInfoDTO.NameDTO dto) {
        if (dto == null || dto.getFullName() == null || dto.getFullName().isEmpty()) {
            return null;
        }
        Optional<Name> existingName = nameRepository.findByFullName(dto.getFullName());
        if (existingName.isPresent()) {
            return existingName.get();
        } else {
            Name name = new Name();
            name.setTitle(dto.getTitle());
            name.setFirstName(dto.getFirstName());
            name.setMiddleName(dto.getMiddleName());
            name.setLastName(dto.getLastName());
            name.setFullName(dto.getFullName());
            return nameRepository.save(name);
        }
    }

    private AddressType toAddressTypeEntity(BillInfoDTO.AddressTypeDTO dto) {
        if (dto == null) return null;
        AddressType entity = new AddressType();
        entity.setAddrType(dto.getAddrType());
        entity.setAddr1(dto.getAddr1());
        entity.setAddr2(dto.getAddr2());
        entity.setAddr3(dto.getAddr3());
        entity.setAddr4(dto.getAddr4());
        entity.setAddr5(dto.getAddr5());
        entity.setAddr6(dto.getAddr6());
        entity.setZipcode(dto.getZipcode());
        entity.setEmailAddr(dto.getEmailAddr());
        entity.setSmsAddr(dto.getSmsAddr());
        return entity;
    }

    private BankAccountInfo toBankAccountInfoEntity(BillInfoDTO.BankAccountInfoDTO dto) {
        if (dto == null) return null;
        BankAccountInfo entity = new BankAccountInfo();
        entity.setBankNo(dto.getBankNo());
        entity.setBankName(dto.getBankName());
        entity.setCardExpDate(dto.getCardExpDate());
        entity.setBankAcctName(dto.getBankAcctName());
        entity.setBankAcctType(dto.getBankAcctType());
        return entity;
    }

    private AccountSummaryFee toAccountSummaryFeeEntity(BillInfoDTO.AccountSummaryFeeDTO dto) {
        if (dto == null) return null;
        AccountSummaryFee entity = new AccountSummaryFee();
        entity.setVoiceFlag(dto.getVoiceFlag());
        entity.setDesc(dto.getDesc());
        entity.setNumber(dto.getNumber());
        entity.setDuration(dto.getDuration());
        entity.setChgFee(dto.getChgFee()); // Ensure type matches in entity
        entity.setCurrency(dto.getCurrency());
        return entity;
    }

    private SubscriberInfo toSubscriberInfoEntity(BillInfoDTO.SubscriberInfoDTO dto) {
        if (dto == null) return null;
        SubscriberInfo entity = new SubscriberInfo();
        entity.setMsisdn(dto.getMsisdn());
        entity.setSubId(dto.getSubId());
        entity.setAcctCode(dto.getAcctCode());

        if (dto.getSubName() != null) {
            entity.setSubName(findOrCreateName(dto.getSubName()));
        }

        if (dto.getSubsSumFees() != null) {
            entity.setSubsSumFees(dto.getSubsSumFees().stream()
                    .map(this::toSubsSumFeeEntity)
                    .collect(Collectors.toList()));
        }

        if (dto.getDetailChargeContainer() != null) {
            DetailChargeContainer container = toDetailChargeContainerEntity(dto.getDetailChargeContainer());
            entity.setDetailChargeContainer(container);
        }

        return entity;
    }

    private SubsSumFee toSubsSumFeeEntity(BillInfoDTO.SubsSumFeeDTO dto) {
        if (dto == null) return null;
        SubsSumFee entity = new SubsSumFee();
        entity.setVoiceFlag(dto.getVoiceFlag());
        entity.setRoamFlag(dto.getRoamFlag());
        entity.setDesc(dto.getDesc());
        entity.setNumber(dto.getNumber());
        entity.setDuration(dto.getDuration());
        entity.setChgFee(dto.getChgFee());
        entity.setCurrency(dto.getCurrency());
        entity.setPayFlag(dto.getPayFlag());
        entity.setBillItemCode(dto.getBillItemCode());
        return entity;
    }

    private DetailChargeContainer toDetailChargeContainerEntity(BillInfoDTO.DetailChargeContainerDTO dto) {
        if (dto == null) return null;
        DetailChargeContainer entity = new DetailChargeContainer();
        entity.setFreeUnit(dto.getFreeUnit());

        // Process nested FeeCategory and CdrInfo lists within DetailChargeContainer
        if (dto.getFeeCategories() != null) {
            entity.setFeeCategories(dto.getFeeCategories().stream()
                    .map(this::toFeeCategoryEntity) // Convert FeeCategory DTOs
                    .collect(Collectors.toList()));
        }

        if (dto.getCdrInfoDTOS() != null) {
            entity.setCdrInfos(dto.getCdrInfoDTOS().stream()
                    .map(this::toCdrInfoEntity) // Convert CdrInfo DTOs
                    .collect(Collectors.toList()));
        }
        return entity;
    }

    private FeeCategory toFeeCategoryEntity(BillInfoDTO.FeeCategoryDTO dto) {
        if (dto == null) return null;
        FeeCategory entity = new FeeCategory();
        entity.setPayAcctCode(dto.getPayAcctCode());
        // `freeUnit` might be in FeeCategory DTO but in your FeeCategory entity, it's also there.
        // Ensure consistency. If it's only in DetailChargeContainer, remove from FeeCategory entity.
        // For now, I'll assume it exists in FeeCategory as well from your entity definition.

        if (dto.getDetailCharges() != null) {
            entity.setDetailCharges(dto.getDetailCharges().stream()
                    .map(this::toChargeLineEntity)
                    .collect(Collectors.toList()));
        }
        return entity;
    }

    private ChargeLine toChargeLineEntity(BillInfoDTO.ChargeLineDTO dto) {
        if (dto == null) return null;
        ChargeLine entity = new ChargeLine();
        entity.setChgFee(dto.getChgFee());
        entity.setDesc(dto.getDesc());
        entity.setCategory(dto.getCategory());
        entity.setSubCategory(dto.getSubcategory());
        entity.setCycleNo(dto.getCycleNo());
        entity.setServiceType(dto.getServiceType());
        entity.setChargeCode(dto.getChargeCode());
        entity.setItemId(dto.getItemId());
        entity.setRatingTax1(dto.getRatingTax1());
        entity.setRatingTax2(dto.getRatingTax2());
        entity.setRatingTax3(dto.getRatingTax3());
        entity.setRatingTax4(dto.getRatingTax4());
        entity.setDisc1(dto.getDisc1());
        entity.setDisc2(dto.getDisc2());
        entity.setDisc3(dto.getDisc3());
        entity.setStartDate(parseDateTime(dto.getStartDate())); // Assuming this is also a datetime
        entity.setEndDate(parseDateTime(dto.getEndDate()));     // Assuming this is also a datetime
        entity.setCurrency(dto.getCurrency());
        return entity;
    }

    private CdrInfo toCdrInfoEntity(BillInfoDTO.CdrInfoDTO dto) {
        if (dto == null) return null;
        CdrInfo entity = new CdrInfo();
        entity.setCallFlow(dto.getCallFlow());
        entity.setOtherNum(dto.getOtherNum());
        entity.setCallMask(dto.getCallMask());
        entity.setDateTime(parseDateTime(dto.getDateTime()));
        entity.setChgAmt(dto.getChgAmt());
        entity.setTaxAmt1(dto.getTaxAmt1());
        entity.setTaxAmt2(dto.getTaxAmt2());
        entity.setTaxAmt3(dto.getTaxAmt3());
        entity.setVolume(dto.getVolume());
        entity.setServiceType(dto.getServiceType());
        entity.setStartTime(parseDateTime(dto.getStartTime()));
        entity.setEndTime(parseDateTime(dto.getEndTime()));
        entity.setDuration(dto.getDuration());
        entity.setResultReason(dto.getResultReason());
        entity.setServiceFlow(dto.getServiceFlow());
        entity.setNetwork(dto.getNetwork());
        entity.setCountry(dto.getCountry());
        entity.setOperator(dto.getOperator());
        entity.setGprsFlag(dto.getGprsFlag());
        entity.setCfNum(dto.getCfNum());
        return entity;
    }

    // NEW: Mapper methods for new entities
    private PrevBill toPrevBillEntity(BillInfoDTO.PrevBillDTO dto) {
        if (dto == null) return null;
        PrevBill entity = new PrevBill();
        entity.setPrevBalance(dto.getPrevBalance());
        entity.setBalanceFwd(dto.getBalanceFwd());
        return prevBillRepository.save(entity); // Assuming independent save or cascade from AccountInfo
    }

    private CurBill toCurBillEntity(BillInfoDTO.CurBillDTO dto) {
        if (dto == null) return null;
        CurBill entity = new CurBill();
        entity.setCurrentCharges(dto.getCurrentCharges()); // Map all fields as per your CurBillDTO and Entity
        // Add other fields from CurBillDTO to CurBill entity
        return curBillRepository.save(entity); // Assuming independent save or cascade
    }

    private CustCharge toCustChargeEntity(BillInfoDTO.CustChargeDTO dto) {
        if (dto == null) return null;
        CustCharge entity = new CustCharge();
        entity.setCategory(dto.getCategory()); // Map all fields as per your CustChargeDTO and Entity
        // Add other fields from CustChargeDTO to CustCharge entity
        return custChargeRepository.save(entity); // Assuming independent save or cascade
    }

    private SumFee toSumFeeEntity(BillInfoDTO.SumFeeDTO dto) {
        if (dto == null) return null;
        SumFee entity = new SumFee();
        entity.setDesc(dto.getDesc()); // Map all fields as per your SumFeeDTO and Entity
        // Add other fields from SumFeeDTO to SumFee entity
        return sumFeeRepository.save(entity); // Assuming independent save or cascade
    }

    private RatingTax toRatingTaxEntity(BillInfoDTO.RatingTaxDTO dto) {
        if (dto == null) return null;
        RatingTax entity = new RatingTax();
        entity.setTaxAmount(dto.getTaxAmount()); // Map all fields as per your RatingTaxDTO and Entity
        // Add other fields from RatingTaxDTO to RatingTax entity
        return ratingTaxRepository.save(entity); // Assuming independent save or cascade
    }

    // Helper method for parsing date/time strings
    private LocalDateTime parseDateTime(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        try {
            // Try parsing with the full date-time format first
            return LocalDateTime.parse(dateString.trim(), DATE_TIME_FORMATTER);
        } catch (java.time.format.DateTimeParseException e) {
            // If that fails, try parsing with just the date format (assuming 00:00:00 time)
            try {
                return LocalDateTime.parse(dateString.trim() + " 00:00:00", DATE_TIME_FORMATTER);
            } catch (java.time.format.DateTimeParseException ex) {
                System.err.println("Could not parse date string: " + dateString + " - " + ex.getMessage());
                return null; // Or throw a custom exception, or handle as needed
            }
        }
    }
}
