package com.personal.domain.user.service;

import com.personal.common.code.ResponseCode;
import com.personal.common.config.JwtUtil;
import com.personal.common.entity.AuthUser;
import com.personal.common.enums.UserRole;
import com.personal.common.exception.custom.BadRequestException;
import com.personal.domain.user.dto.UserRequest;
import com.personal.domain.user.dto.UserResponse;
import com.personal.domain.user.exception.InvalidPasswordException;
import com.personal.domain.user.repository.UserRepository;
import com.personal.entity.user.User;
import com.personal.entity.user.UserAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final UserCommonService userCommonService;
    private final UserAddressCommonService userAddressCommonService;

    @Transactional
    public String login(UserRequest.Login login) {
        User user = userCommonService.getUserByEmail(login.email());

        // 비밀번호 검증
        if (!passwordEncoder.matches(login.password(), user.getPassword())) {
            throw new InvalidPasswordException(ResponseCode.PASSWORD_NOT_MATCHED);
        }

        // 삭제 여부 검증
        if (user.isDeleted()) {
            throw new BadRequestException(ResponseCode.NOT_USE_USER);
        }

        // 토큰 생성
        String refreshToken = jwtUtil.createToken(user.getId() , user.getEmail() , user.getRole() , JwtUtil.REFRESH);
        return jwtUtil.createToken(user.getId() , user.getEmail() , user.getRole() , JwtUtil.ACCESS);
    }

    @Transactional
    public void register(UserRequest.Register register) {
        // 중복된 이메일 확인
        if (userRepository.findByEmail(register.email()).isPresent()) {
            throw new BadRequestException(ResponseCode.EMAIL_ALREADY_REGISTERED);
        }

        // 비밀번호 정규식 검증
        if (!passwordVerification(register.password())) {
            throw new InvalidPasswordException(ResponseCode.INVALID_PASSWORD_FORMAT);
        }

        // 비밀번호 인코딩
        String encryptPassword = passwordEncoder.encode(register.password());
        User user = new User(register.email() , encryptPassword , register.name() , UserRole.ROLE_USER);

        userRepository.save(user);
    }

    public void logout(AuthUser authUser) {
        // TODO : 추후 redis로 관리하게 될 refreshToken 제거용으로 작성!
    }

    @Transactional
    public void updateProfile(AuthUser authUser , UserRequest.UpdateProfile updateProfile) {
        // 본인이 맞는지 검증?
        User user = userCommonService.getUserById(authUser.getUserId());

        // 비밀번호 검증
        if (!passwordEncoder.matches(updateProfile.password(), user.getPassword())) {
            throw new InvalidPasswordException(ResponseCode.PASSWORD_NOT_MATCHED);
        }

        // 업데이트 로직
        user.updateName(updateProfile.name());
    }

    public UserResponse.getProfile getProfile(AuthUser authUser) {
        User user = userCommonService.getUserById(authUser.getUserId());
        UserAddress userAddress = userAddressCommonService.getRepUserAddress(authUser.getUserId());
        return new UserResponse.getProfile(user.getEmail() , user.getName() , userAddress.getAddress() , userAddress.getAddressDetail());
    }

    // 비밀번호 정규식 검증 ^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*(),.?":{}|<>]).{8,}$
    private boolean passwordVerification(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$");
        return pattern.matcher(password).matches();
    }
}
