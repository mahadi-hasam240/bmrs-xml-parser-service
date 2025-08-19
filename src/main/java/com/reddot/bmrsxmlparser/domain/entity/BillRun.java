package com.reddot.bmrsxmlparser.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bill_run")
@SequenceGenerator(name = "bill_run_seq", sequenceName = "bill_run_id_seq", allocationSize = 1)
@Data
public class BillRun extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bill_run_seq")
    @Column(name = "id")
    private Long id;
    // Fields from BILL_PROP (Mapped directly here)
    @Column(name = "invoice_id")
    private String invoiceId;

    @Column(name = "invoice_date")
    private LocalDateTime invoiceDate;

    @Column(name = "invoice_no")
    private String invoiceNo;

    @Column(name = "bar_code_number")
    private String barCodeNumber;

    @Column(name = "statement_date")
    private LocalDateTime statementDate;

    @Column(name = "bill_period")
    private String billPeriod;

    @Column(name = "from_date")
    private LocalDateTime fromDate;

    @Column(name = "to_date")
    private LocalDateTime toDate;

    @Column(name = "due_date_bill_prop")
    private LocalDateTime dueDate;

    @Column(name = "lang")
    private String lang;

    @Column(name = "currency")
    private String currency;

    @Column(name = "bill_medium")
    private String billMedium;

    @Column(name = "fee_precision")
    private String feePrecision;

    @Column(name = "bill_insert")
    private String billInsert;

    @Column(name = "bill_type")
    private String billType;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "main_offering_name")
    private String mainOfferingName;

    // Relationships (Ensure CascadeType.ALL on owning sides)
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bill_cycle_id", referencedColumnName = "id")
    private BillCycle billCycleInfo;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_info_id", referencedColumnName = "id")
    private AccountInfo accountInfo;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "custom_info_id", referencedColumnName = "id")
    private CustomInfo customInfo;

    // OneToMany relationships (inverse side, mappedBy is on the child entity)
    // These should have cascade = CascadeType.ALL and orphanRemoval = true in the respective child entities.
    @OneToMany(mappedBy = "billRun", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeeCategory> feeCategories;

    @OneToMany(mappedBy = "billRun", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CdrInfo> cdrInfos;
}

