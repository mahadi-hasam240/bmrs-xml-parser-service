package com.reddot.bmrsxmlparser.service;

import com.reddot.bmrsxmlparser.domain.entity.SubscriberInfo;
import com.reddot.bmrsxmlparser.repository.SubscriberInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriberInfoService {
    private final SubscriberInfoRepository subscriberInfoRepository;

    public SubscriberInfo save(SubscriberInfo subscriberInfo){
        return subscriberInfoRepository.save(subscriberInfo);
    }
}
