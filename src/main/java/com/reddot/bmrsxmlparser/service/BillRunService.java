package com.reddot.bmrsxmlparser.service;

import com.reddot.bmrsxmlparser.domain.dto.BillInfoDTO;
import com.reddot.bmrsxmlparser.domain.entity.BillCycle;
import com.reddot.bmrsxmlparser.domain.entity.BillRun;
import com.reddot.bmrsxmlparser.mapper.DtoToEntityMapper;
import com.reddot.bmrsxmlparser.repository.BillCycleRepository;
import com.reddot.bmrsxmlparser.repository.BillRunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BillRunService {
    private final BillRunRepository billRunRepository;

    public BillRun save(BillRun billRun) {
        return billRunRepository.save(billRun);
    }
}
