package com.personal.domain.owner.service;

import com.personal.common.code.ResponseCode;
import com.personal.common.config.JwtUtil;
import com.personal.common.entity.AuthUser;
import com.personal.common.enums.UserRole;
import com.personal.common.exception.custom.BadRequestException;
import com.personal.common.exception.custom.NotFoundException;
import com.personal.domain.owner.dto.OwnerRequest;
import com.personal.domain.owner.dto.OwnerResponse;
import com.personal.domain.owner.exception.InvalidPasswordException;
import com.personal.domain.owner.repository.OwnerRepository;
import com.personal.entity.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OwnerService {
    private final JwtUtil jwtUtil;
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;
    private final OwnerCommonService ownerCommonService;

    @Transactional
    public String login(OwnerRequest.Login login) {
        User owner = ownerRepository.findByEmail(login.email()).orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_USER));


        //비밀번호 검증
        if (!passwordEncoder.matches(login.password(), owner.getPassword())) {
            throw new InvalidPasswordException(ResponseCode.PASSWORD_NOT_MATCHED);
        }
        //삭제 여부 검증
        if (owner.isDeleted()) {
            throw new BadRequestException(ResponseCode.NOT_USE_USER);
        }

        //토큰 생성
        String refreshToken = jwtUtil.createToken(owner.getId(), owner.getEmail(), owner.getRole(), JwtUtil.REFRESH);
        return jwtUtil.createToken(owner.getId(), owner.getEmail(), owner.getRole(), jwtUtil.ACCESS);
    }

    @Transactional
    public void register(OwnerRequest.Register register) {
        //이메일이 중복
        if (ownerRepository.findByEmail(register.email()).isPresent()) {
            throw new BadRequestException(ResponseCode.EMAIL_ALREADY_REGISTERED);
        }

        if (!passwordVerification(register.password())) {
            throw new BadRequestException(ResponseCode.INVALID_PASSWORD_FORMAT);
        }
        String encryptPassword = passwordEncoder.encode(register.password());
        User owner = new User(register.email(), encryptPassword, register.name(), UserRole.ROLE_OWNER);
        ownerRepository.save(owner);
    }

    public void logout(AuthUser authUser) {
        // TODO : 추후 redis로 관리하게 될 refreshToken 제거용으로 작성!
    }

    // 비밀번호 정규식 검증 ^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*(),.?":{}|<>]).{8,}$
    private boolean passwordVerification(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$");
        return pattern.matcher(password).matches();
    }


    @Transactional
    public void updateProfile(AuthUser authUser, OwnerRequest.@Valid UpdateProfile updateProfile) {
        User owner = ownerCommonService.getUserById(authUser.getUserId());

        //비밀번호 검증
        if (!passwordEncoder.matches(updateProfile.password(), owner.getPassword())) {
            throw new InvalidPasswordException(ResponseCode.PASSWORD_NOT_MATCHED);
        }

        //업데이트 로직
        owner.updateName(updateProfile.name());
    }

    // 유저 프로필 조회
    public OwnerResponse.GetProfile getProfile(AuthUser authUser) {
        User owner = ownerCommonService.getUserById(authUser.getUserId());
        return new OwnerResponse.GetProfile(owner.getEmail(), owner.getName());
    }


}
