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

    @Column(name = "acct_code")
    private String acctCode;

    @Column(name = "acct_number")
    private String acctNumber;

    @ManyToOne
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

    @Column(name = "credit_limit")
    private BigDecimal creditLimit;

    @Column(name = "pay_method")
    private String payMethod;

    @Column(name = "print_vatno")
    private String printVatNo;

    @OneToMany(mappedBy = "accountInfo")
    private List<FeeCategory> feeCategories;

    @OneToMany(mappedBy = "accountInfo")
    private List<CdrInfo> cdrInfos;

    // Getters and Setters
}
