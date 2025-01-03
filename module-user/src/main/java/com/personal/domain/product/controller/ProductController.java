package com.personal.domain.product.controller;

import com.personal.common.entity.SuccessResponse;
import com.personal.common.enums.UserRole;
import com.personal.domain.product.dto.ProductRequest;
import com.personal.domain.product.dto.ProductResponse;
import com.personal.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores/{storeId}/products")
@Secured({UserRole.Authority.USER})
@RestController
public class ProductController {
    private final ProductService productService;

    /**
     * 매장 상품 조회
     * */
    @GetMapping
    public ResponseEntity<SuccessResponse<List<ProductResponse.Info>>> getProducts(
            @PathVariable Long storeId ,
            @ModelAttribute ProductRequest.GetProduct getProduct
    ) {
        return ResponseEntity.ok().body(SuccessResponse.of(productService.getProducts(storeId , getProduct)));
    }

    /**
     * 매장 상품 상세 조회
     * */
    @GetMapping("/{productId}")
    public ResponseEntity<SuccessResponse<ProductResponse.Info>> getProduct(
            @PathVariable Long storeId ,
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok().body(SuccessResponse.of(productService.getProduct(storeId , productId)));
    }
}
