package com.personal.domain.product.repository;

import com.personal.domain.product.dto.ProductRequest;
import com.personal.domain.product.dto.ProductResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.personal.entity.product.QProduct.product;

@RequiredArgsConstructor
public class ProductDslRepositoryImpl implements ProductDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProductResponse.Infos> getProducts(ProductRequest.GetProducts getProducts, Pageable pageable, Long storeId) {

        List<ProductResponse.Infos> results = queryFactory
                .select(Projections.constructor(ProductResponse.Infos.class,
                        product.id,
                        product.type,
                        product.name,
                        product.category,
                        product.material,
                        product.basePrice,
                        product.customPrice,
                        product.store.id
                ))
                .from(product)
                .where(
                        product.store.id.eq(storeId),
                        product.isDeleted.eq(false),
                        searchProduct(getProducts.name(), getProducts.category()),
                        applyIsSoldCondition(getProducts.isSold())
                )
                .orderBy(product.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        // 총 개수 계산
        Long totalCount = queryFactory
                .select(product.count())
                .from(product)
                .where(
                        product.store.id.eq(storeId),
                        product.isDeleted.eq(false),
                        searchProduct(getProducts.name(), getProducts.category()),
                        applyIsSoldCondition(getProducts.isSold())
                )
                .fetchOne();
        return new PageImpl<>(results, pageable, totalCount);
    }

    private BooleanExpression searchProduct(String name, String category) {
        BooleanExpression condition = null;
        if (name != null) {
            condition = product.name.contains(name);
        }
        if (category != null) {
            condition = condition != null ?
                    condition.and(product.category.contains(category)) : product.category.contains(category);
        }
        return condition;
    }


    private BooleanExpression applyIsSoldCondition(Boolean isSold) {
        return isSold != null ? product.isSold.eq(isSold) : null;
    }

}
