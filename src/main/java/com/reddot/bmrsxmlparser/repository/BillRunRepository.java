package com.reddot.bmrsxmlparser.repository;

import com.reddot.bmrsxmlparser.domain.entity.BillCycle;
import com.reddot.bmrsxmlparser.domain.entity.BillRun;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRunRepository extends JpaRepository<BillRun, Long> {
}
