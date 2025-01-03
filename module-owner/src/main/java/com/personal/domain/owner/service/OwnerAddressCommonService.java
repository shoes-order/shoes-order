package com.personal.domain.owner.service;

import com.personal.domain.owner.repository.OwnerAddressRepository;
import com.personal.entity.user.UserAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OwnerAddressCommonService {
    private final OwnerAddressRepository ownerAddressRepository;

    //해당 유저의 대표 주소 찾기
    public UserAddress getReqOwnerAddress(Long userId) {
        return ownerAddressRepository.findByUserIdAndRepYNTrue(userId)
                .orElse(new UserAddress());
    }
}
