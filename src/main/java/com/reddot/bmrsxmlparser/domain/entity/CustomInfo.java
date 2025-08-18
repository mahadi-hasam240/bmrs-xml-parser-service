package com.reddot.bmrsxmlparser.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "custom_info")
@SequenceGenerator(name = "custom_info_seq", sequenceName = "custom_info_id_seq", allocationSize = 1)
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "custom_info_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "cust_gender")
    private String custGender;
    @Column(name = "cust_type")
    private String custType;
    @Column(name = "cust_level")
    private String custLevel;
    @Column(name = "cust_id_xml")
    private String custId; // Renamed to avoid conflict with entity ID
    @Column(name = "vat_number")
    private String vatNumber;
    @Column(name = "cust_code")
    private String custCode;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cust_name_id", referencedColumnName = "id")
    private Name custName;
}
