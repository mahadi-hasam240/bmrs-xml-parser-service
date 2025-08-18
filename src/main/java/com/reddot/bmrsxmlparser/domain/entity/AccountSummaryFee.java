package com.reddot.bmrsxmlparser.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "account_summary_fee")
@SequenceGenerator(name = "account_summary_fee_seq", sequenceName = "account_summary_fee_id_seq", allocationSize = 1)
@Data
public class AccountSummaryFee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_summary_fee_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "account_summary_fee_id")
    private Long accountSummaryFeeId;

    @Column(name = "voice_flag")
    private String voiceFlag;

    @Column(name = "desc")
    private String desc;

    @Column(name = "number")
    private String number;

    @Column(name = "duration")
    private String duration;

    @Column(name = "chg_fee")
    private String chgFee;

    @Column(name = "currency")
    private String currency;

}

