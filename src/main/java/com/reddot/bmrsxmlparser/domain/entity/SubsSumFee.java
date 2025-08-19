package com.reddot.bmrsxmlparser.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "subs_sum_fee")
@SequenceGenerator(name = "subs_sum_fee_seq", sequenceName = "subs_sum_fee_id_seq", allocationSize = 1)
@Data
public class SubsSumFee extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subs_sum_fee_seq")
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subscriber_info_id", referencedColumnName = "id")
    private SubscriberInfo subscriberInfo;

    @Column(name = "voice_flag")
    private String voiceFlag;

    @Column(name = "roam_flag")
    private String roamFlag;

    @Column(name = "description")
    private String desc;

    @Column(name = "number_val")
    private String number;

    @Column(name = "duration")
    private String duration;

    @Column(name = "chg_fee")
    private BigDecimal chgFee;

    @Column(name = "currency")
    private String currency;

    @Column(name = "pay_flag")
    private String payFlag;

    @Column(name = "billitem_code")
    private String billItemCode;
}

