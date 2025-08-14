package com.reddot.bmrsxmlparser.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "subscriber_info")
@SequenceGenerator(name = "subscriber_info_seq", sequenceName = "subscriber_info_id_seq", allocationSize = 1)
@Data
public class SubscriberInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subscriber_info_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "msisdn")
    private String msisdn;

    @Column(name = "sub_id")
    private String subId;

    @Column(name = "acct_code")
    private String acctCode;

    @ManyToOne
    @JoinColumn(name = "sub_name_id", referencedColumnName = "id")
    private Name subName;

    @OneToMany(mappedBy = "subscriberInfo")
    private List<SubsSumFee> subsSumFees;

    @ManyToOne
    @JoinColumn(name = "detail_charge_container_id", referencedColumnName = "id")
    private DetailChargeContainer detailChargeContainer;

    // Getters and Setters
}

