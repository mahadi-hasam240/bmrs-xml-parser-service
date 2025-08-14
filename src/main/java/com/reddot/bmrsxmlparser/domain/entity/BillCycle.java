package com.reddot.bmrsxmlparser.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bill_cycle")
@SequenceGenerator(name = "bill_cycle_seq", sequenceName = "bill_cycle_id_seq", allocationSize = 1)
@Data
public class BillCycle extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bill_cycle_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "cycle_begin")
    private LocalDateTime cycleBegin;

    @Column(name = "cycle_end")
    private LocalDateTime cycleEnd;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @OneToMany(mappedBy = "billCycleInfo")
    private List<BillInfo> billInfos;

    // Getters and Setters
}
