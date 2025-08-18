package com.reddot.bmrsxmlparser.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Entity
@Table(name = "cur_bill")
@SequenceGenerator(name = "cur_bill_seq", sequenceName = "cur_bill_id_seq", allocationSize = 1)
@Data
@EqualsAndHashCode(callSuper = true)
public class CurBill extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cur_bill_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "current_charges") private BigDecimal currentCharges;
    // Add other fields from your <CUR_BILL> XML here
    // Example: @Column(name = "total_amount") private BigDecimal totalAmount;

    @OneToOne(mappedBy = "curBill")
    private AccountInfo accountInfo;
}