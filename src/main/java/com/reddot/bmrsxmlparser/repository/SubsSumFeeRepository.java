package com.reddot.bmrsxmlparser.repository;

import com.reddot.bmrsxmlparser.domain.entity.BillCycle;
import com.reddot.bmrsxmlparser.domain.entity.SubsSumFee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubsSumFeeRepository extends JpaRepository<SubsSumFee, Long> {
}
