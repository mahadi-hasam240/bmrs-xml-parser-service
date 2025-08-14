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

    @Column(name = "invoice_id")
    private String invoiceId;

    @Column(name = "invoice_date")
    private LocalDateTime invoiceDate;

    @Column(name = "invoice_no")
    private String invoiceNo;

    @Column(name = "bar_code_number")
    private String barCodeNumber;

    @ManyToOne
    @JoinColumn(name = "bill_cycle_id", referencedColumnName = "id")
    private BillCycle billCycleInfo;

    @OneToMany(mappedBy = "billRun")
    private List<BillInfo> billInfos;

    @OneToMany(mappedBy = "billRun")
    private List<FeeCategory> feeCategories;

    @OneToMany(mappedBy = "billRun")
    private List<CdrInfo> cdrInfos;

    // Getters and Setters
}

