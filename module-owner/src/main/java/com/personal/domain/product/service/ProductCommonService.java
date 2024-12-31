package com.personal.domain.product.service;

import com.personal.common.code.ResponseCode;
import com.personal.domain.product.exception.NotFoundProductException;
import com.personal.domain.product.repository.ProductRepository;
import com.personal.entity.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCommonService {

    private final ProductRepository productRepository;

    public Product getProducts(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundProductException(ResponseCode.NOT_FOUND_PRODUCT));
    }

    public Product getStoreProduct(Long storeId, Long productId) {
        return productRepository.getStoreProduct(storeId, productId);
    }
}
