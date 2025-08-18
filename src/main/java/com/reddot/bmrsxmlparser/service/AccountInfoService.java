package com.reddot.bmrsxmlparser.service;

import com.reddot.bmrsxmlparser.domain.dto.BillInfoDTO;
import com.reddot.bmrsxmlparser.domain.entity.AccountInfo;
import com.reddot.bmrsxmlparser.repository.AccountInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountInfoService {
    private final AccountInfoRepository accountInfoRepository;

    public AccountInfo processAccountInfo(BillInfoDTO billInfoDTO) {
        AccountInfo accountInfo = new AccountInfo();
//        accountInfo.setAcctId(billInfoDTO.getAccountInfo().getAcctId());
//        accountInfo.setAcctCode(billInfoDTO.getAccountInfo().getAcctCode());
//        accountInfo.setAcctName(billInfoDTO.getAccountInfo().getAcctName());
        // other fields mapping...

        return accountInfoRepository.save(accountInfo);
    }
}
