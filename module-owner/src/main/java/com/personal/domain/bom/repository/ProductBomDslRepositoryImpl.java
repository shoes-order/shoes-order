package com.personal.domain.bom.repository;

import com.personal.entity.product.ProductBom;
import com.personal.entity.product.QProduct;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.personal.entity.product.QProduct.product;
import static com.personal.entity.product.QProductBom.productBom;
import static com.personal.entity.store.QStore.store;
import static com.personal.entity.user.QUser.user;

@RequiredArgsConstructor
public class ProductBomDslRepositoryImpl implements ProductBomDslRepository {

    private final JPQLQueryFactory queryFactory;

    QProduct baseProduct = new QProduct("baseProduct");
    QProduct materialProduct = new QProduct("materialProduct");

    @Override
    public List<ProductBom> searchStoreProductAndUser(Long storeId, Long productId, Long userId) {
        return queryFactory
                .selectFrom(productBom)
                .join(productBom.baseProduct, baseProduct)
                .join(productBom.materialProduct, materialProduct)
                .join(productBom.baseProduct.store, store).innerJoin(store.user, user) // innerJoin 사용
                .fetchJoin()
                .where(
                        product.id.eq(productId),
                        store.id.eq(storeId),
                        user.id.eq(userId)
                )
                .fetch();
    }


}
