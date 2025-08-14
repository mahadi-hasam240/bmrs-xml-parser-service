package com.reddot.bmrsxmlparser.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@JacksonXmlRootElement(localName = "BILL_INFO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillInfoDTO {

    @JacksonXmlProperty(localName = "XML_INFO")
    private String xmlInfo;

    @JacksonXmlProperty(localName = "OPER_INFO")
    private String operInfo;

    // BILL_CYCLE_INFO should be in the root DTO class (BillInfoDTO)
    @JacksonXmlProperty(localName = "BILL_CYCLE_INFO")
    private BillCycleInfoDTO billCycleInfo;

    @JacksonXmlProperty(localName = "BILL_RUN")
    private BillRunDTO billRun;

    // Sub-types for nested classes
    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BillCycleInfoDTO {
        @JacksonXmlProperty(localName = "BILL_CYCLE_ID") private String billCycleId;
        @JacksonXmlProperty(localName = "BILL_CYCLE_BEGIN") private String billCycleBegin;
        @JacksonXmlProperty(localName = "BILL_CYCLE_END") private String billCycleEnd;
        @JacksonXmlProperty(localName = "DUE_DATE") private String dueDate;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BillRunDTO {
        @JacksonXmlProperty(localName = "BILL_PROP") private BillPropDTO billProp;
        @JacksonXmlProperty(localName = "CUSTOM_INFO") private CustomInfoDTO customInfo;
        @JacksonXmlProperty(localName = "ACCT_INFO") private AccountInfoDTO accountInfo;

        @JacksonXmlProperty(localName = "BILL_CYCLE_INFO")
        @JsonIgnoreProperties(ignoreUnknown = true)
        private BillCycleInfoDTO billCycleInfo;

        // Handle FEE_CATEGORY and CDR_INFO directly under BILL_RUN
        @JacksonXmlProperty(localName = "FEE_CATEGORY")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<FeeCategoryDTO> feeCategories;

        @JacksonXmlProperty(localName = "CDR_INFO")
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonIgnoreProperties(ignoreUnknown = true)
        private List<CdrInfoDTO> cdrInfos;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BillPropDTO {
        @JacksonXmlProperty(localName = "INVOICE_ID") private String invoiceId;
        @JacksonXmlProperty(localName = "INVOICE_DATE") private String invoiceDate;
        @JacksonXmlProperty(localName = "INVOICE_NO") private String invoiceNo;
        @JacksonXmlProperty(localName = "BAR_CODE_NUMBER") private String barCodeNumber;
        @JacksonXmlProperty(localName = "STATEMENT_DATE") private String statementDate;
        @JacksonXmlProperty(localName = "BILL_PERIOD") private String billPeriod;
        @JacksonXmlProperty(localName = "FROM_DATE") private String fromDate;
        @JacksonXmlProperty(localName = "TO_DATE") private String toDate;
        @JacksonXmlProperty(localName = "DUE_DATE") private String dueDate;
        @JacksonXmlProperty(localName = "LANG") private String lang;
        @JacksonXmlProperty(localName = "CURRENCY") private String currency;
        @JacksonXmlProperty(localName = "BILL_MEDIUM") private String billMedium;
        @JacksonXmlProperty(localName = "FEE_PRECISION") private String feePrecision;
        @JacksonXmlProperty(localName = "BILL_INSERT") private String billInsert;
        @JacksonXmlProperty(localName = "BILL_TYPE") private String billType;
        @JacksonXmlProperty(localName = "MOBILE_NUMBER") private String mobileNumber;
        @JacksonXmlProperty(localName = "END_DATE") private String endDate;
        @JacksonXmlProperty(localName = "MAIN_OFFERING_NAME") private String mainOfferingName;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CustomInfoDTO {
        @JacksonXmlProperty(localName = "CUST_GENDER") private String custGender;
        @JacksonXmlProperty(localName = "CUST_TYPE") private String custType;
        @JacksonXmlProperty(localName = "CUST_LEVEL") private String custLevel;
        @JacksonXmlProperty(localName = "CUST_ID") private String custId;
        @JacksonXmlProperty(localName = "VAT_NUMBER") private String vatNumber;
        @JacksonXmlProperty(localName = "CUST_CODE") private String custCode;
        @JacksonXmlProperty(localName = "CUST_NAME") private NameDTO custName;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NameDTO {
        @JacksonXmlProperty(localName = "TITLE") private String title;
        @JacksonXmlProperty(localName = "FIRST_NAME") private String firstName;
        @JacksonXmlProperty(localName = "MIDDLE_NAME") private String middleName;
        @JacksonXmlProperty(localName = "LAST_NAME") private String lastName;
        @JacksonXmlProperty(localName = "FULL_NAME") private String fullName;
    }

    @Data @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AccountInfoDTO {
        @JacksonXmlProperty(localName = "ACCT_ID")       private String acctId;
        @JacksonXmlProperty(localName = "ACCT_CODE")     private String acctCode;
        @JacksonXmlProperty(localName = "ACCT_NUMBER")   private String acctNumber;
        @JacksonXmlProperty(localName = "ACCT_NAME")     private NameDTO acctName;

        @JacksonXmlProperty(localName = "Deposit")           private BigDecimal deposit;
        @JacksonXmlProperty(localName = "PREVIOUS_BALANCE")  private BigDecimal previousBalance;
        @JacksonXmlProperty(localName = "PAYMENTS")          private BigDecimal payments;
        @JacksonXmlProperty(localName = "ADJUSTMENTS")       private BigDecimal adjustments;
        @JacksonXmlProperty(localName = "CURRENT_CHARGES")   private BigDecimal currentCharges;
        @JacksonXmlProperty(localName = "AMOUNT_DUE")        private BigDecimal amountDue;

        @JacksonXmlProperty(localName = "ADDRESSTYPE")   private AddressTypeDTO addressType;

        @JacksonXmlProperty(localName = "CREDIT_LIMIT")  private BigDecimal creditLimit;
        @JacksonXmlProperty(localName = "PAY_METHOD")    private String payMethod;
        @JacksonXmlProperty(localName = "PRINT_VATNO")   private String printVatNo;

        @JacksonXmlProperty(localName = "BANK_ACCT_INFO") private BankAccountInfoDTO bankAccountInfo;
        @JacksonXmlProperty(localName = "ACCT_SUMFEE")    private AccountSummaryFeeDTO acctSumFee;

        @JacksonXmlProperty(localName = "SUBS_INFO")      private SubscriberInfoDTO subsInfo;

        @JacksonXmlProperty(localName = "FEE_CATEGORY")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<FeeCategoryDTO> feeCategories;

        @JacksonXmlProperty(localName = "CDR_INFO")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<CdrInfoDTO> cdrInfos;
    }

    @Data @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AddressTypeDTO {
        @JacksonXmlProperty(localName = "ADDR_TYPE")  private String addrType;
        @JacksonXmlProperty(localName = "ADDR1")      private String addr1;
        @JacksonXmlProperty(localName = "ADDR2")      private String addr2;
        @JacksonXmlProperty(localName = "ADDR3")      private String addr3;
        @JacksonXmlProperty(localName = "ADDR4")      private String addr4;
        @JacksonXmlProperty(localName = "ADDR5")      private String addr5;
        @JacksonXmlProperty(localName = "ADDR6")      private String addr6;
        @JacksonXmlProperty(localName = "ZIPCODE")    private String zipcode;
        @JacksonXmlProperty(localName = "EMAILADDR")  private String emailAddr;
        @JacksonXmlProperty(localName = "SMSADDR")    private String smsAddr;
    }

    @Data @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BankAccountInfoDTO {
        @JacksonXmlProperty(localName = "BANK_NO")       private String bankNo;
        @JacksonXmlProperty(localName = "BANK_NAME")     private String bankName;
        @JacksonXmlProperty(localName = "CARD_EXPDATE")  private String cardExpDate;
        @JacksonXmlProperty(localName = "BANK_ACCTNAME") private String bankAcctName;
        @JacksonXmlProperty(localName = "BANK_ACCTTYPE") private String bankAcctType;
    }

    @Data @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AccountSummaryFeeDTO {
        @JacksonXmlProperty(localName = "VOICE_FLAG") private String voiceFlag;
        @JacksonXmlProperty(localName = "DESC")       private String desc;
        @JacksonXmlProperty(localName = "NUMBER")     private String number;
        @JacksonXmlProperty(localName = "DURATION")   private String duration;
        @JacksonXmlProperty(localName = "CHG_FEE")    private String chgFee; // left String in sample
        @JacksonXmlProperty(localName = "CURRENCY")   private String currency;
    }

    @Data @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SubscriberInfoDTO {
        @JacksonXmlProperty(localName = "MSISDN")    private String msisdn;
        @JacksonXmlProperty(localName = "SUB_ID")    private String subId;
        @JacksonXmlProperty(localName = "ACCT_CODE") private String acctCode;
        @JacksonXmlProperty(localName = "SUB_NAME")  private NameDTO subName;

        @JacksonXmlProperty(localName = "SUBS_SUMFEE")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<SubsSumFeeDTO> subsSumFees;

        // DETAIL_CHARGE -> contains many FEE_CATEGORY
        @JacksonXmlProperty(localName = "DETAIL_CHARGE")
        private DetailChargeContainerDTO detailChargeContainer;
    }

    @Data @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DetailChargeContainerDTO {
        @JacksonXmlProperty(localName = "FEE_CATEGORY")
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonIgnoreProperties(ignoreUnknown = true)
        private List<FeeCategoryDTO> feeCategories;

        @JsonIgnoreProperties(ignoreUnknown = true)
        @JacksonXmlProperty(localName = "CDR_INFO")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<CdrInfoDTO> cdrInfoDTOS;

        @JacksonXmlProperty(localName = "FREEUNIT")
        private String freeUnit;

    }

    @Data @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SubsSumFeeDTO {
        @JacksonXmlProperty(localName = "VOICE_FLAG")   private String voiceFlag;
        @JacksonXmlProperty(localName = "ROAM_FLAG")    private String roamFlag;
        @JacksonXmlProperty(localName = "DESC")         private String desc;
        @JacksonXmlProperty(localName = "NUMBER")       private String number;
        @JacksonXmlProperty(localName = "DURATION")     private String duration;
        @JacksonXmlProperty(localName = "CHG_FEE")      private BigDecimal chgFee;
        @JacksonXmlProperty(localName = "CURRENCY")     private String currency;
        @JacksonXmlProperty(localName = "PAY_FLAG")     private String payFlag;
        @JacksonXmlProperty(localName = "BILLITEM_CODE")private String billItemCode;
    }

    @Data @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FeeCategoryDTO {
        @JacksonXmlProperty(localName = "PayAcctCode") private String payAcctCode;

        @JacksonXmlProperty(localName = "DETAIL_CHARGE")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<ChargeLineDTO> detailCharges;
    }

    @Data @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChargeLineDTO {
        @JacksonXmlProperty(localName = "CHG_FEE")     private BigDecimal chgFee;
        @JacksonXmlProperty(localName = "DESC")        private String desc;
        @JacksonXmlProperty(localName = "CATEGORY")    private String category;
        @JacksonXmlProperty(localName = "SUBCATEGORY") private String subcategory;
        @JacksonXmlProperty(localName = "CYCLENO")     private String cycleNo;
        @JacksonXmlProperty(localName = "SERVICETYPE") private String serviceType;
        @JacksonXmlProperty(localName = "CHARGECODE")  private String chargeCode;
        @JacksonXmlProperty(localName = "ITEMID")      private String itemId;

        @JacksonXmlProperty(localName = "RATINGTAX1")  private BigDecimal ratingTax1;
        @JacksonXmlProperty(localName = "RATINGTAX2")  private BigDecimal ratingTax2;
        @JacksonXmlProperty(localName = "RATINGTAX3")  private BigDecimal ratingTax3;
        @JacksonXmlProperty(localName = "RATINGTAX4")  private BigDecimal ratingTax4;

        @JacksonXmlProperty(localName = "DISC1")       private BigDecimal disc1;
        @JacksonXmlProperty(localName = "DISC2")       private BigDecimal disc2;
        @JacksonXmlProperty(localName = "DISC3")       private BigDecimal disc3;

        @JacksonXmlProperty(localName = "STARTDATE")   private String startDate;
        @JacksonXmlProperty(localName = "ENDDATE")     private String endDate;
        @JacksonXmlProperty(localName = "CURRENCY")    private String currency;
    }

    @Data @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CdrInfoDTO {
        @JacksonXmlProperty(localName = "CALL_FLOW")    private String callFlow;
        @JacksonXmlProperty(localName = "OTHER_NUM")    private String otherNum;
        @JacksonXmlProperty(localName = "CALL_MASK")    private String callMask;
        @JacksonXmlProperty(localName = "DATE_TIME")    private String dateTime;
        @JacksonXmlProperty(localName = "CHG_AMT")      private BigDecimal chgAmt;
        @JacksonXmlProperty(localName = "TAX_AMT1")     private BigDecimal taxAmt1;
        @JacksonXmlProperty(localName = "TAX_AMT2")     private BigDecimal taxAmt2;
        @JacksonXmlProperty(localName = "TAX_AMT3")     private BigDecimal taxAmt3;
        @JacksonXmlProperty(localName = "VOLUME")       private String volume;
        @JacksonXmlProperty(localName = "SERVICE_TYPE") private String serviceType;
        @JacksonXmlProperty(localName = "START_TIME")   private String startTime;
        @JacksonXmlProperty(localName = "END_TIME")     private String endTime;
        @JacksonXmlProperty(localName = "DURATION")     private String duration;
        @JacksonXmlProperty(localName = "RESULT_REASON")private String resultReason;
        @JacksonXmlProperty(localName = "SERVICE_FLOW") private String serviceFlow;
        @JacksonXmlProperty(localName = "NETWORK")      private String network;
        @JacksonXmlProperty(localName = "COUNTRY")      private String country;
        @JacksonXmlProperty(localName = "OPERATOR")     private String operator;
        @JacksonXmlProperty(localName = "GPRS_FLAG")    private String gprsFlag;
        @JacksonXmlProperty(localName = "CF_NUM")       private String cfNum;
    }
}
