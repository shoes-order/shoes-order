package com.personal.domain.stock.repository;

import com.personal.domain.stock.dto.StockRequest;
import com.personal.domain.stock.dto.StockResponse;
import com.personal.entity.product.ProductType;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.personal.entity.stock.QStock.stock;

@RequiredArgsConstructor
public class StockDslRepositoryImpl implements StockDslRepository {
    private final JPQLQueryFactory queryFactory;


    @Override
    public Page<StockResponse.Infos> getStocks(Pageable pageable, Long storeId, StockRequest.GetStocks getStocks) {
        List<StockResponse.Infos> results = queryFactory
                .select(Projections.constructor(StockResponse.Infos.class,
                        stock.type,
                        stock.size,
                        stock.qty,
                        stock.price,
                        stock.lot,
                        stock.description,
                        stock.createdAt,
                        stock.updatedAt
                ))
                .from(stock)
                .where(
                        stock.store.id.eq(storeId),
                        searchProducts(getStocks.type())
                )
                .orderBy(getStocks.sort().equals("ASC") ? stock.updatedAt.asc() : stock.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        //총 개수 계산
        Long totalCount = queryFactory
                .select(stock.count())
                .from(stock)
                .where(
                        stock.store.id.eq(storeId),
                        searchProducts(getStocks.type())
                )
                .fetchOne();

        return new PageImpl<>(results, pageable, totalCount);

    }

    private BooleanExpression searchProducts(ProductType type) {
        return type != null ? stock.type.eq(type) : null;
    }
}
