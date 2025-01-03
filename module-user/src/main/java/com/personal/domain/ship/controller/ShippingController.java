package com.personal.domain.ship.controller;

import com.personal.common.entity.AuthUser;
import com.personal.common.entity.SuccessResponse;
import com.personal.common.enums.UserRole;
import com.personal.domain.ship.dto.ShippingRequest;
import com.personal.domain.ship.dto.ShippingResponse;
import com.personal.domain.ship.service.ShippingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/shipping-status")
@Secured({UserRole.Authority.USER})
@RestController
public class ShippingController {

    private final ShippingService shippingService;

    @GetMapping
    public ResponseEntity<SuccessResponse<Page<ShippingResponse.Info>>> getShippingStatus(
            @AuthenticationPrincipal AuthUser authUser,
            @ModelAttribute ShippingRequest.GetShipping getShipping
    ) {
        return ResponseEntity.ok().body(SuccessResponse.of(shippingService.getShippingStatus(authUser , getShipping)));
    }
}
