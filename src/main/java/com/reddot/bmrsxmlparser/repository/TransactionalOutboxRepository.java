package com.reddot.bmrsxmlparser.repository;

import com.reddot.bmrsxmlparser.domain.entity.TransactionalOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionalOutboxRepository extends JpaRepository<TransactionalOutbox, Long> {
}
