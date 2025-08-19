package com.reddot.bmrsxmlparser.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "account_info")
@SequenceGenerator(name = "account_info_seq", sequenceName = "account_info_id_seq", allocationSize = 1)
@Data
public class AccountInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_info_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "acct_id")
    private String acctId;

    @Column(name = "acct_code")
    private String acctCode;

    @Column(name = "acct_number")
    private String acctNumber;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "acct_name_id", referencedColumnName = "id")
    private Name acctName;

    @Column(name = "deposit")
    private BigDecimal deposit;

    @Column(name = "previous_balance")
    private BigDecimal previousBalance;

    @Column(name = "payments")
    private BigDecimal payments;

    @Column(name = "adjustments")
    private BigDecimal adjustments;

    @Column(name = "current_charges")
    private BigDecimal currentCharges;

    @Column(name = "amount_due")
    private BigDecimal amountDue;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_type_id", referencedColumnName = "id")
    private AddressType addressType;

    @Column(name = "credit_limit")
    private BigDecimal creditLimit;

    @Column(name = "pay_method")
    private String payMethod;

    @Column(name = "print_vatno")
    private String printVatNo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bank_account_info_id", referencedColumnName = "id")
    private BankAccountInfo bankAccountInfo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "acct_sum_fee_id", referencedColumnName = "id")
    private AccountSummaryFee acctSumFee;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "subs_info_id", referencedColumnName = "id")
    private SubscriberInfo subsInfo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "prev_bill_id", referencedColumnName = "id")
    private PrevBill prevBill;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cur_bill_id", referencedColumnName = "id")
    private CurBill curBill;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cust_charge_id", referencedColumnName = "id")
    private CustCharge custCharge;

    @OneToMany(mappedBy = "accountInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SumFee> sumFees;

    @OneToMany(mappedBy = "accountInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RatingTax> ratingTaxs;

    @OneToMany(mappedBy = "accountInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeeCategory> feeCategories;

    @OneToMany(mappedBy = "accountInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CdrInfo> cdrInfos;

    // Getters and Setters
}
