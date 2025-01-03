package com.personal.domain.owner.controller;

import com.personal.common.entity.AuthUser;
import com.personal.common.entity.SuccessResponse;
import com.personal.common.enums.UserRole;
import com.personal.domain.owner.dto.OwnerRequest;
import com.personal.domain.owner.dto.OwnerResponse;
import com.personal.domain.owner.service.OwnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequestMapping("api/v1/users")
@RestController
@RequiredArgsConstructor
public class OwnerController {
    private final OwnerService ownerService;

    /**
     * 유저 로그인
     *
     * @param login
     * @return : SuccessResponse
     * @Request : email,password
     */
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<Void>> login(
            @Valid @RequestBody OwnerRequest.Login login
    ) {
        String token = ownerService.login(login);
        return ResponseEntity.ok()
                .header(AUTHORIZATION, token)
                .body(SuccessResponse.of(null));
    }

    /**
     * 회원 가입
     *
     * @param register
     * @Request: email, password, name
     * @return: SuccessResponse
     */
    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<Void>> login(
            @Valid @RequestBody OwnerRequest.Register register) {
        ownerService.register(register);
        return ResponseEntity.ok().body(SuccessResponse.of(null));
    }

    /**
     * 로그아웃
     *
     * @param authUser
     * @return SuccessResponse
     */
    @PostMapping("/logout")
    @Secured({UserRole.Authority.OWNER})
    public ResponseEntity<SuccessResponse<Void>> logout(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        ownerService.logout(authUser);

        return ResponseEntity.ok()
                .body(SuccessResponse.of(null));

    }

    /**
     * 유저 프로필 수정
     *
     * @Param authUser, updateProfile
     * @Request password, name
     * @Return SuccessResponse
     */
    @PatchMapping
    @Secured({UserRole.Authority.OWNER})
    public ResponseEntity<SuccessResponse<Void>> updateProfile(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody OwnerRequest.UpdateProfile updateProfile
    ) {
        ownerService.updateProfile(authUser, updateProfile);

        return ResponseEntity.ok()
                .body(SuccessResponse.of(null));
    }

    /**
     * 유저 프로필 조회
     *
     * @Param authUser
     * @Return SuccessResponse
     */
    @GetMapping
    @Secured({UserRole.Authority.OWNER})
    public ResponseEntity<SuccessResponse<OwnerResponse.GetProfile>> getProfile(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        return ResponseEntity.ok()
                .body(SuccessResponse.of(ownerService.getProfile(authUser)));
    }


}
