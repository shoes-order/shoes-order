package com.personal.domain.orders.controller;

import com.personal.common.entity.AuthUser;
import com.personal.common.entity.SuccessResponse;
import com.personal.common.enums.UserRole;
import com.personal.domain.orders.dto.OrdersRequest;
import com.personal.domain.orders.dto.OrdersResponse;
import com.personal.domain.orders.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores/{storeId}/orders")
@Secured({UserRole.Authority.OWNER})
@RestController
public class OrdersController {
    private final OrdersService ordersService;

    /**
     * 주문 조회
     * */
    @GetMapping
    public ResponseEntity<SuccessResponse<Page<OrdersResponse.OrdersInfo>>> getOrders(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long storeId,
            @ModelAttribute OrdersRequest.GetOrders getOrders
    ) {
        return ResponseEntity.ok().body(SuccessResponse.of(ordersService.getOrders(authUser, storeId, getOrders)));
    }

    /**
     * 주문 상태 변경
     * */
    @PostMapping("/{orderId}")
    public ResponseEntity<SuccessResponse<Void>> orderStatusChange(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long storeId,
            @PathVariable Long orderId
    ) {
        ordersService.orderStatusChange(authUser, storeId, orderId);
        return ResponseEntity.ok().body(SuccessResponse.of(null));
    }

    /**
     * 주문 취소(생산되고 있는 시점에서는 취소 불가능)
     * */
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<SuccessResponse<Void>> orderCancel(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long storeId,
            @PathVariable Long orderId
    ) {
        ordersService.orderCancel(authUser, storeId , orderId);
        return ResponseEntity.ok().body(SuccessResponse.of(null));
    }

}
