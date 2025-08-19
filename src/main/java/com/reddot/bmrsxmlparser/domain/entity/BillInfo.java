package com.reddot.bmrsxmlparser.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "bill_info")
@SequenceGenerator(name = "bill_info_seq", sequenceName = "bill_info_id_seq", allocationSize = 1)
@Data
public class BillInfo extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bill_info_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "xml_info")
    private String xmlInfo;

    @Column(name = "oper_info")
    private String operInfo;

    @ManyToOne(cascade = CascadeType.ALL) // Ensure BillCycle is cascaded
    @JoinColumn(name = "bill_cycle_id", referencedColumnName = "id")
    private BillCycle billCycleInfo;

    @ManyToOne(cascade = CascadeType.ALL) // <-- CRITICAL: Ensure this is present
    @JoinColumn(name = "bill_run_id", referencedColumnName = "id")
    private BillRun billRun;

}
