package com.reddot.bmrsxmlparser.repository;

import com.reddot.bmrsxmlparser.domain.entity.AccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountInfoRepository extends JpaRepository<AccountInfo, Long> {
}
