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

    public BillInfo processBillInfo(BillInfoDTO billInfoDTO) {
        // Convert DTO to Entity
        BillInfo billInfo = new BillInfo();
        billInfo.setXmlInfo(billInfoDTO.getXmlInfo());
        billInfo.setOperInfo(billInfoDTO.getOperInfo());
        billInfo.setBillCycleInfo(DtoToEntityMapper.mapBillCycleInfoDTOToEntity(billInfoDTO.getBillCycleInfo()));
        billInfo.setBillRun(DtoToEntityMapper.mapBillRunDTOToEntity(billInfoDTO.getBillRun()));

        // Other business logic (e.g., validation, transformations, etc.)

        // Return the processed entity
        return billInfoRepository.save(billInfo);
    }
}

