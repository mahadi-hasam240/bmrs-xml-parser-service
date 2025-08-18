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

    // Fields from BILL_PROP
    @Column(name = "invoice_id")
    private String invoiceId;

    @Column(name = "invoice_date")
    private LocalDateTime invoiceDate;

    @Column(name = "invoice_no")
    private String invoiceNo;

    @Column(name = "bar_code_number")
    private String barCodeNumber;

    @Column(name = "statement_date")
    private LocalDateTime statementDate; // From BillProp

    @Column(name = "bill_period")
    private String billPeriod; // From BillProp

    @Column(name = "from_date")
    private LocalDateTime fromDate; // From BillProp

    @Column(name = "to_date")
    private LocalDateTime toDate; // From BillProp

    @Column(name = "due_date_bill_prop") // Renamed to differentiate from BillCycle's due_date
    private LocalDateTime dueDate; // From BillProp

    @Column(name = "lang")
    private String lang; // From BillProp

    @Column(name = "currency")
    private String currency; // From BillProp

    @Column(name = "bill_medium")
    private String billMedium; // From BillProp

    @Column(name = "fee_precision")
    private String feePrecision; // From BillProp

    @Column(name = "bill_insert")
    private String billInsert; // From BillProp

    @Column(name = "bill_type")
    private String billType; // From BillProp

    @Column(name = "mobile_number")
    private String mobileNumber; // From BillProp

    @Column(name = "end_date")
    private LocalDateTime endDate; // From BillProp

    @Column(name = "main_offering_name")
    private String mainOfferingName; // From BillProp


    // Relationships
    @ManyToOne(cascade = CascadeType.ALL) // Consider cascading saves for BillCycle
    @JoinColumn(name = "bill_cycle_id", referencedColumnName = "id")
    private BillCycle billCycleInfo; // This is the BillCycleInfo from the root BILL_INFO

    @ManyToOne(cascade = CascadeType.ALL) // Relationship to AccountInfo
    @JoinColumn(name = "account_info_id", referencedColumnName = "id")
    private AccountInfo accountInfo;

    @ManyToOne(cascade = CascadeType.ALL) // Relationship to CustomInfo
    @JoinColumn(name = "custom_info_id", referencedColumnName = "id")
    private CustomInfo customInfo;

//    @ManyToOne(cascade = CascadeType.ALL) // NEW: Relationship to MktMsg
//    @JoinColumn(name = "mkt_msg_id", referencedColumnName = "id")
//    private MktMsg mktMsg;

    // OneToMany relationships (inverse side of the relationship)
    @OneToMany(mappedBy = "billRun", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillInfo> billInfos;

    @OneToMany(mappedBy = "billRun", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeeCategory> feeCategories;

    @OneToMany(mappedBy = "billRun", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CdrInfo> cdrInfos;
}

