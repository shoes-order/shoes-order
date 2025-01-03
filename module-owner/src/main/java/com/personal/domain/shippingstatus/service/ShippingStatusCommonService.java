package com.personal.domain.shippingstatus.service;

import com.personal.common.code.ResponseCode;
import com.personal.common.exception.custom.NotFoundException;
import com.personal.domain.shippingstatus.repository.ShippingStatusRepository;
import com.personal.entity.ship.ShippingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShippingStatusCommonService {
    private final ShippingStatusRepository shippingStatusRepository;


    public ShippingStatus getShipping(Long shippingId) {
        return shippingStatusRepository.findById(shippingId)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_SHIPPING));
    }
}
