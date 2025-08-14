package com.reddot.bmrsxmlparser.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "bank_account_info")
@SequenceGenerator(name = "bank_account_info_seq", sequenceName = "bank_account_info_id_seq", allocationSize = 1)
@Data
public class BankAccountInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_account_info_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "bank_no")
    private String bankNo;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "card_expdate")
    private String cardExpDate;

    @Column(name = "bank_acctname")
    private String bankAcctName;

    @Column(name = "bank_accttype")
    private String bankAcctType;

}
