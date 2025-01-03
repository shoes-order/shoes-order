package com.personal.domain.order.controller;

import com.personal.common.entity.AuthUser;
import com.personal.common.entity.SuccessResponse;
import com.personal.common.enums.UserRole;
import com.personal.domain.order.dto.OrderRequest;
import com.personal.domain.order.dto.OrderResponse;
import com.personal.domain.order.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@Secured({UserRole.Authority.USER})
@RestController
public class OrdersController {

    private final OrdersService ordersService;

    /**
     * 주문하기
     * */
    @PostMapping
    public ResponseEntity<SuccessResponse<Void>> orders(
            @AuthenticationPrincipal AuthUser authUser ,
            @RequestBody OrderRequest.Order order
    ) {
        ordersService.orders(authUser , order);
        return ResponseEntity.ok().body(SuccessResponse.of(null));
    }

    /**
     * 주문 다건 조회
     * */
    @GetMapping
    public ResponseEntity<SuccessResponse<Page<OrderResponse.Infos>>> getOrders(
            @AuthenticationPrincipal AuthUser authUser ,
            @ModelAttribute OrderRequest.GetOrder getOrder
    ) {
        return ResponseEntity.ok().body(SuccessResponse.of(ordersService.getOrders(authUser, getOrder)));
    }

    /**
     * 주문 상세 조회
     * */
    @GetMapping("/{orderId}")
    public ResponseEntity<SuccessResponse<OrderResponse.Info>> getOrder(
            @AuthenticationPrincipal AuthUser authUser ,
            @PathVariable Long orderId
    ) {
        return ResponseEntity.ok().body(SuccessResponse.of(ordersService.getOrder(authUser, orderId)));
    }
}
