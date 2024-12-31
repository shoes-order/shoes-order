package com.personal.domain.product.controller;

import com.personal.common.entity.AuthUser;
import com.personal.common.entity.SuccessResponse;
import com.personal.common.enums.UserRole;
import com.personal.domain.product.dto.ProductBomRequest;
import com.personal.domain.product.dto.ProductBomResponse;
import com.personal.domain.product.service.ProductBomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/v1/stores")
@Secured({UserRole.Authority.OWNER})
@RestController
public class ProductBomController {
    private final ProductBomService productBomService;


    /**
     * bom 생성
     *
     * @param createBom
     * @param authUser
     * @return
     */
    @PostMapping("{storeId}/products/{productId}/bom")
    public ResponseEntity<SuccessResponse<Void>> createBom(
            @RequestBody ProductBomRequest.CreateBom createBom,
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long storeId,
            @PathVariable Long productId
    ) {
        // TODO : ProductBomRequest.CreateBom 내부의 name 파라미터의 필요성?
        productBomService.createBom(createBom, authUser, storeId, productId);

        return ResponseEntity.ok()
                .body(SuccessResponse.of(null));
    }

    /**
     * bom 수정
     *
     * @param updateBom
     * @param storeId
     * @param productId
     * @param bomId
     * @param authUser
     * @return
     */
    @PatchMapping("{storeId}/products/{productId}/bom/{bomId}")
    public ResponseEntity<SuccessResponse<Void>> updateBom(
            @RequestBody ProductBomRequest.UpdateBom updateBom,
            @PathVariable Long storeId,
            @PathVariable Long productId,
            @PathVariable Long bomId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        productBomService.updateBom(updateBom, storeId, productId, bomId, authUser);
        return ResponseEntity.ok()
                .body(SuccessResponse.of(null));
    }

    /**
     * bom 조회
     * @param storeId
     * @param productId
     * @return
     */
    @GetMapping("/{storeId}/products/{productId}/bom")
    public ResponseEntity<SuccessResponse<List<ProductBomResponse.GetInfos>>> getBoms(
            @PathVariable Long storeId,
            @PathVariable Long productId
            ) {
        return ResponseEntity.ok().body(SuccessResponse.of(productBomService.getBoms(storeId, productId)));
    }

    @DeleteMapping("/{storeId}/products/{productId}/bom/{bomId}")
    public ResponseEntity<SuccessResponse<Void>> deleteBom(
            @PathVariable Long storeId,
            @PathVariable Long productId,
            @PathVariable Long bomId,
            @AuthenticationPrincipal AuthUser authUser
    ){
        productBomService.deleteBom(storeId,productId,bomId ,authUser);
        return ResponseEntity.ok()
                .body(SuccessResponse.of(null));
    }
}
