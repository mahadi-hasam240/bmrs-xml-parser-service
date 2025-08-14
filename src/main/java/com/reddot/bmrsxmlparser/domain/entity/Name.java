package com.reddot.bmrsxmlparser.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "name")
@SequenceGenerator(name = "name_seq", sequenceName = "name_id_seq", allocationSize = 1)
@Data
public class Name {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "name_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "full_name")
    private String fullName;

}

