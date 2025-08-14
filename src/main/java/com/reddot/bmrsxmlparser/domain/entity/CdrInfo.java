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

    @ManyToOne
    @JoinColumn(name = "bill_run_id", referencedColumnName = "id")
    private BillRun billRun;

    @ManyToOne
    @JoinColumn(name = "account_info_id", referencedColumnName = "id")
    private AccountInfo accountInfo;

    @ManyToOne
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

}

