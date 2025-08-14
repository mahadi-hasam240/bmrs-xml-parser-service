package com.reddot.bmrsxmlparser.repository;

import com.reddot.bmrsxmlparser.domain.entity.BillCycle;
import com.reddot.bmrsxmlparser.domain.entity.ChargeLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargeLineRepository extends JpaRepository<ChargeLine, Long> {
}
