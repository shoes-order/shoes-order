package com.personal.domain.stock.repository;

import com.personal.domain.stock.dto.StockRequest;
import com.personal.domain.stock.dto.StockResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StockDslRepository {
    Page<StockResponse.Infos> getStocks(Pageable pageable, Long storeId, StockRequest.GetStocks getStocks);
}
