package com.personal.domain.product.service;

import com.personal.domain.product.repository.ProductRepository;
import com.personal.entity.product.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductCommonService {

    private final ProductRepository productRepository;

    public List<Product> getProductsById(Long storeId) {
        return productRepository.findByStore_Id(storeId);
    }

    public Product getProductByIdAndStoreId(Long productId , Long storeId) {
        return productRepository.findByIdAndStore_Id(productId, storeId);
    }
}
