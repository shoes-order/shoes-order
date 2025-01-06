package com.personal.domain.inputhistory.service;

import com.personal.common.code.ResponseCode;
import com.personal.common.entity.AuthUser;
import com.personal.domain.inputhistory.dto.InputHistoryResponse;
import com.personal.domain.inputhistory.dto.InputhistoryRequest;
import com.personal.domain.inputhistory.exception.IllegalLotException;
import com.personal.domain.inputhistory.repository.InputHistoryRepository;
import com.personal.domain.product.service.ProductCommonService;
import com.personal.domain.stock.service.StockService;
import com.personal.domain.store.service.StoreCommonService;
import com.personal.entity.history.InputHistory;
import com.personal.entity.product.Product;
import com.personal.entity.store.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InputHistoryService {
    private final InputHistoryRepository inputHistoryRepository;
    private final StoreCommonService storeCommonService;
    private final StockService stockService;
    private final ProductCommonService productCommonService;

    @Transactional
    public void createInput(Long storeId, InputhistoryRequest.CreateInput createInput, AuthUser authUser) {

        Store store = storeCommonService.getStores(storeId);
        storeCommonService.validateUserAccess(authUser, storeId);

        String newLot = "lot-" + UUID.randomUUID();
        //lot가 유니크한 값인지 검증
        if (inputHistoryRepository.existsByLot(newLot)) {
            throw new IllegalLotException(ResponseCode.ILLEGAL_LOT);
        }
        Product product = productCommonService.getStoreProduct(storeId, createInput.productId());
        InputHistory inputHistory = InputHistory.builder()
                .productId(product.getId())
                .type(product.getType())
                .name(createInput.name())
                .size(createInput.size())
                .qty(createInput.qty())
                .price(createInput.price())
                .lot(newLot)
                .inputDate(createInput.inputDate())
                .description(createInput.description())
                .store(store)
                .build();
        inputHistoryRepository.save(inputHistory);

        //입고에 insert될때 재고 insert
        stockService.addStock(inputHistory, storeId);

    }


    public Page<InputHistoryResponse.GetInfos> getInputHistories(Long storeId, InputhistoryRequest.GetInputHistories getInputHistories, AuthUser authUser) {
        storeCommonService.getStores(storeId);
        storeCommonService.validateUserAccess(authUser, storeId);
        Pageable pageable = PageRequest.of(getInputHistories.page() - 1, getInputHistories.size());

        return inputHistoryRepository.getInputHistories(storeId, getInputHistories, pageable);

    }
}
