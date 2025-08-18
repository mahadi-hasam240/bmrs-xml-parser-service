package com.reddot.bmrsxmlparser.service;

import com.reddot.bmrsxmlparser.domain.dto.BillInfoDTO;
import com.reddot.bmrsxmlparser.domain.entity.BillCycle;
import com.reddot.bmrsxmlparser.domain.entity.BillInfo;
import com.reddot.bmrsxmlparser.mapper.DtoToEntityMapper;
import com.reddot.bmrsxmlparser.repository.BillCycleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BillCycleService {
    private final BillCycleRepository billCycleRepository;

    public BillCycle processBillCycle(BillInfoDTO billInfoDTO) {
        BillCycle billCycle = DtoToEntityMapper.mapBillCycleInfoDTOToEntity(billInfoDTO.getBillCycleInfo());
        return billCycleRepository.save(billCycle);
    }

    @Transactional // Ensures the save operation runs within a transaction
    public BillCycle save(BillCycle billCycle) {
        return billCycleRepository.save(billCycle); // Calling the save method from JpaRepository
    }
}
