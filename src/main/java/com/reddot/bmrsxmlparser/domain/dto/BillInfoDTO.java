package com.reddot.bmrsxmlparser.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlElement; // Make sure this is imported if used elsewhere for actual elements

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@JacksonXmlRootElement(localName = "BILL_INFO")
@XmlRootElement(name = "BILL_INFO") // This is key for JAXB to recognize the root
@XmlAccessorType(XmlAccessType.FIELD) // Tells JAXB to access fields directly
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillInfoDTO {

    @JacksonXmlProperty(localName = "XML_INFO")
    private String xmlInfo;

    @JacksonXmlProperty(localName = "OPER_INFO")
    private String operInfo;

    // BILL_CYCLE_INFO should be in the root DTO class (BillInfoDTO)
    @JacksonXmlProperty(localName = "BILL_CYCLE_INFO")
    // Removed: @XmlElement(name = "BILL_CYCLE_ID") - this was incorrect.
    private BillCycleInfoDTO billCycleInfo;

    @JacksonXmlProperty(localName = "BILL_RUN")
    private BillRunDTO billRun;

    // Sub-types for nested classes
    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @XmlAccessorType(XmlAccessType.FIELD) // Add this for consistency with JAXB mapping
    public static class BillCycleInfoDTO {
        @JacksonXmlProperty(localName = "BILL_CYCLE_ID")
        @XmlElement(name = "BILL_CYCLE_ID") // Keep JAXB annotation for actual field
        private String billCycleId;
        @JacksonXmlProperty(localName = "BILL_CYCLE_BEGIN")
        @XmlElement(name = "BILL_CYCLE_BEGIN")
        private String billCycleBegin;
        @JacksonXmlProperty(localName = "BILL_CYCLE_END")
        @XmlElement(name = "BILL_CYCLE_END")
        private String billCycleEnd;
        @JacksonXmlProperty(localName = "DUE_DATE")
        @XmlElement(name = "DUE_DATE")
        private String dueDate;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @XmlAccessorType(XmlAccessType.FIELD) // Add this for consistency with JAXB mapping
    public static class BillRunDTO {
        @JacksonXmlProperty(localName = "BILL_PROP") private BillPropDTO billProp;
        @JacksonXmlProperty(localName = "CUSTOM_INFO") private CustomInfoDTO customInfo;
        @JacksonXmlProperty(localName = "ACCT_INFO") private AccountInfoDTO accountInfo;

        @JacksonXmlProperty(localName = "BILL_CYCLE_INFO")
        @JsonIgnoreProperties(ignoreUnknown = true)
        // Removed: @XmlElement(name = "BILL_CYCLE_ID") - this was incorrect.
        private BillCycleInfoDTO billCycleInfo;

        // Handle FEE_CATEGORY and CDR_INFO directly under BILL_RUN
        @JacksonXmlProperty(localName = "FEE_CATEGORY")
        @JacksonXmlElementWrapper(useWrapping = false)
        @XmlElement(name = "FEE_CATEGORY") // Add for JAXB list elements
        private List<FeeCategoryDTO> feeCategories;

        @JacksonXmlProperty(localName = "CDR_INFO")
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonIgnoreProperties(ignoreUnknown = true)
        @XmlElement(name = "CDR_INFO") // Add for JAXB list elements
        private List<CdrInfoDTO> cdrInfos;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class BillPropDTO {
        @JacksonXmlProperty(localName = "INVOICE_ID") @XmlElement(name = "INVOICE_ID") private String invoiceId;
        @JacksonXmlProperty(localName = "INVOICE_DATE") @XmlElement(name = "INVOICE_DATE") private String invoiceDate;
        @JacksonXmlProperty(localName = "INVOICE_NO") @XmlElement(name = "INVOICE_NO") private String invoiceNo;
        @JacksonXmlProperty(localName = "BAR_CODE_NUMBER") @XmlElement(name = "BAR_CODE_NUMBER") private String barCodeNumber;
        @JacksonXmlProperty(localName = "STATEMENT_DATE") @XmlElement(name = "STATEMENT_DATE") private String statementDate;
        @JacksonXmlProperty(localName = "BILL_PERIOD") @XmlElement(name = "BILL_PERIOD") private String billPeriod;
        @JacksonXmlProperty(localName = "FROM_DATE") @XmlElement(name = "FROM_DATE") private String fromDate;
        @JacksonXmlProperty(localName = "TO_DATE") @XmlElement(name = "TO_DATE") private String toDate;
        @JacksonXmlProperty(localName = "DUE_DATE") @XmlElement(name = "DUE_DATE") private String dueDate;
        @JacksonXmlProperty(localName = "LANG") @XmlElement(name = "LANG") private String lang;
        @JacksonXmlProperty(localName = "CURRENCY") @XmlElement(name = "CURRENCY") private String currency;
        @JacksonXmlProperty(localName = "BILL_MEDIUM") @XmlElement(name = "BILL_MEDIUM") private String billMedium;
        @JacksonXmlProperty(localName = "FEE_PRECISION") @XmlElement(name = "FEE_PRECISION") private String feePrecision;
        @JacksonXmlProperty(localName = "BILL_INSERT") @XmlElement(name = "BILL_INSERT") private String billInsert;
        @JacksonXmlProperty(localName = "BILL_TYPE") @XmlElement(name = "BILL_TYPE") private String billType;
        @JacksonXmlProperty(localName = "MOBILE_NUMBER") @XmlElement(name = "MOBILE_NUMBER") private String mobileNumber;
        @JacksonXmlProperty(localName = "END_DATE") @XmlElement(name = "END_DATE") private String endDate;
        @JacksonXmlProperty(localName = "MAIN_OFFERING_NAME") @XmlElement(name = "MAIN_OFFERING_NAME") private String mainOfferingName;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class CustomInfoDTO {
        @JacksonXmlProperty(localName = "CUST_GENDER") @XmlElement(name = "CUST_GENDER") private String custGender;
        @JacksonXmlProperty(localName = "CUST_TYPE") @XmlElement(name = "CUST_TYPE") private String custType;
        @JacksonXmlProperty(localName = "CUST_LEVEL") @XmlElement(name = "CUST_LEVEL") private String custLevel;
        @JacksonXmlProperty(localName = "CUST_ID") @XmlElement(name = "CUST_ID") private String custId;
        @JacksonXmlProperty(localName = "VAT_NUMBER") @XmlElement(name = "VAT_NUMBER") private String vatNumber;
        @JacksonXmlProperty(localName = "CUST_CODE") @XmlElement(name = "CUST_CODE") private String custCode;
        @JacksonXmlProperty(localName = "CUST_NAME") @XmlElement(name = "CUST_NAME") private NameDTO custName;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class NameDTO {
        @JacksonXmlProperty(localName = "TITLE") @XmlElement(name = "TITLE") private String title;
        @JacksonXmlProperty(localName = "FIRST_NAME") @XmlElement(name = "FIRST_NAME") private String firstName;
        @JacksonXmlProperty(localName = "MIDDLE_NAME") @XmlElement(name = "MIDDLE_NAME") private String middleName;
        @JacksonXmlProperty(localName = "LAST_NAME") @XmlElement(name = "LAST_NAME") private String lastName;
        @JacksonXmlProperty(localName = "FULL_NAME") @XmlElement(name = "FULL_NAME") private String fullName;
    }

    @Data @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class AccountInfoDTO {
        @JacksonXmlProperty(localName = "ACCT_ID") @XmlElement(name = "ACCT_ID") private String acctId;
        @JacksonXmlProperty(localName = "ACCT_CODE") @XmlElement(name = "ACCT_CODE") private String acctCode;
        @JacksonXmlProperty(localName = "ACCT_NUMBER") @XmlElement(name = "ACCT_NUMBER") private String acctNumber;
        @JacksonXmlProperty(localName = "ACCT_NAME") @XmlElement(name = "ACCT_NAME") private NameDTO acctName;

        @JacksonXmlProperty(localName = "Deposit") @XmlElement(name = "Deposit") private BigDecimal deposit;
        @JacksonXmlProperty(localName = "PREVIOUS_BALANCE") @XmlElement(name = "PREVIOUS_BALANCE") private BigDecimal previousBalance;
        @JacksonXmlProperty(localName = "PAYMENTS") @XmlElement(name = "PAYMENTS") private BigDecimal payments;
        @JacksonXmlProperty(localName = "ADJUSTMENTS") @XmlElement(name = "ADJUSTMENTS") private BigDecimal adjustments;
        @JacksonXmlProperty(localName = "CURRENT_CHARGES") @XmlElement(name = "CURRENT_CHARGES") private BigDecimal currentCharges;
        @JacksonXmlProperty(localName = "AMOUNT_DUE") @XmlElement(name = "AMOUNT_DUE") private BigDecimal amountDue;

        @JacksonXmlProperty(localName = "ADDRESSTYPE") @XmlElement(name = "ADDRESSTYPE") private AddressTypeDTO addressType;

        @JacksonXmlProperty(localName = "CREDIT_LIMIT") @XmlElement(name = "CREDIT_LIMIT") private BigDecimal creditLimit;
        @JacksonXmlProperty(localName = "PAY_METHOD") @XmlElement(name = "PAY_METHOD") private String payMethod;
        @JacksonXmlProperty(localName = "PRINT_VATNO") @XmlElement(name = "PRINT_VATNO") private String printVatNo;

        @JacksonXmlProperty(localName = "BANK_ACCT_INFO") @XmlElement(name = "BANK_ACCT_INFO") private BankAccountInfoDTO bankAccountInfo;
        @JacksonXmlProperty(localName = "ACCT_SUMFEE") @XmlElement(name = "ACCT_SUMFEE") private AccountSummaryFeeDTO acctSumFee;

        @JacksonXmlProperty(localName = "SUBS_INFO") @XmlElement(name = "SUBS_INFO") private SubscriberInfoDTO subsInfo;

        @JacksonXmlProperty(localName = "FEE_CATEGORY")
        @JacksonXmlElementWrapper(useWrapping = false)
        @XmlElement(name = "FEE_CATEGORY") // Add for JAXB list elements
        private List<FeeCategoryDTO> feeCategories;

        @JacksonXmlProperty(localName = "CDR_INFO")
        @JacksonXmlElementWrapper(useWrapping = false)
        @XmlElement(name = "CDR_INFO") // Add for JAXB list elements
        private List<CdrInfoDTO> cdrInfos;
    }

    @Data @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class AddressTypeDTO {
        @JacksonXmlProperty(localName = "ADDR_TYPE") @XmlElement(name = "ADDR_TYPE") private String addrType;
        @JacksonXmlProperty(localName = "ADDR1") @XmlElement(name = "ADDR1") private String addr1;
        @JacksonXmlProperty(localName = "ADDR2") @XmlElement(name = "ADDR2") private String addr2;
        @JacksonXmlProperty(localName = "ADDR3") @XmlElement(name = "ADDR3") private String addr3;
        @JacksonXmlProperty(localName = "ADDR4") @XmlElement(name = "ADDR4") private String addr4;
        @JacksonXmlProperty(localName = "ADDR5") @XmlElement(name = "ADDR5") private String addr5;
        @JacksonXmlProperty(localName = "ADDR6") @XmlElement(name = "ADDR6") private String addr6;
        @JacksonXmlProperty(localName = "ZIPCODE") @XmlElement(name = "ZIPCODE") private String zipcode;
        @JacksonXmlProperty(localName = "EMAILADDR") @XmlElement(name = "EMAILADDR") private String emailAddr;
        @JacksonXmlProperty(localName = "SMSADDR") @XmlElement(name = "SMSADDR") private String smsAddr;
    }

    @Data @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class BankAccountInfoDTO {
        @JacksonXmlProperty(localName = "BANK_NO") @XmlElement(name = "BANK_NO") private String bankNo;
        @JacksonXmlProperty(localName = "BANK_NAME") @XmlElement(name = "BANK_NAME") private String bankName;
        @JacksonXmlProperty(localName = "CARD_EXPDATE") @XmlElement(name = "CARD_EXPDATE") private String cardExpDate;
        @JacksonXmlProperty(localName = "BANK_ACCTNAME") @XmlElement(name = "BANK_ACCTNAME") private String bankAcctName;
        @JacksonXmlProperty(localName = "BANK_ACCTTYPE") @XmlElement(name = "BANK_ACCTTYPE") private String bankAcctType;
    }

    @Data @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class AccountSummaryFeeDTO {
        @JacksonXmlProperty(localName = "VOICE_FLAG") @XmlElement(name = "VOICE_FLAG") private String voiceFlag;
        @JacksonXmlProperty(localName = "DESC") @XmlElement(name = "DESC") private String desc;
        @JacksonXmlProperty(localName = "NUMBER") @XmlElement(name = "NUMBER") private String number;
        @JacksonXmlProperty(localName = "DURATION") @XmlElement(name = "DURATION") private String duration;
        @JacksonXmlProperty(localName = "CHG_FEE") @XmlElement(name = "CHG_FEE") private String chgFee;
        @JacksonXmlProperty(localName = "CURRENCY") @XmlElement(name = "CURRENCY") private String currency;
    }

    @Data @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class SubscriberInfoDTO {
        @JacksonXmlProperty(localName = "MSISDN") @XmlElement(name = "MSISDN") private String msisdn;
        @JacksonXmlProperty(localName = "SUB_ID") @XmlElement(name = "SUB_ID") private String subId;
        @JacksonXmlProperty(localName = "ACCT_CODE") @XmlElement(name = "ACCT_CODE") private String acctCode;
        @JacksonXmlProperty(localName = "SUB_NAME") @XmlElement(name = "SUB_NAME") private NameDTO subName;

        @JacksonXmlProperty(localName = "SUBS_SUMFEE")
        @JacksonXmlElementWrapper(useWrapping = false)
        @XmlElement(name = "SUBS_SUMFEE") // Add for JAXB list elements
        private List<SubsSumFeeDTO> subsSumFees;

        // DETAIL_CHARGE -> contains many FEE_CATEGORY
        @JacksonXmlProperty(localName = "DETAIL_CHARGE")
        @XmlElement(name = "DETAIL_CHARGE") // Add for JAXB elements
        private DetailChargeContainerDTO detailChargeContainer;
    }

    @Data @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class DetailChargeContainerDTO {
        @JacksonXmlProperty(localName = "FEE_CATEGORY")
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonIgnoreProperties(ignoreUnknown = true)
        @XmlElement(name = "FEE_CATEGORY") // Add for JAXB list elements
        private List<FeeCategoryDTO> feeCategories;

        @JsonIgnoreProperties(ignoreUnknown = true)
        @JacksonXmlProperty(localName = "CDR_INFO")
        @JacksonXmlElementWrapper(useWrapping = false)
        @XmlElement(name = "CDR_INFO") // Add for JAXB list elements
        private List<CdrInfoDTO> cdrInfoDTOS;

        @JacksonXmlProperty(localName = "FREEUNIT") @XmlElement(name = "FREEUNIT")
        private String freeUnit;

    }

    @Data @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class SubsSumFeeDTO {
        @JacksonXmlProperty(localName = "VOICE_FLAG") @XmlElement(name = "VOICE_FLAG") private String voiceFlag;
        @JacksonXmlProperty(localName = "ROAM_FLAG") @XmlElement(name = "ROAM_FLAG") private String roamFlag;
        @JacksonXmlProperty(localName = "DESC") @XmlElement(name = "DESC") private String desc;
        @JacksonXmlProperty(localName = "NUMBER") @XmlElement(name = "NUMBER") private String number;
        @JacksonXmlProperty(localName = "DURATION") @XmlElement(name = "DURATION") private String duration;
        @JacksonXmlProperty(localName = "CHG_FEE") @XmlElement(name = "CHG_FEE") private BigDecimal chgFee;
        @JacksonXmlProperty(localName = "CURRENCY") @XmlElement(name = "CURRENCY") private String currency;
        @JacksonXmlProperty(localName = "PAY_FLAG") @XmlElement(name = "PAY_FLAG") private String payFlag;
        @JacksonXmlProperty(localName = "BILLITEM_CODE") @XmlElement(name = "BILLITEM_CODE") private String billItemCode;
    }

    @Data @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class FeeCategoryDTO {
        @JacksonXmlProperty(localName = "PayAcctCode") @XmlElement(name = "PayAcctCode") private String payAcctCode;

        @JacksonXmlProperty(localName = "DETAIL_CHARGE")
        @JacksonXmlElementWrapper(useWrapping = false)
        @XmlElement(name = "DETAIL_CHARGE") // Add for JAXB list elements
        private List<ChargeLineDTO> detailCharges;
    }

    @Data @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ChargeLineDTO {
        @JacksonXmlProperty(localName = "CHG_FEE") @XmlElement(name = "CHG_FEE") private BigDecimal chgFee;
        @JacksonXmlProperty(localName = "DESC") @XmlElement(name = "DESC") private String desc;
        @JacksonXmlProperty(localName = "CATEGORY") @XmlElement(name = "CATEGORY") private String category;
        @JacksonXmlProperty(localName = "SUBCATEGORY") @XmlElement(name = "SUBCATEGORY") private String subcategory;
        @JacksonXmlProperty(localName = "CYCLENO") @XmlElement(name = "CYCLENO") private String cycleNo;
        @JacksonXmlProperty(localName = "SERVICETYPE") @XmlElement(name = "SERVICETYPE") private String serviceType;
        @JacksonXmlProperty(localName = "CHARGECODE") @XmlElement(name = "CHARGECODE") private String chargeCode;
        @JacksonXmlProperty(localName = "ITEMID") @XmlElement(name = "ITEMID") private String itemId;

        @JacksonXmlProperty(localName = "RATINGTAX1") @XmlElement(name = "RATINGTAX1") private BigDecimal ratingTax1;
        @JacksonXmlProperty(localName = "RATINGTAX2") @XmlElement(name = "RATINGTAX2") private BigDecimal ratingTax2;
        @JacksonXmlProperty(localName = "RATINGTAX3") @XmlElement(name = "RATINGTAX3") private BigDecimal ratingTax3;
        @JacksonXmlProperty(localName = "RATINGTAX4") @XmlElement(name = "RATINGTAX4") private BigDecimal ratingTax4;

        @JacksonXmlProperty(localName = "DISC1") @XmlElement(name = "DISC1") private BigDecimal disc1;
        @JacksonXmlProperty(localName = "DISC2") @XmlElement(name = "DISC2") private BigDecimal disc2;
        @JacksonXmlProperty(localName = "DISC3") @XmlElement(name = "DISC3") private BigDecimal disc3;

        @JacksonXmlProperty(localName = "STARTDATE") @XmlElement(name = "STARTDATE") private String startDate;
        @JacksonXmlProperty(localName = "ENDDATE") @XmlElement(name = "ENDDATE") private String endDate;
        @JacksonXmlProperty(localName = "CURRENCY") @XmlElement(name = "CURRENCY") private String currency;
    }

    @Data @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class CdrInfoDTO {
        @JacksonXmlProperty(localName = "CALL_FLOW") @XmlElement(name = "CALL_FLOW") private String callFlow;
        @JacksonXmlProperty(localName = "OTHER_NUM") @XmlElement(name = "OTHER_NUM") private String otherNum;
        @JacksonXmlProperty(localName = "CALL_MASK") @XmlElement(name = "CALL_MASK") private String callMask;
        @JacksonXmlProperty(localName = "DATE_TIME") @XmlElement(name = "DATE_TIME") private String dateTime;
        @JacksonXmlProperty(localName = "CHG_AMT") @XmlElement(name = "CHG_AMT") private BigDecimal chgAmt;
        @JacksonXmlProperty(localName = "TAX_AMT1") @XmlElement(name = "TAX_AMT1") private BigDecimal taxAmt1;
        @JacksonXmlProperty(localName = "TAX_AMT2") @XmlElement(name = "TAX_AMT2") private BigDecimal taxAmt2;
        @JacksonXmlProperty(localName = "TAX_AMT3") @XmlElement(name = "TAX_AMT3") private BigDecimal taxAmt3;
        @JacksonXmlProperty(localName = "VOLUME") @XmlElement(name = "VOLUME") private String volume;
        @JacksonXmlProperty(localName = "SERVICE_TYPE") @XmlElement(name = "SERVICE_TYPE") private String serviceType;
        @JacksonXmlProperty(localName = "START_TIME") @XmlElement(name = "START_TIME") private String startTime;
        @JacksonXmlProperty(localName = "END_TIME") @XmlElement(name = "END_TIME") private String endTime;
        @JacksonXmlProperty(localName = "DURATION") @XmlElement(name = "DURATION") private String duration;
        @JacksonXmlProperty(localName = "RESULT_REASON") @XmlElement(name = "RESULT_REASON") private String resultReason;
        @JacksonXmlProperty(localName = "SERVICE_FLOW") @XmlElement(name = "SERVICE_FLOW") private String serviceFlow;
        @JacksonXmlProperty(localName = "NETWORK") @XmlElement(name = "NETWORK") private String network;
        @JacksonXmlProperty(localName = "COUNTRY") @XmlElement(name = "COUNTRY") private String country;
        @JacksonXmlProperty(localName = "OPERATOR") @XmlElement(name = "OPERATOR") private String operator;
        @JacksonXmlProperty(localName = "GPRS_FLAG") @XmlElement(name = "GPRS_FLAG") private String gprsFlag;
        @JacksonXmlProperty(localName = "CF_NUM") @XmlElement(name = "CF_NUM") private String cfNum;
    }
}
