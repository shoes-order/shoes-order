package com.personal.domain.outputhistory.service;

import com.personal.common.code.ResponseCode;
import com.personal.common.entity.AuthUser;
import com.personal.domain.inputhistory.exception.IllegalLotException;
import com.personal.domain.outputhistory.dto.OutputRequest;
import com.personal.domain.outputhistory.dto.OutputResponse;
import com.personal.domain.outputhistory.repository.OutputRepository;
import com.personal.domain.store.service.StoreCommonService;
import com.personal.entity.history.OutputHistory;
import com.personal.entity.store.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OutputService {
    private final OutputRepository outPutRepository;
    private final StoreCommonService storeCommonService;

    @Transactional
    public void createOutPut(Long storeId, AuthUser authUser, OutputRequest.CreateOutPut createOutPut) {
        // TODO : parameter 정리 및 불필요한 필수값은 엔티티에서 수정 할것 (Column 'description' cannot be null 에러 등등)
        Store store = storeCommonService.getStores(storeId);
        storeCommonService.validateUserAccess(authUser, storeId);
        String newLot = "lot-" + createOutPut.lot();
        if (outPutRepository.existsByLot(newLot)) {
            throw new IllegalLotException(ResponseCode.ILLEGAL_LOT);
        }
        OutputHistory outputHistory = OutputHistory.builder()
                .productId(createOutPut.productId())
                .type(createOutPut.type())
                .name(createOutPut.name())
                .size(createOutPut.size())
                .qty(createOutPut.qty())
                .price(createOutPut.price())
                .lot(newLot)
                .outputDate(createOutPut.outputDate())
                .store(store)
                .build();
        outPutRepository.save(outputHistory);

    }

    public Page<OutputResponse.GetInfos> getOutputs(Long storeId, AuthUser authUser, OutputRequest.GetOutputs getOutputs) {
        Pageable pageable = PageRequest.of(getOutputs.page() - 1, getOutputs.size());
        storeCommonService.getStores(storeId);
        storeCommonService.validateUserAccess(authUser, storeId);

        return outPutRepository.getOutputs(pageable,storeId,getOutputs);

    }
}
