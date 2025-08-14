package com.reddot.bmrsxmlparser.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
@SequenceGenerator(name = "audit_log_seq", sequenceName = "audit_log_id_seq", allocationSize = 1)
@Data
public class AuditLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audit_log_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "xml_file_name")
    private String xmlFileName;

    @Column(name = "status")
    private String status;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "error_message")
    private String errorMessage;

}

