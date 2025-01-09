package com.personal.domain.store.controller;

import com.personal.common.entity.AuthUser;
import com.personal.common.entity.SuccessResponse;
import com.personal.common.enums.UserRole;
import com.personal.domain.store.dto.StoreRequest;
import com.personal.domain.store.dto.StoreResponse;
import com.personal.domain.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("api/v1/stores")
@Secured({UserRole.Authority.OWNER})
public class StoreController {
    private final StoreService storeService;

    /**
     * 매장 등록
     */
    @PostMapping
    public ResponseEntity<SuccessResponse<Void>> saveStores(
            @Valid @RequestBody StoreRequest.SaveStores saveStores,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        storeService.saveStores(saveStores, authUser);
        return ResponseEntity.ok()
                .body(SuccessResponse.of(null));
    }

    /**
     * 매장 조회(자신이 등록한 매장들)
     */
    @GetMapping
    public ResponseEntity<SuccessResponse<Page<StoreResponse.GetStores>>> getStores(
            @AuthenticationPrincipal AuthUser authUser,
            @ModelAttribute StoreRequest.GetStores getStores
    ) {
        return ResponseEntity.ok()
                .body(SuccessResponse.of(storeService.getStores(authUser, getStores)));
    }

    /**
     * 매장 조회(자신이 등록한 매장 하나)
     */
    @GetMapping("/{storeId}")
    public ResponseEntity<SuccessResponse<StoreResponse.GetStores>> getStore(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long storeId
    ) {

        return ResponseEntity.ok().body(SuccessResponse.of(storeService.getStore(authUser, storeId)));
    }

    /**
     * 매장 수정
     */
    @PatchMapping("/{storeId}")
    public ResponseEntity<SuccessResponse<Void>> updateStores(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long storeId,
            @Valid @RequestBody StoreRequest.UpdateStores updateStores
    ) {
        storeService.updateStores(authUser, storeId, updateStores);
        return ResponseEntity.ok().body(SuccessResponse.of(null));
    }

    /**
     * 매장 삭제
     */
    @DeleteMapping("/{storeId}")
    public ResponseEntity<SuccessResponse<Void>> deleteStores(
            @PathVariable Long storeId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        storeService.deleteStores(storeId, authUser);

        return ResponseEntity.ok()
                .body(SuccessResponse.of(null));
    }
}
