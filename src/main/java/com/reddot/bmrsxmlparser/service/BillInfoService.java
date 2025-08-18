package com.reddot.bmrsxmlparser.service;

import com.reddot.bmrsxmlparser.domain.dto.BillInfoDTO;
import com.reddot.bmrsxmlparser.domain.entity.BillInfo;
import com.reddot.bmrsxmlparser.mapper.DtoToEntityMapper;
import com.reddot.bmrsxmlparser.repository.BillInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BillInfoService {

    private final BillInfoRepository billInfoRepository;

    public BillInfo save(BillInfo billInfo) {
        return billInfoRepository.save(billInfo);
    }

}

