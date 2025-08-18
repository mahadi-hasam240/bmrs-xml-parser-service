package com.reddot.bmrsxmlparser.service;

import com.reddot.bmrsxmlparser.domain.entity.ChargeLine;
import com.reddot.bmrsxmlparser.repository.ChargeLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChargeLineService {
    private final ChargeLineRepository chargeLineRepository;

    public ChargeLine save(ChargeLine chargeLine){
        return chargeLineRepository.save(chargeLine);
    }
}
