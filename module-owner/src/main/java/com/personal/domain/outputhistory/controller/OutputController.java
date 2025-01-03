package com.personal.domain.outputhistory.controller;

import com.personal.common.entity.AuthUser;
import com.personal.common.entity.SuccessResponse;
import com.personal.common.enums.UserRole;
import com.personal.domain.outputhistory.dto.OutputRequest;
import com.personal.domain.outputhistory.dto.OutputResponse;
import com.personal.domain.outputhistory.service.OutputService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stores")
@Secured({UserRole.Authority.OWNER})
@RequiredArgsConstructor
public class OutputController {
    private final OutputService outPutService;

    /**
     * 출고 기록 생성
     *
     * @param storeId
     * @param authUser
     * @param createOutPut(productId,type,name,size,qty,price,lot,outputDate)
     * @return
     */
    @PostMapping("/{storeId}/output")
    public ResponseEntity<SuccessResponse<Void>> createOutPut(
            @PathVariable Long storeId,
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody OutputRequest.CreateOutPut createOutPut
    ) {
        outPutService.createOutPut(storeId, authUser, createOutPut);
        return ResponseEntity.ok()
                .body(SuccessResponse.of(null));
    }

    /**
     * 출고 기록 조회
     * @param storeId
     * @param authUser
     * @param getOutputs(startDate,endDate,name,type)
     * @return
     */
    @GetMapping("/{storeId}/output")
    public ResponseEntity<SuccessResponse<Page<OutputResponse.GetInfos>>> getOutputs(
            @PathVariable Long storeId,
            @AuthenticationPrincipal AuthUser authUser,
            @ModelAttribute OutputRequest.GetOutputs getOutputs
    ) {
        return ResponseEntity.ok()
                .body(SuccessResponse.of(outPutService.getOutputs(storeId, authUser,getOutputs)));

    }

}
