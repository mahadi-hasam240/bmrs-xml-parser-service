package com.reddot.bmrsxmlparser.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Entity
@Table(name = "cust_charge")
@SequenceGenerator(name = "cust_charge_seq", sequenceName = "cust_charge_id_seq", allocationSize = 1)
@Data
@EqualsAndHashCode(callSuper = true)
public class CustCharge extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cust_charge_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "category") private String category;
    // Add other fields from your <CUST_CHARGE> XML here
    // Example: @Column(name = "charge_type") private String chargeType;

    @OneToOne(mappedBy = "custCharge")
    private AccountInfo accountInfo;
}