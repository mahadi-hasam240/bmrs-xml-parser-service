package com.reddot.bmrsxmlparser.repository;

import com.reddot.bmrsxmlparser.domain.entity.BillCycle;
import com.reddot.bmrsxmlparser.domain.entity.DetailChargeContainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailChargeContainerRepository extends JpaRepository<DetailChargeContainer, Long> {
}
