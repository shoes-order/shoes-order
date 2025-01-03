package com.personal.domain.bom.repository;

import com.personal.entity.product.ProductBom;
import com.personal.entity.product.QProduct;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

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
                .select(productBom)
                .distinct()
                .from(productBom)
                .join(productBom.baseProduct, baseProduct).fetchJoin()
                .join(productBom.materialProduct, materialProduct)
                .join(productBom.baseProduct.store, store).fetchJoin()
                .innerJoin(store.user, user).fetchJoin()
                .where(
                        baseProduct.id.eq(productId),
                        store.id.eq(storeId),
                        user.id.eq(userId)
                )
                .fetch();
    }

    @Override
    public Optional<ProductBom> validateStoreProductAndUser(Long storeId, Long productId, Long userId) {
        return Optional.ofNullable((queryFactory
                .select(productBom)
                .distinct()
                .from(productBom)
                .join(productBom.baseProduct, baseProduct).fetchJoin()
                .join(productBom.materialProduct, materialProduct)
                .join(productBom.baseProduct.store, store).fetchJoin()
                .innerJoin(store.user, user).fetchJoin()
                .where(
                        baseProduct.id.eq(productId),
                        store.id.eq(storeId),
                        user.id.eq(userId)
                )
                .fetchOne()));
    }


}
