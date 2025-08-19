package com.reddot.bmrsxmlparser.mapper;

import com.reddot.bmrsxmlparser.domain.dto.BillInfoDTO;
import com.reddot.bmrsxmlparser.domain.entity.BillCycle;
import com.reddot.bmrsxmlparser.domain.entity.BillInfo;
import com.reddot.bmrsxmlparser.domain.entity.BillRun;
import com.reddot.commonservice.utils.DateTimeUtils;
import org.springframework.stereotype.Service;

@Service
public class DtoToEntityMapper {


    // Method to map BillInfoDTO to BillInfo Entity
    public static BillInfo mapBillInfoDTOToEntity(BillInfoDTO billInfoDTO) {
        BillInfo billInfo = new BillInfo();
        billInfo.setXmlInfo(billInfoDTO.getXmlInfo());
        billInfo.setOperInfo(billInfoDTO.getOperInfo());

        // Mapping BillCycleInfoDTO to BillCycle entity
        BillCycle billCycle = mapBillCycleInfoDTOToEntity(billInfoDTO.getBillCycleInfo());
        billInfo.setBillCycleInfo(billCycle);

        // Mapping BillRunDTO to BillRun entity
        BillRun billRun = mapBillRunDTOToEntity(billInfoDTO.getBillRun());
        billInfo.setBillRun(billRun);

        return billInfo;
    }

    // Method to map BillCycleInfoDTO to BillCycle Entity
    public static BillCycle mapBillCycleInfoDTOToEntity(BillInfoDTO.BillCycleInfoDTO billCycleInfoDTO) {
        BillCycle billCycle = new BillCycle();
        billCycle.setId(Long.parseLong(billCycleInfoDTO.getBillCycleId()));
        billCycle.setCycleBegin(DateTimeUtils.convertToLocalDateTime(billCycleInfoDTO.getBillCycleBegin(), DateTimeUtils.TRANSACTION_DATE_FORMAT));
        billCycle.setCycleEnd(DateTimeUtils.convertToLocalDateTime(billCycleInfoDTO.getBillCycleEnd(), DateTimeUtils.TRANSACTION_DATE_FORMAT));
        billCycle.setDueDate(DateTimeUtils.convertToLocalDateTime(billCycleInfoDTO.getDueDate(), DateTimeUtils.APP_DATE_FORMAT));

        return billCycle;
    }

    // Method to map BillRunDTO to BillRun Entity
    public static BillRun mapBillRunDTOToEntity(BillInfoDTO.BillRunDTO billRunDTO) {
        BillRun billRun = new BillRun();
        billRun.setInvoiceId(billRunDTO.getBillProp().getInvoiceId());
        billRun.setInvoiceDate(DateTimeUtils.convertToLocalDateTime(billRunDTO.getBillProp().getInvoiceDate(), DateTimeUtils.TRANSACTION_DATE_FORMAT));
        billRun.setInvoiceNo(billRunDTO.getBillProp().getInvoiceNo());
        // Map other fields from BillRunDTO
        return billRun;
    }
}
