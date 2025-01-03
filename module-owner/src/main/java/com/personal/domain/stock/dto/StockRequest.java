package com.personal.domain.stock.dto;

import com.personal.entity.product.ProductType;

import java.util.Objects;

public sealed interface StockRequest permits
        StockRequest.GetStocks {
    record GetStocks(
            Integer page,
            Integer size,
            String sort,
            ProductType type
    )
            implements StockRequest {
        public GetStocks {
            if (Objects.isNull(page)) page = 1;
            if (Objects.isNull(size)) size = 10;
        }
    }
}
