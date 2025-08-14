package com.reddot.bmrsxmlparser.repository;

import com.reddot.bmrsxmlparser.domain.entity.AccountInfo;
import com.reddot.bmrsxmlparser.domain.entity.BankAccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountInfoRepository extends JpaRepository<BankAccountInfo, Long> {
}
