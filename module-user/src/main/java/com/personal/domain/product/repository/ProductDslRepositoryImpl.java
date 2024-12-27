package com.personal.domain.product.repository;

import com.personal.domain.product.dto.ProductRequest;
import com.personal.domain.product.dto.ProductResponse;
import com.personal.entity.product.ProductType;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

import static com.personal.entity.product.QProduct.product;
import static com.personal.entity.store.QStore.store;

@Slf4j
@RequiredArgsConstructor
public class ProductDslRepositoryImpl implements ProductDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProductResponse.Info> getProducts(Long storeId, ProductRequest.GetProduct getProduct) {
        return queryFactory
                .select(Projections.constructor(ProductResponse.Info.class ,
                        product.id,
                        product.name,
                        product.category,
                        product.basePrice,
                        product.customPrice))
                .from(product)
                .innerJoin(store).on(store.id.eq(product.store.id))
                .where(
                        store.id.eq(storeId) ,
                        product.type.eq(ProductType.PRODUCT) ,
                        searchProducts(getProduct.type() , getProduct.value()) ,
                        searchPrice(getProduct.minPrice(), getProduct.maxPrice())
                )
                .orderBy(getProduct.sort().equals("ASC") ? product.id.asc() : product.id.desc())
                .fetch();
    }

    private BooleanExpression searchProducts(String type , String value) {
        return switch (type) {
            case "name" -> value != null ? product.name.contains(value) : null;
            case "category" -> value != null ? product.category.contains(value) : null;
            default -> null;
        };
    }

    private BooleanExpression searchPrice(Long minPrice , Long maxPrice) {
        BooleanExpression predicate = null;
        if (Objects.nonNull(minPrice) && Objects.nonNull(maxPrice)) {
            predicate = product.basePrice.between(minPrice, maxPrice);
        } else if (Objects.nonNull(minPrice)) {
            predicate = product.basePrice.goe(minPrice);
        } else if (Objects.nonNull(maxPrice)) {
            predicate = product.basePrice.loe(maxPrice);
        }
        return predicate;
    }
}
