package com.personal.domain.cart.controller;

import com.personal.common.entity.AuthUser;
import com.personal.common.entity.SuccessResponse;
import com.personal.common.enums.UserRole;
import com.personal.domain.cart.dto.CartRequest;
import com.personal.domain.cart.dto.CartResponse;
import com.personal.domain.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Secured({UserRole.Authority.USER})
@RestController
public class CartController {

    private final CartService cartService;

    /**
     * 장바구니 조회
     * */
    @GetMapping("/cart")
    public ResponseEntity<SuccessResponse<CartResponse.GetCart>> getCarts(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        return ResponseEntity.ok().body(SuccessResponse.of(cartService.getCarts(authUser)));
    }

    /**
     * 장바구니 등록
     * */
    @PostMapping("/stores/{storeId}/cart")
    public ResponseEntity<SuccessResponse<Void>> addCart(
            @AuthenticationPrincipal AuthUser authUser ,
            @PathVariable Long storeId ,
            @Valid @RequestBody CartRequest.AddCart addCart
            ) {
        cartService.addCart(authUser , storeId , addCart);
        return ResponseEntity.ok().body(SuccessResponse.of(null));
    }

    /**
     * 장바구니 수정
     * */
    @PatchMapping("/stores/{storeId}/cart/{cartId}")
    public ResponseEntity<SuccessResponse<Void>> modCart(
            @AuthenticationPrincipal AuthUser authUser ,
            @PathVariable Long storeId ,
            @PathVariable Long cartId ,
            @Valid @RequestBody CartRequest.ModCart modCart
    ) {
        cartService.modCart(authUser , storeId , cartId , modCart);
        return ResponseEntity.ok().body(SuccessResponse.of(null));
    }

    /**
     * 장바구니 삭제
     * */
    @DeleteMapping("/stores/{storeId}/cart/{cartId}")
    public ResponseEntity<SuccessResponse<Void>> removeCart(
            @AuthenticationPrincipal AuthUser authUser ,
            @PathVariable Long storeId ,
            @PathVariable Long cartId
    ) {
        cartService.removeCart(authUser , storeId , cartId);
        return ResponseEntity.ok().body(SuccessResponse.of(null));
    }

    /**
     * 장바구니 비우기
     * */
    @DeleteMapping("/cart/empty")
    public ResponseEntity<SuccessResponse<Void>> emptyCart(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        cartService.emptyCart(authUser);
        return ResponseEntity.ok().body(SuccessResponse.of(null));
    }
}
