package com.reddot.bmrsxmlparser.service;

import com.reddot.bmrsxmlparser.domain.dto.BillInfoDTO;
import com.reddot.bmrsxmlparser.domain.entity.CdrInfo;
import com.reddot.bmrsxmlparser.repository.CdrInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CdrInfoService {
    private final CdrInfoRepository cdrInfoRepository;

    public CdrInfo processCdrInfo(BillInfoDTO billInfoDTO) {
        CdrInfo cdrInfo = new CdrInfo();
//        cdrInfo.setCallFlow(billInfoDTO.getCdrInfo().getCallFlow());
        // Map other fields...
        return cdrInfoRepository.save(cdrInfo);
    }
}
