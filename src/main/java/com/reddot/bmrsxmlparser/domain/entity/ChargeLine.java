package com.reddot.bmrsxmlparser.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "charge_line")
@SequenceGenerator(name = "charge_line_seq", sequenceName = "charge_line_id_seq", allocationSize = 1)
@Data
public class ChargeLine extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "charge_line_seq")
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fee_category_id", referencedColumnName = "id")
    private FeeCategory feeCategory;

    @Column(name = "chg_fee")
    private BigDecimal chgFee;

    @Column(name = "desc")
    private String desc;

    @Column(name = "category")
    private String category;

    @Column(name = "sub_category")
    private String subCategory;

    @Column(name = "cycle_no")
    private String cycleNo;

    @Column(name = "service_type")
    private String serviceType;

    @Column(name = "charge_code")
    private String chargeCode;

    @Column(name = "item_id")
    private String itemId;

}

