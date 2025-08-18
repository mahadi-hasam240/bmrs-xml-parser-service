package com.reddot.bmrsxmlparser.service;

import com.reddot.bmrsxmlparser.domain.dto.BillInfoDTO;
import com.reddot.bmrsxmlparser.domain.entity.FeeCategory;
import com.reddot.bmrsxmlparser.repository.FeeCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeeCategoryService {
    private final FeeCategoryRepository feeCategoryRepository;

    public FeeCategory processFeeCategory(BillInfoDTO billInfoDTO) {
        FeeCategory feeCategory = new FeeCategory();
//        feeCategory.setPayAcctCode(billInfoDTO.getFeeCategory().getPayAcctCode());
        // Map other fields...
        return feeCategoryRepository.save(feeCategory);
    }
}
