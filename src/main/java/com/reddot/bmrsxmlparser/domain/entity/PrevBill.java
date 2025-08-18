package com.reddot.bmrsxmlparser.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Entity
@Table(name = "prev_bill")
@SequenceGenerator(name = "prev_bill_seq", sequenceName = "prev_bill_id_seq", allocationSize = 1)
@Data
@EqualsAndHashCode(callSuper = true)
public class PrevBill extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prev_bill_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "prev_balance") private BigDecimal prevBalance;
    @Column(name = "balance_fwd") private BigDecimal balanceFwd;

    @OneToOne(mappedBy = "prevBill")
    private AccountInfo accountInfo;
}