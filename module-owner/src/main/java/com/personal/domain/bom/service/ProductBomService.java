package com.personal.domain.bom.service;

import com.personal.common.code.ResponseCode;
import com.personal.common.entity.AuthUser;
import com.personal.common.exception.custom.NotFoundException;
import com.personal.domain.bom.dto.ProductBomRequest;
import com.personal.domain.bom.dto.ProductBomResponse;
import com.personal.domain.bom.repository.ProductBomRepository;
import com.personal.domain.product.service.ProductCommonService;
import com.personal.domain.stock.service.StockService;
import com.personal.domain.store.service.StoreCommonService;
import com.personal.entity.product.Product;
import com.personal.entity.product.ProductBom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductBomService {
    private final ProductBomRepository productBomRepository;
    private final ProductCommonService productCommonService;
    private final StoreCommonService storeCommonService;
    private final ProductBomCommonService productBomCommonService;
    private final StockService stockService;


    @Transactional
    public void createBom(ProductBomRequest.CreateBom createBom, AuthUser authUser, Long storeId, Long productId) {
        storeCommonService.getStores(storeId);
        storeCommonService.validateUserAccess(authUser, storeId);


        // 해당가게에서 기본 제품 가져오기
        Product baseProduct = productCommonService.getStoreProduct(storeId, productId);
        // 해당가게에서 하위 제품 가져오기
        Product materialProduct = productCommonService.getStoreProduct(storeId, createBom.materialProductId());

        // 상품 타입 검증
        productCommonService.validateProductType(baseProduct, materialProduct);

        // 해당 BOM이 존재하는지 확인
        productBomCommonService.checkForDuplicateBom(productId, createBom.materialProductId());
        ProductBom productBom = ProductBom.builder()
                .baseProduct(baseProduct)
                .baseQty(createBom.baseProductQty())
                .materialProduct(materialProduct)
                .materialQty(createBom.materialProductQty())
                .build();
        productBomRepository.save(productBom);

    }


    @Transactional
    public void updateBom(ProductBomRequest.UpdateBom updateBom, Long storeId, Long bomId, Long productId, AuthUser authUser) {
        Long userId = authUser.getUserId();


        //기존 BOM 확인
        ProductBom productBom = productBomRepository.validateAndFindBom(storeId, productId, bomId, userId)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_PRODUCTBOM));
        // 해당가게에서 기본 제품 가져오기
        Product baseProduct = productCommonService.getStoreProduct(storeId, productId);
        // 해당가게에서 하위 제품 가져오기
        Product materialProduct = productCommonService.getStoreProduct(storeId, updateBom.materialProductId());

        // 상품 타입 검증
        productCommonService.validateProductType(baseProduct, materialProduct);

        // 해당 BOM이 존재하는지 확인
        productBomCommonService.checkForDuplicateBom(productId, updateBom.materialProductId());

        productBom.updateProductBom(
                baseProduct,
                updateBom.baseProductQty(),
                materialProduct,
                updateBom.materialProductQty()
        );
    }

    public List<ProductBomResponse.GetInfos> getBoms(Long storeId, Long productId, AuthUser authUser) {
        storeCommonService.getStores(storeId);
        storeCommonService.validateUserAccess(authUser, storeId);

        Long userId = authUser.getUserId();
        List<ProductBom> results = productBomRepository.searchStoreProductAndUser(storeId, productId, userId);
        if (results.isEmpty()) {
            throw new NotFoundException(ResponseCode.NOT_FOUND_STORE_PRODUCT);
        }


        return results.stream().map(productBom -> new ProductBomResponse.GetInfos(
                productBom.getBaseProduct().getId(),
                productBom.getBaseProduct().getName(),
                productBom.getBaseQty(),
                productBom.getMaterialProduct().getId(),
                productBom.getMaterialProduct().getName(),
                productBom.getMaterialQty()

        )).toList();
    }

    @Transactional
    public void deleteBom(Long storeId, Long productId, Long bomId, AuthUser authUser) {
        Long userId = authUser.getUserId();

        ProductBom bom = productBomRepository.validateAndFindBom(storeId, productId, bomId, userId)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_PRODUCTBOM));

        productBomRepository.delete(bom);
    }
}
