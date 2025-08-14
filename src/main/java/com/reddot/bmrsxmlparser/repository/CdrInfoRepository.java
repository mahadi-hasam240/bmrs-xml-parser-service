package com.reddot.bmrsxmlparser.repository;

import com.reddot.bmrsxmlparser.domain.entity.BillCycle;
import com.reddot.bmrsxmlparser.domain.entity.CdrInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CdrInfoRepository extends JpaRepository<CdrInfo, Long> {
}
