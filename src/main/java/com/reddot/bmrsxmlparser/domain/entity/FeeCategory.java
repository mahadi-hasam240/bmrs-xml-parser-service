package com.reddot.bmrsxmlparser.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "fee_category")
@SequenceGenerator(name = "fee_category_seq", sequenceName = "fee_category_id_seq", allocationSize = 1)
@Data
public class FeeCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fee_category_seq")
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

    @Column(name = "pay_acct_code")
    private String payAcctCode;

    @Column(name = "free_unit")
    private String freeUnit; // Confirm if this is always null or has a value in XML for FeeCategory

    // --- NEW: List of ChargeLine entities ---
    @OneToMany(mappedBy = "feeCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChargeLine> detailCharges;


    // Getters and Setters
}

