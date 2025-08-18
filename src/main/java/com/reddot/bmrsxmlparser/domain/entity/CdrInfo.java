package com.reddot.bmrsxmlparser.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cdr_info")
@SequenceGenerator(name = "cdr_info_seq", sequenceName = "cdr_info_id_seq", allocationSize = 1)
@Data
public class CdrInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cdr_info_seq")
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL) // Consider cascade for relationship management
    @JoinColumn(name = "bill_run_id", referencedColumnName = "id")
    private BillRun billRun;

    @ManyToOne(cascade = CascadeType.ALL) // Consider cascade for relationship management
    @JoinColumn(name = "account_info_id", referencedColumnName = "id")
    private AccountInfo accountInfo;

    @ManyToOne(cascade = CascadeType.ALL) // Consider cascade for relationship management
    @JoinColumn(name = "detail_charge_container_id", referencedColumnName = "id")
    private DetailChargeContainer detailChargeContainer;

    @Column(name = "call_flow")
    private String callFlow;

    @Column(name = "other_num")
    private String otherNum;

    @Column(name = "call_mask")
    private String callMask;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "chg_amt")
    private BigDecimal chgAmt;

    // --- MISSING FIELDS ADDED BELOW ---

    @Column(name = "tax_amt1")
    private BigDecimal taxAmt1;

    @Column(name = "tax_amt2")
    private BigDecimal taxAmt2;

    @Column(name = "tax_amt3")
    private BigDecimal taxAmt3;

    @Column(name = "volume")
    private String volume; // Assuming String based on your DTO/XML, adjust if it's numeric

    @Column(name = "service_type")
    private String serviceType;

    @Column(name = "start_time")
    private LocalDateTime startTime; // Assuming you'll convert to LocalDateTime

    @Column(name = "end_time")
    private LocalDateTime endTime; // Assuming you'll convert to LocalDateTime

    @Column(name = "duration")
    private String duration; // Stored as string like "00:00:10"

    @Column(name = "result_reason")
    private String resultReason;

    @Column(name = "service_flow")
    private String serviceFlow;

    @Column(name = "network")
    private String network;

    @Column(name = "country")
    private String country;

    @Column(name = "operator")
    private String operator;

    @Column(name = "gprs_flag")
    private String gprsFlag;

    @Column(name = "cf_num")
    private String cfNum;

}

