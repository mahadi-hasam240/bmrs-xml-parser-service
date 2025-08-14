package com.reddot.bmrsxmlparser.repository;

import com.reddot.bmrsxmlparser.domain.entity.BillCycle;
import com.reddot.bmrsxmlparser.domain.entity.BillInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillInfoRepository extends JpaRepository<BillInfo, Long> {
}
