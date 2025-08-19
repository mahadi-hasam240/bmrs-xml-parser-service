package com.reddot.bmrsxmlparser.mapper;

import com.reddot.bmrsxmlparser.domain.dto.*;
import com.reddot.bmrsxmlparser.domain.entity.*;
import com.reddot.bmrsxmlparser.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BillInfoToEntityMapper {

    // Define date/time formatters based on your XML data
    private static final DateTimeFormatter DATE_TIME_FORMATTER_DDMMYYYY = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter DATE_TIME_FORMATTER_YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss"); // NEW FORMATTER
    private static final DateTimeFormatter DATE_FORMATTER_DDMMYYYY = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATE_FORMATTER_YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd"); // For cases where there's no time part, but in YYYYMMDD format

    // Repositories for looking up existing entities
    private final BillCycleRepository billCycleRepository;
    private final NameRepository nameRepository;
    private final BillRunRepository billRunRepository;
    private final AccountInfoRepository accountInfoRepository;
    private final DetailChargeContainerRepository detailChargeContainerRepository;
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
            BillRun billRun = toBillRunEntity(dto.getBillRun(), billInfo.getBillCycleInfo()); // Pass the BillCycle
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
            billRun.setStatementDate(parseDateTime(dto.getBillProp().getStatementDate()));
            billRun.setBillPeriod(dto.getBillProp().getBillPeriod());
            billRun.setFromDate(parseDateTime(dto.getBillProp().getFromDate()));
            billRun.setToDate(parseDateTime(dto.getBillProp().getToDate()));
            billRun.setDueDate(parseDateTime(dto.getBillProp().getDueDate()));
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

        billRun.setBillCycleInfo(billCycle);

        if (dto.getAccountInfo() != null) {
            AccountInfo accountInfo = toAccountInfoEntity(dto.getAccountInfo());
            billRun.setAccountInfo(accountInfo);
        }

        if (dto.getCustomInfo() != null) {
            CustomInfo customInfo = toCustomInfoEntity(dto.getCustomInfo());
            billRun.setCustomInfo(customInfo);
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
        return entity; // Don't save here if cascading from BillRun, otherwise customInfoRepository.save(entity)
    }

    private AccountInfo toAccountInfoEntity(BillInfoDTO.AccountInfoDTO dto) {
        if (dto == null) {
            return null;
        }
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setAcctId(dto.getAcctId());
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

        if (dto.getAddressType() != null) {
            AddressType addressType = toAddressTypeEntity(dto.getAddressType());
            accountInfo.setAddressType(addressType);
        }
        if (dto.getBankAccountInfo() != null) {
            BankAccountInfo bankAccountInfo = toBankAccountInfoEntity(dto.getBankAccountInfo());
            accountInfo.setBankAccountInfo(bankAccountInfo);
        }
        if (dto.getAcctSumFee() != null) {
            AccountSummaryFee accountSummaryFee = toAccountSummaryFeeEntity(dto.getAcctSumFee());
            accountInfo.setAcctSumFee(accountSummaryFee);
        }

        if (dto.getSubsInfo() != null) {
            SubscriberInfo subscriberInfo = toSubscriberInfoEntity(dto.getSubsInfo());
            accountInfo.setSubsInfo(subscriberInfo);
        }

        // Mapping for previously unmapped fields
        if (dto.getPrevBill() != null) {
            PrevBill prevBill = toPrevBillEntity(dto.getPrevBill());
            accountInfo.setPrevBill(prevBill);
        }
        if (dto.getCurBill() != null) {
            CurBill curBill = toCurBillEntity(dto.getCurBill());
            accountInfo.setCurBill(curBill);
        }
        if (dto.getCustCharge() != null) {
            CustCharge custCharge = toCustChargeEntity(dto.getCustCharge());
            accountInfo.setCustCharge(custCharge);
        }
        if (dto.getSumFees() != null) {
            List<SumFee> sumFees = dto.getSumFees().stream()
                    .map(this::toSumFeeEntity)
                    .collect(Collectors.toList());
            for (SumFee sumFee : sumFees) {
                sumFee.setAccountInfo(accountInfo); // Set parent on child
            }
            accountInfo.setSumFees(sumFees);
        }
        if (dto.getRatingTaxs() != null) {
            List<RatingTax> ratingTaxs = dto.getRatingTaxs().stream()
                    .map(this::toRatingTaxEntity)
                    .collect(Collectors.toList());
            for (RatingTax ratingTax : ratingTaxs) {
                ratingTax.setAccountInfo(accountInfo); // Set parent on child
            }
            accountInfo.setRatingTaxs(ratingTaxs);
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
        entity.setDescription(dto.getDesc());
        entity.setNumber(dto.getNumber());
        entity.setDuration(dto.getDuration());
        entity.setChgFee(dto.getChgFee());
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
            List<SubsSumFee> mappedSubsSumFees = dto.getSubsSumFees().stream()
                    .map(this::toSubsSumFeeEntity)
                    .collect(Collectors.toList());
            for (SubsSumFee subsSumFee : mappedSubsSumFees) {
                subsSumFee.setSubscriberInfo(entity); // Set parent
            }
            entity.setSubsSumFees(mappedSubsSumFees);
        }

        if (dto.getDetailChargeContainer() != null) {
            DetailChargeContainer container = toDetailChargeContainerEntity(dto.getDetailChargeContainer());
            entity.setDetailChargeContainer(container);
            container.setSubscriberInfo(entity); // Set bidirectional relationship
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

        if (dto.getFeeCategories() != null) {
            List<FeeCategory> mappedFeeCategories = dto.getFeeCategories().stream()
                    .map(this::toFeeCategoryEntity)
                    .collect(Collectors.toList());
            for (FeeCategory feeCategory : mappedFeeCategories) {
                feeCategory.setDetailChargeContainer(entity); // Set parent on child
            }
            entity.setFeeCategories(mappedFeeCategories);
        }

        if (dto.getCdrInfoDTOS() != null) {
            List<CdrInfo> mappedCdrInfos = dto.getCdrInfoDTOS().stream()
                    .map(this::toCdrInfoEntity)
                    .collect(Collectors.toList());
            for (CdrInfo cdrInfo : mappedCdrInfos) {
                cdrInfo.setDetailChargeContainer(entity); // Set parent on child
            }
            entity.setCdrInfos(mappedCdrInfos);
        }
        return entity;
    }


    private FeeCategory toFeeCategoryEntity(BillInfoDTO.FeeCategoryDTO dto) {
        if (dto == null) return null;
        FeeCategory entity = new FeeCategory();
        entity.setPayAcctCode(dto.getPayAcctCode());
        // entity.setFreeUnit(dto.getFreeUnit()); // Uncomment if FeeCategory also has freeUnit from XML

        if (dto.getDetailCharges() != null) {
            List<ChargeLine> mappedChargeLines = dto.getDetailCharges().stream()
                    .map(this::toChargeLineEntity)
                    .collect(Collectors.toList());
            for (ChargeLine chargeLine : mappedChargeLines) {
                chargeLine.setFeeCategory(entity); // Set parent on child
            }
            entity.setDetailCharges(mappedChargeLines);
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
        entity.setStartDate(parseDateTime(dto.getStartDate()));
        entity.setEndDate(parseDateTime(dto.getEndDate()));
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
        return entity; // No direct save here if cascading from AccountInfo
    }

    private CurBill toCurBillEntity(BillInfoDTO.CurBillDTO dto) {
        if (dto == null) return null;
        CurBill entity = new CurBill();
        entity.setCurrentCharges(dto.getCurrentCharges());
        // Map other fields from CurBillDTO to CurBill entity
        return entity; // No direct save here if cascading from AccountInfo
    }

    private CustCharge toCustChargeEntity(BillInfoDTO.CustChargeDTO dto) {
        if (dto == null) return null;
        CustCharge entity = new CustCharge();
        entity.setCategory(dto.getCategory());
        // Map other fields from CustChargeDTO to CustCharge entity
        return entity; // No direct save here if cascading from AccountInfo
    }

    private SumFee toSumFeeEntity(BillInfoDTO.SumFeeDTO dto) {
        if (dto == null) return null;
        SumFee entity = new SumFee();
        entity.setVoiceFlag(dto.getVoiceFlag());
        entity.setDesc(dto.getDesc());
        entity.setNumber(dto.getNumber());
        entity.setDuration(dto.getDuration());
        entity.setChgFee(dto.getChgFee());
        entity.setCurrency(dto.getCurrency());
//        entity.setPayFlag(dto.getPayFlag()); // Assuming PayFlag is also a field in SumFee entity/DTO
//        entity.setBillItemCode(dto.getBillItemCode()); // Assuming BillItemCode is also a field in SumFee entity/DTO
        return entity; // No direct save here if cascading from AccountInfo
    }

    private RatingTax toRatingTaxEntity(BillInfoDTO.RatingTaxDTO dto) {
        if (dto == null) return null;
        RatingTax entity = new RatingTax();
        entity.setTaxAmount(dto.getTaxAmount());
        // Map other fields from RatingTaxDTO to RatingTax entity
        return entity; // No direct save here if cascading from AccountInfo
    }

    // Helper method for parsing date/time strings
    private LocalDateTime parseDateTime(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        String trimmedDateString = dateString.trim();
        try {
            // Try parsing with YYYYMMDD HH:mm:ss format first (e.g., "20241201 00:00:00")
            return LocalDateTime.parse(trimmedDateString, DATE_TIME_FORMATTER_YYYYMMDD);
        } catch (DateTimeParseException e1) {
            try {
                // Try parsing with dd/MM/yyyy HH:mm:ss format (e.g., "10/01/2025 00:00:00")
                return LocalDateTime.parse(trimmedDateString, DATE_TIME_FORMATTER_DDMMYYYY);
            } catch (DateTimeParseException e2) {
                try {
                    // Try parsing with YYYYMMDD format (e.g., "20241201") and append default time
                    return LocalDateTime.parse(trimmedDateString + " 00:00:00", DATE_TIME_FORMATTER_YYYYMMDD);
                } catch (DateTimeParseException e3) {
                    try {
                        // Try parsing with dd/MM/yyyy format (e.g., "10/01/2025") and append default time
                        return LocalDateTime.parse(trimmedDateString + " 00:00:00", DATE_TIME_FORMATTER_DDMMYYYY);
                    } catch (DateTimeParseException e4) {
                        System.err.println("Could not parse date string: '" + dateString + "' - All formats failed.");
                        return null; // Or throw a custom exception, or handle as needed
                    }
                }
            }
        }
    }
}
