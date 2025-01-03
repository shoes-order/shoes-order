package com.personal.domain.stock.service;

import com.personal.common.entity.AuthUser;
import com.personal.domain.product.service.ProductCommonService;
import com.personal.domain.stock.dto.StockRequest;
import com.personal.domain.stock.dto.StockResponse;
import com.personal.domain.stock.repository.StockRepository;
import com.personal.domain.store.service.StoreCommonService;
import com.personal.entity.history.InputHistory;
import com.personal.entity.product.Product;
import com.personal.entity.stock.Stock;
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
public class StockService {
    private final StockRepository stockRepository;
    private final StoreCommonService storeCommonService;
    private final ProductCommonService productCommonService;

    public Page<StockResponse.Infos> getStocks(Long storeId, StockRequest.GetStocks getStocks, AuthUser authUser) {
        Pageable pageable = PageRequest.of(getStocks.page() - 1, getStocks.size());

        Store store = storeCommonService.getStores(storeId);
        storeCommonService.validateUserAccess(authUser, storeId);

        return stockRepository.getStocks(pageable, storeId, getStocks);

    }

    @Transactional
    public void addStock(InputHistory inputHistory, Long storeId) {
        Store store = storeCommonService.getStores(storeId);
        Product product = productCommonService.getProducts(inputHistory.getProductId());

        Stock stock = Stock.builder()
                .type(inputHistory.getType())
                .size(inputHistory.getSize())
                .qty(inputHistory.getQty())
                .price(inputHistory.getPrice())
                .lot(inputHistory.getLot())
                .description(inputHistory.getDescription())
                .store(store)
                .product(product)
                .build();
        stockRepository.save(stock);

    }
}
