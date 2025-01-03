package com.personal.domain.inputhistory.controller;

import com.personal.common.entity.AuthUser;
import com.personal.common.entity.SuccessResponse;
import com.personal.common.enums.UserRole;
import com.personal.domain.inputhistory.dto.InputHistoryResponse;
import com.personal.domain.inputhistory.dto.InputhistoryRequest;
import com.personal.domain.inputhistory.service.InputHistoryService;
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
public class InputHistoryController {
    private final InputHistoryService inputHistoryService;

    /**
     * 입고 기록 생성
     *
     * @param storeId
     * @param createInput
     * @param authUser
     * @return
     */
    @PostMapping("/{storeId}/input")
    public ResponseEntity<SuccessResponse<Void>> createInput(
            @PathVariable Long storeId,
            @RequestBody InputhistoryRequest.CreateInput createInput,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        inputHistoryService.createInput(storeId, createInput, authUser);
        return ResponseEntity.ok()
                .body(SuccessResponse.of(null));
    }

    /**
     * 입고 기록 조회
     *
     * @param storeId
     * @param getInputHistories(type,name,startDate,endDate)
     * @param authUser
     * @return
     */
    @GetMapping("/{storeId}/input")
    public ResponseEntity<SuccessResponse<Page<InputHistoryResponse.GetInfos>>> getInputHistories(
            @PathVariable Long storeId,
            @ModelAttribute InputhistoryRequest.GetInputHistories getInputHistories,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        return ResponseEntity.ok()
                .body(SuccessResponse.of(inputHistoryService.getInputHistories(storeId, getInputHistories, authUser)));

    }
}
