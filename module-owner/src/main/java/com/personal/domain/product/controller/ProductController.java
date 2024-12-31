package com.personal.domain.product.controller;

import com.personal.common.entity.AuthUser;
import com.personal.common.entity.SuccessResponse;
import com.personal.common.enums.UserRole;
import com.personal.domain.product.dto.ProductRequest;
import com.personal.domain.product.dto.ProductResponse;
import com.personal.domain.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/stores")
@Secured({UserRole.Authority.OWNER})
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    /**
     * 상품 다건 조회
     * 해당 가게의 상품들(원자재,완제품)전부 조회
     * 삭제되지 않은 상품들 조회
     *
     * @param getProducts
     * @return SuccessResponse
     */
    @GetMapping("/{storeId}/products")
    public ResponseEntity<SuccessResponse<Page<ProductResponse.Infos>>> getProducts(
            @ModelAttribute ProductRequest.GetProducts getProducts,
            @PathVariable Long storeId
    ) {
        return ResponseEntity.ok().body(SuccessResponse.of(productService.getProducts(getProducts, storeId)));

    }

    /**
     * 상품 단일 조회
     *
     * @param storeId
     * @param productId
     * @return
     */
    @GetMapping("/{storeId}/products/{productId}")
    public ResponseEntity<SuccessResponse<ProductResponse.Info>> getProduct(
            @PathVariable Long storeId,
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok().body(SuccessResponse.of(productService.getProduct(storeId, productId)));
    }


    /**
     * 상품 등록
     *
     * @param addProduct
     * @return SuccessResponse
     */
    @PostMapping("/{storeId}/products")
    public ResponseEntity<SuccessResponse<Void>> addProduct(
            @Valid @RequestBody ProductRequest.AddProduct addProduct,
            @PathVariable Long storeId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        productService.addProduct(addProduct, storeId, authUser);
        return ResponseEntity.ok()
                .body(SuccessResponse.of(null));
    }

    /**
     * 상품 수정
     */
    @PatchMapping("/{storeId}/products/{productId}")
    public ResponseEntity<SuccessResponse<Void>> updateProduct(
            @Valid @RequestBody ProductRequest.UpdateProduct updateProduct,
            @PathVariable Long storeId,
            @PathVariable Long productId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        productService.updateProduct(updateProduct, productId, storeId, authUser);

        return ResponseEntity.ok()
                .body(SuccessResponse.of(null));
    }

    /**
     * 상품 삭제
     */
    @DeleteMapping("/{storeId}/products/{productId}")
    public ResponseEntity<SuccessResponse<Void>> deleteProduct(
            @PathVariable Long storeId,
            @PathVariable Long productId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        productService.deleteProduct(storeId, productId, authUser);

        return ResponseEntity.ok()
                .body(SuccessResponse.of(null));
    }
}
