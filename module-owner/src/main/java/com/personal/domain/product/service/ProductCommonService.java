package com.personal.domain.product.service;

import com.personal.common.code.ResponseCode;
import com.personal.common.exception.custom.NotFoundException;
import com.personal.domain.product.exception.NotFoundProductException;
import com.personal.domain.product.repository.ProductRepository;
import com.personal.entity.product.Product;
import com.personal.entity.product.ProductType;
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

    // 해당가게의 상품 가져오기
    public Product getStoreProduct(Long storeId, Long productId) {
        return productRepository.getStoreProduct(storeId, productId)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_PRODUCT));
    }
}
