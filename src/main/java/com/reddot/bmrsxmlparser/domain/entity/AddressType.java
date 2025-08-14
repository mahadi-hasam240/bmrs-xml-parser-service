package com.reddot.bmrsxmlparser.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "address_type")
@Data
@SequenceGenerator(name = "address_type_seq", sequenceName = "address_type_id_seq", allocationSize = 1)
public class AddressType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_type_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "addr_type")
    private String addrType;

    @Column(name = "addr1")
    private String addr1;

    @Column(name = "addr2")
    private String addr2;

    @Column(name = "addr3")
    private String addr3;

    @Column(name = "addr4")
    private String addr4;

    @Column(name = "addr5")
    private String addr5;

    @Column(name = "addr6")
    private String addr6;

    @Column(name = "zipcode")
    private String zipcode;

    @Column(name = "emailaddr")
    private String emailAddr;

    @Column(name = "smsaddr")
    private String smsAddr;

    // Getters and Setters
}

