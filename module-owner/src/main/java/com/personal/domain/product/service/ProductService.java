package com.personal.domain.product.service;

import com.personal.common.code.ResponseCode;
import com.personal.common.entity.AuthUser;
import com.personal.domain.product.dto.ProductRequest;
import com.personal.domain.product.dto.ProductResponse;
import com.personal.domain.product.exception.NotFoundProductException;
import com.personal.domain.product.repository.ProductRepository;
import com.personal.domain.store.exception.StoreOwnerMismatchException;
import com.personal.domain.store.service.StoreCommonService;
import com.personal.entity.product.Product;
import com.personal.entity.product.ProductType;
import com.personal.entity.store.Store;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final StoreCommonService storeCommonService;
    private final ProductCommonService productCommonService;

    public Page<ProductResponse.Infos> getProducts(ProductRequest.GetProducts getProducts, Long storeId, AuthUser authUser) {
        storeCommonService.getStores(storeId);
        storeCommonService.validateUserAccess(authUser, storeId);
        Pageable pageable = PageRequest.of(getProducts.page() - 1, getProducts.size());
        return productRepository.getProducts(getProducts, pageable, storeId);
    }

    public ProductResponse.Info getProduct(Long storeId, Long productId) {

        // 가게 유무 확인
        storeCommonService.getStores(storeId);

        // 가게의 상품 찾기
        Product product = productCommonService.getStoreProduct(storeId, productId);

        //상품 삭제 유무 확인
        if (product.isDeleted()) {
            throw new NotFoundProductException(ResponseCode.NOT_FOUND_PRODUCT);
        }

        return new ProductResponse.Info(
                product.getId(),
                product.getType(),
                product.getName(),
                product.getCategory(),
                product.getMaterial(),
                product.getBasePrice(),
                product.getCustomPrice(),
                product.getDescription()
        );
    }

    @Transactional
    public void addProduct(ProductRequest.AddProduct addProduct, Long storeId, AuthUser authUser) {

        Store store = storeCommonService.getStores(storeId);
        if (!authUser.getUserId().equals(store.getUser().getId())) {
            throw new StoreOwnerMismatchException(ResponseCode.FORBIDDEN_PRODUCTS_ADD);
        }

        if (addProduct.type().equals(ProductType.PRODUCT)) {
            store.updateProductSaleCnt(store.getProductSaleCnt() + 1L);
        }

        Product product = Product.builder()
                .store(store)
                .type(addProduct.type())
                .name(addProduct.name())
                .category(addProduct.category())
                .material(addProduct.material())
                .spacing(addProduct.spacing())
                .basePrice(addProduct.basePrice())
                .customPrice(addProduct.customPrice())
                .description(addProduct.description())
                .build();
        productRepository.save(product);
    }

    @Transactional
    public void updateProduct(ProductRequest.UpdateProduct updateProduct, Long productId, Long storeId, AuthUser authUser) {
        Store store = storeCommonService.getStores(storeId);

        if (!authUser.getUserId().equals(store.getUser().getId())) {
            throw new StoreOwnerMismatchException(ResponseCode.FORBIDDEN_PRODUCTS_UPDATE);
        }
        Product product = productCommonService.getStoreProduct(storeId, productId);

        if (!product.getType().equals(updateProduct.type())) {
            if (updateProduct.type().equals(ProductType.PRODUCT)) {
                store.updateProductSaleCnt(store.getProductSaleCnt() + 1L);
            } else {
                store.updateProductSaleCnt(store.getProductSaleCnt() - 1L);
            }
        }

        product.updateProducts(
                updateProduct.type(),
                updateProduct.name(),
                updateProduct.category(),
                updateProduct.material(),
                updateProduct.spacing(),
                updateProduct.basePrice(),
                updateProduct.customPrice(),
                updateProduct.description()
        );
    }

    @Transactional
    public void deleteProduct(Long storeId, Long productId, AuthUser authUser) {
        Store store = storeCommonService.getStores(storeId);
        if (!authUser.getUserId().equals(store.getUser().getId())) {
            throw new StoreOwnerMismatchException(ResponseCode.FORBIDDEN_PRODUCTS_DELETE);
        }
        Product product = productCommonService.getStoreProduct(storeId, productId);

        if (product.getType().equals(ProductType.PRODUCT)) {
            store.updateProductSaleCnt(store.getProductSaleCnt() - 1L);
        }

        product.updateIsDeleted(true);
    }


}
