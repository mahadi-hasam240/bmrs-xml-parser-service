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

    @OneToMany(mappedBy = "detailChargeContainer")
    private List<FeeCategory> feeCategories;

    @OneToMany(mappedBy = "detailChargeContainer")
    private List<CdrInfo> cdrInfoDTOS;

    // Getters and Setters
}

