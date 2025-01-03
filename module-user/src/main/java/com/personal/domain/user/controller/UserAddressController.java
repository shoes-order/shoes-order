package com.personal.domain.user.controller;

import com.personal.common.entity.AuthUser;
import com.personal.common.entity.SuccessResponse;
import com.personal.common.enums.UserRole;
import com.personal.domain.user.dto.UserAddressRequest;
import com.personal.domain.user.dto.UserAddressResponse;
import com.personal.domain.user.service.UserAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users/address")
@Secured({UserRole.Authority.USER})
@RestController
public class UserAddressController {

    private final UserAddressService userAddressService;


    /**
     * 주소 등록
     * */
    @PostMapping
    public ResponseEntity<SuccessResponse<Void>> addUserAddress(
            @AuthenticationPrincipal AuthUser authUser ,
            @Valid @RequestBody UserAddressRequest.UserAddress userAddress
            ) {
        userAddressService.addUserAddress(authUser , userAddress);
        return ResponseEntity.ok()
                .body(SuccessResponse.of(null));
    }

    /**
     * 주소 수정
     * */
    @PatchMapping("/{userAddressId}")
    public ResponseEntity<SuccessResponse<Void>> modUserAddress(
            @AuthenticationPrincipal AuthUser authUser ,
            @PathVariable Long userAddressId,
            @Valid @RequestBody UserAddressRequest.UserAddress userAddress
    ) {
        userAddressService.modUserAddress(authUser , userAddressId , userAddress);
        return ResponseEntity.ok()
                .body(SuccessResponse.of(null));
    }

    /**
     * 주소 조회
     * */
    @GetMapping
    public ResponseEntity<SuccessResponse<List<UserAddressResponse.UserAddress>>> getUserAddresses(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        return ResponseEntity.ok()
                .body(SuccessResponse.of(userAddressService.getUserAddresses(authUser)));
    }

    /**
     * 대표 주소 설정
     * */
    @PatchMapping("/repyn")
    public ResponseEntity<SuccessResponse<Void>> setRepresentativeAddress(
            @AuthenticationPrincipal AuthUser authUser ,
            @Valid @RequestBody UserAddressRequest.RepYN repYN

    ) {
        userAddressService.setRepresentativeAddress(authUser , repYN);
        return ResponseEntity.ok()
                .body(SuccessResponse.of(null));
    }

    /**
     * 주소 삭제
     * */
    @DeleteMapping("/{userAddressId}")
    public ResponseEntity<SuccessResponse<Void>> deleteUserAddress(
            @AuthenticationPrincipal AuthUser authUser ,
            @PathVariable Long userAddressId
    ) {
        userAddressService.deleteUserAddress(authUser , userAddressId);
        return ResponseEntity.ok()
                .body(SuccessResponse.of(null));
    }
}
