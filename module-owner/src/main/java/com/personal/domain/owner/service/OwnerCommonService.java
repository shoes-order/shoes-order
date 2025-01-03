package com.personal.domain.owner.service;

import com.personal.common.code.ResponseCode;
import com.personal.common.exception.custom.NotFoundException;
import com.personal.domain.owner.repository.OwnerRepository;
import com.personal.entity.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class OwnerCommonService {
    private final OwnerRepository ownerRepository;

    public User getUserById(Long userId) {
        return ownerRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_USER));
    }
}
