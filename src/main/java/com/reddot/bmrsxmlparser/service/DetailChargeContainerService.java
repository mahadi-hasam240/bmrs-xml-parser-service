package com.reddot.bmrsxmlparser.service;

import com.reddot.bmrsxmlparser.domain.entity.DetailChargeContainer;
import com.reddot.bmrsxmlparser.repository.DetailChargeContainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DetailChargeContainerService {
    private final DetailChargeContainerRepository detailChargeContainerRepository;

    public DetailChargeContainer save(DetailChargeContainer detailChargeContainer){
        return detailChargeContainerRepository.save(detailChargeContainer);
    }
}
