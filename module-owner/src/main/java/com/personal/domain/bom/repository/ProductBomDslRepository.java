package com.personal.domain.bom.repository;

import com.personal.entity.product.ProductBom;

import java.util.List;

public interface ProductBomDslRepository {

    List<ProductBom> searchStoreProductAndUser(Long storeId, Long productId, Long userId);
}
