package com.reddot.bmrsxmlparser.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Entity
@Table(name = "rating_tax")
@SequenceGenerator(name = "rating_tax_seq", sequenceName = "rating_tax_id_seq", allocationSize = 1)
@Data
@EqualsAndHashCode(callSuper = true)
public class RatingTax extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rating_tax_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "tax_amount") private BigDecimal taxAmount;
    // Add other fields as per your <RATING_TAX> XML structure
    // Example: @Column(name = "tax_type") private String taxType;

    @ManyToOne
    @JoinColumn(name = "account_info_id")
    private AccountInfo accountInfo;
}