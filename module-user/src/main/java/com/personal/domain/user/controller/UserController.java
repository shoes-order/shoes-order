package com.personal.domain.user.controller;

import com.personal.common.entity.AuthUser;
import com.personal.common.entity.SuccessResponse;
import com.personal.common.enums.UserRole;
import com.personal.domain.user.dto.UserRequest;
import com.personal.domain.user.dto.UserResponse;
import com.personal.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {

    private final UserService userService;

    /**
     * 로그인
     * */
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<Void>> login(
            @Valid @RequestBody UserRequest.Login login
    ) {
        String token = userService.login(login);
        return ResponseEntity.ok()
                .header(AUTHORIZATION, token)
                .body(SuccessResponse.of(null));
    }

    /**
     * 회원가입
     * */
    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<Void>> register(
            @Valid @RequestBody UserRequest.Register register
    ) {
        userService.register(register);
        return ResponseEntity.ok()
                .body(SuccessResponse.of(null));
    }

    /**
     * 로그아웃
     * */
    @PostMapping("/logout")
    @Secured({UserRole.Authority.USER})
    public ResponseEntity<SuccessResponse<Void>> logout(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        userService.logout(authUser);
        return ResponseEntity.ok()
                .body(SuccessResponse.of(null));
    }

    /**
     * 유저 프로필 수정
     * */
    @PatchMapping
    @Secured({UserRole.Authority.USER})
    public ResponseEntity<SuccessResponse<Void>> updateProfile(
            @AuthenticationPrincipal AuthUser authUser ,
            @Valid @RequestBody UserRequest.UpdateProfile updateProfile
    ) {
        userService.updateProfile(authUser , updateProfile);
        return ResponseEntity.ok()
                .body(SuccessResponse.of(null));
    }

    /**
     * 유저 프로필 조회
     * */
    @GetMapping
    @Secured({UserRole.Authority.USER})
    public ResponseEntity<SuccessResponse<UserResponse.getProfile>> getProfile(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        return ResponseEntity.ok()
                .body(SuccessResponse.of(userService.getProfile(authUser)));
    }
}
