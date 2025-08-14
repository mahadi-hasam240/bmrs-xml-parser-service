package com.reddot.bmrsxmlparser.repository;

import com.reddot.bmrsxmlparser.domain.entity.BillCycle;
import com.reddot.bmrsxmlparser.domain.entity.SubscriberInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriberInfoRepository extends JpaRepository<SubscriberInfo, Long> {
}
