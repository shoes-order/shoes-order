package com.personal.domain.stock.controller;

import com.personal.common.entity.AuthUser;
import com.personal.common.entity.SuccessResponse;
import com.personal.common.enums.UserRole;
import com.personal.domain.stock.dto.StockRequest;
import com.personal.domain.stock.dto.StockResponse;
import com.personal.domain.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores")
@Secured({UserRole.Authority.OWNER})
public class StockController {
    private final StockService stockService;

    /**
     * 가게 아이디에 해당하는 재고 모두 조회
     * (원자재,완제품 선택 가능)
     * (정렬 기준 선택 가능)
     * @param storeId
     * @param getStocks
     * @param authUser
     * @return SuccessResponse(200)
     */
    @GetMapping("/{storeId}/stock")
    public ResponseEntity<SuccessResponse<Page<StockResponse.Infos>>> getStocks(
            @PathVariable Long storeId,
            @ModelAttribute StockRequest.GetStocks getStocks,
            @AuthenticationPrincipal AuthUser authUser
    ) {

        return ResponseEntity.ok()
                .body(SuccessResponse.of(stockService.getStocks(storeId, getStocks, authUser)));

    }
}
