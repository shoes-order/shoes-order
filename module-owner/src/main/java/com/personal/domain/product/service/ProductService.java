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

    public Page<ProductResponse.Infos> getProducts(ProductRequest.GetProducts getProducts, Long storeId) {
        Pageable pageable = PageRequest.of(getProducts.page() - 1, getProducts.size());
        // TODO : Cannot invoke "String.hashCode()" because "<local3>" is null 에러 발생 : 해결
        return productRepository.getProducts(getProducts, pageable, storeId);
    }

    public ProductResponse.Info getProduct(Long storeId, Long productId) {
        // TODO : Cannot invoke "com.personal.entity.product.Product.isDeleted()" because "product" is null 에러 발생! : 해결
        //        해당 에러는 매장과 상품 아이디기반의 검증이 덜되어서 난 에러! 예를 들어 storeId가 1인 매장의 상품을 storeId 2라는 값과 조회하면 나는 에러

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
        // TODO : storeId 와 productId 기반으로 검증 후 상품 수정이 가능하도록 할 것 : 해결
        Store store = storeCommonService.getStores(storeId);

        if (!authUser.getUserId().equals(store.getUser().getId())) {
            throw new StoreOwnerMismatchException(ResponseCode.FORBIDDEN_PRODUCTS_UPDATE);
        }
        Product product = productCommonService.getStoreProduct(storeId, productId);

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
        // TODO : 내가 가지고 온 storeId값은 2인데 storeId 1 매장의 상품을 삭제처리하는 문제점 발생 : 해결
        //        storeId와 productId을 합쳐서 하는 검증이 덜 됨
        Store store = storeCommonService.getStores(storeId);
        if (!authUser.getUserId().equals(store.getUser().getId())) {
            throw new StoreOwnerMismatchException(ResponseCode.FORBIDDEN_PRODUCTS_DELETE);
        }
        Product product = productCommonService.getStoreProduct(storeId, productId);
        product.updateIsDeleted(true);
    }


}
