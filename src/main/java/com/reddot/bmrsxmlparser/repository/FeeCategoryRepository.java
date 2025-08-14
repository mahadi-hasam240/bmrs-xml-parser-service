package com.reddot.bmrsxmlparser.repository;

import com.reddot.bmrsxmlparser.domain.entity.BillCycle;
import com.reddot.bmrsxmlparser.domain.entity.FeeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeeCategoryRepository extends JpaRepository<FeeCategory, Long> {
}
