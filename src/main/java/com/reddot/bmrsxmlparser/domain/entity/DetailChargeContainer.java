package com.reddot.bmrsxmlparser.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "detail_charge_container")
@SequenceGenerator(name = "detail_charge_container_seq", sequenceName = "detail_charge_container_id_seq", allocationSize = 1)
@Data
public class DetailChargeContainer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detail_charge_container_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "free_unit") // Added from DTO/XML
    private String freeUnit;

    @OneToMany(mappedBy = "detailChargeContainer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeeCategory> feeCategories;

    @OneToMany(mappedBy = "detailChargeContainer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CdrInfo> cdrInfos; // Changed name from cdrInfoDTOS to cdrInfos to hold entities

    // Relationship back to SubscriberInfo (assuming it's nested under SubscriberInfo)
    @OneToOne(mappedBy = "detailChargeContainer") // If SubscriberInfo has a @OneToOne to this container
    private SubscriberInfo subscriberInfo;

    // Getters and Setters
}

