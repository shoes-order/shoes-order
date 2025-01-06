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
                .selectFrom(productBom)
                .join(productBom.baseProduct, baseProduct).fetchJoin()
                .join(productBom.materialProduct, materialProduct).fetchJoin()
                .join(productBom.baseProduct.store, store).fetchJoin()
                .join(store.user, user).fetchJoin()
                .where(
                        baseProduct.id.eq(productId),
                        store.id.eq(storeId),
                        user.id.eq(userId)
                )
                .fetch();
    }

    @Override
    public Optional<ProductBom> validateAndFindBom(Long storeId, Long productId, Long bomId, Long userId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(productBom)
                .join(productBom.baseProduct, baseProduct).fetchJoin()
                .join(productBom.materialProduct, materialProduct).fetchJoin()
                .join(baseProduct.store, store).fetchJoin()
                .join(store.user, user).fetchJoin()
                .where(
                        baseProduct.id.eq(productId),
                        store.id.eq(storeId),
                        user.id.eq(userId),
                        productBom.id.eq(bomId)
                )
                .fetchOne());
    }

    @Override
    public boolean existsBaseAndMaterial(Long productId, Long materialProductId) {
        Long count = queryFactory
                .select(productBom.count())
                .from(productBom)
                .where(
                        productBom.baseProduct.id.eq(productId),
                        productBom.materialProduct.id.eq(materialProductId)
                )
                .fetchOne();
        return count != null && count > 0;
    }

    @Override
    public boolean existsBaseAndMaterialAndBomId(Long productId, Long materialProductId, Long bomId) {
        Long count = queryFactory
                .select(productBom.count())
                .from(productBom)
                .where(
                        productBom.baseProduct.id.eq(productId),
                        productBom.materialProduct.id.eq(materialProductId),
                        productBom.id.ne(bomId)
                )
                .fetchOne();
        return count != null && count > 0;
    }


}
