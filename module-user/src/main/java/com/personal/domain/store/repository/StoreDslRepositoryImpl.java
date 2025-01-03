package com.personal.domain.store.repository;

import com.personal.domain.store.dto.StoreRequest;
import com.personal.domain.store.dto.StoreResponse;
import com.personal.entity.product.ProductType;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.personal.entity.product.QProduct.product;
import static com.personal.entity.store.QStore.store;

@Slf4j
@RequiredArgsConstructor
public class StoreDslRepositoryImpl implements StoreDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<StoreResponse.Infos> getStores(StoreRequest.GetStores getStores, Pageable pageable) {

        JPQLQuery<Long> productCnt = JPAExpressions
                .select(product.count())
                .from(product)
                .where(product.store.id.eq(store.id),
                        product.type.eq(ProductType.PRODUCT),
                        product.isDeleted.eq(false),
                        product.isSold.eq(true));

        List<StoreResponse.Infos> results = queryFactory
                .select(Projections.constructor(StoreResponse.Infos.class ,
                        store.id ,
                        store.name ,
                        store.tel ,
                        store.zip ,
                        store.address ,
                        store.addressDetail ,
                        store.description ,
                        productCnt
                ))
                .from(store)
                .where(
                        searchStores(getStores.type() , getStores.value()) ,
                        store.isDeleted.eq(false)
                )
                .orderBy(getStores.sort().equals("ASC") ? store.updatedAt.asc() : store.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory
                .select(store.count())
                .from(store)
                .where(
                        searchStores(getStores.type() , getStores.value()) ,
                        store.isDeleted.eq(false)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchOne();;

        return new PageImpl<>(results, pageable, totalCount);
    }

    private BooleanExpression searchStores(String type , String value) {
        return switch (type) {
            case "name" -> value != null ? store.name.contains(value) : null;
            case "address" -> value != null ? store.address.contains(value) : null;
            default -> null;
        };
    }
}