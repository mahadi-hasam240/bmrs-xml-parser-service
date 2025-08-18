package com.reddot.bmrsxmlparser.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Entity
@Table(name = "sum_fee")
@SequenceGenerator(name = "sum_fee_seq", sequenceName = "sum_fee_id_seq", allocationSize = 1)
@Data
@EqualsAndHashCode(callSuper = true)
public class SumFee extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sum_fee_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "voice_flag") private String voiceFlag;
    @Column(name = "description") private String desc; // Renamed to avoid keyword
    @Column(name = "number_val") private String number; // Renamed to avoid keyword
    @Column(name = "duration") private String duration;
    @Column(name = "charge_fee") private String chgFee; // Assuming String based on DTO
    @Column(name = "currency") private String currency;
    // Add other fields as per your <SUMFEE> XML structure

    @ManyToOne
    @JoinColumn(name = "account_info_id")
    private AccountInfo accountInfo;
}