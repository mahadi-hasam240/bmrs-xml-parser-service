package com.reddot.bmrsxmlparser.repository;

import com.reddot.bmrsxmlparser.domain.entity.AccountInfo;
import com.reddot.bmrsxmlparser.domain.entity.BillCycle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BillCycleRepository extends JpaRepository<BillCycle, Long> {
    Optional<BillCycle> findByBillCycleId(String billCycleId);
}
