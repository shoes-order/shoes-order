package com.personal.domain.product.repository;

import com.personal.domain.product.dto.ProductRequest;
import com.personal.domain.product.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductDslRepository {
    Page<ProductResponse.Infos> getProducts(ProductRequest.GetProducts getProducts, Pageable pageable ,Long storeId);
}
