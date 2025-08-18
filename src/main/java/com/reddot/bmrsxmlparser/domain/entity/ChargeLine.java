package com.reddot.bmrsxmlparser.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "charge_line")
@SequenceGenerator(name = "charge_line_seq", sequenceName = "charge_line_id_seq", allocationSize = 1)
@Data
public class ChargeLine extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "charge_line_seq")
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL) // Consider cascade for relationship management
    @JoinColumn(name = "fee_category_id", referencedColumnName = "id")
    private FeeCategory feeCategory;

    @Column(name = "chg_fee")
    private BigDecimal chgFee;

    @Column(name = "description") // Changed name to avoid SQL keyword conflict
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

    // --- MISSING FIELDS ADDED BELOW ---

    @Column(name = "rating_tax1")
    private BigDecimal ratingTax1;

    @Column(name = "rating_tax2")
    private BigDecimal ratingTax2;

    @Column(name = "rating_tax3")
    private BigDecimal ratingTax3;

    @Column(name = "rating_tax4")
    private BigDecimal ratingTax4;

    @Column(name = "disc1")
    private BigDecimal disc1;

    @Column(name = "disc2")
    private BigDecimal disc2;

    @Column(name = "disc3")
    private BigDecimal disc3;

    @Column(name = "start_date")
    private LocalDateTime startDate; // Changed to LocalDateTime, assuming parsing will happen in mapper

    @Column(name = "end_date")
    private LocalDateTime endDate;   // Changed to LocalDateTime, assuming parsing will happen in mapper

    @Column(name = "currency")
    private String currency;

}

