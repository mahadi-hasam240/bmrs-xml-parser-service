package com.reddot.bmrsxmlparser.repository;

import com.reddot.bmrsxmlparser.domain.entity.AccountInfo;
import com.reddot.bmrsxmlparser.domain.entity.AccountSummaryFee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountSummaryFeeRepository extends JpaRepository<AccountSummaryFee, Long> {
}
