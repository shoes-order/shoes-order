package com.personal.domain.bom.repository;

import com.personal.entity.product.ProductBom;

import java.util.List;
import java.util.Optional;

public interface ProductBomDslRepository {

    List<ProductBom> searchStoreProductAndUser(Long storeId, Long productId, Long userId);



    Optional<ProductBom> validateAndFindBom(Long storeId, Long productId, Long bomId, Long userId);

    boolean existsBaseAndMaterial(Long productId, Long materialProductId);
}
