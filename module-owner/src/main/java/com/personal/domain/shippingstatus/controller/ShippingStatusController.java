package com.personal.domain.shippingstatus.controller;

import com.personal.common.entity.AuthUser;
import com.personal.common.entity.SuccessResponse;
import com.personal.common.enums.UserRole;
import com.personal.domain.shippingstatus.dto.ShippingStatusRequest;
import com.personal.domain.shippingstatus.dto.ShippingStatusResponse;
import com.personal.domain.shippingstatus.service.ShippingStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
@Secured({UserRole.Authority.OWNER})
public class ShippingStatusController {
    private final ShippingStatusService shippingStatusService;

    /**
     * 배송 현황 조회
     *
     * @param storeId
     * @param getShippingStatus
     * @param authUser
     * @return
     */
    @GetMapping("/{storeId}/shipping-status")
    public ResponseEntity<SuccessResponse<Page<ShippingStatusResponse.Infos>>> getShippingStatus(
            @PathVariable Long storeId,
            @ModelAttribute ShippingStatusRequest.GetShippingStatus getShippingStatus,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        return ResponseEntity.ok()
                .body(SuccessResponse.of(shippingStatusService.getShippingStatus(storeId, getShippingStatus, authUser)));

    }

    /**
     * 주문 상태 변경
     *
     * @param storeId
     * @return
     */
    @PostMapping("/{storeId}/shipping/{shippingId}")
    public ResponseEntity<SuccessResponse<Void>> updateShippingStatus(
            @PathVariable Long storeId,
            @PathVariable Long shippingId
    ) {
        shippingStatusService.updateShippingStatus(storeId, shippingId);

        return ResponseEntity.ok()
                .body(SuccessResponse.of(null));
    }
}
