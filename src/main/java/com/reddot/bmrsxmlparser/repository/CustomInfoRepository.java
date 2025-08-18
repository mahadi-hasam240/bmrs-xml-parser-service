package com.reddot.bmrsxmlparser.repository;

import com.reddot.bmrsxmlparser.domain.entity.CustomInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomInfoRepository extends JpaRepository<CustomInfo, Long> {}