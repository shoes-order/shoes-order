package com.personal.domain.shippingstatus.service;

import com.personal.common.entity.AuthUser;
import com.personal.domain.shippingstatus.dto.ShippingStatusRequest;
import com.personal.domain.shippingstatus.dto.ShippingStatusResponse;
import com.personal.domain.shippingstatus.repository.ShippingStatusRepository;
import com.personal.domain.store.service.StoreCommonService;
import com.personal.entity.ship.ShipStatus;
import com.personal.entity.ship.ShippingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShippingStatusService {
    private final ShippingStatusRepository shippingStatusRepository;
    private final StoreCommonService storeCommonService;
    private final ShippingStatusCommonService shippingStatusCommonService;

    public Page<ShippingStatusResponse.Infos> getShippingStatus(Long storeId, ShippingStatusRequest.GetShippingStatus getShippingStatus, AuthUser authUser) {
        Pageable pageable = PageRequest.of(getShippingStatus.page() - 1, getShippingStatus.size());
        storeCommonService.getStores(storeId);
        storeCommonService.validateUserAccess(authUser, storeId);

        return shippingStatusRepository.getShippingStatus(pageable, getShippingStatus);


    }

    @Transactional
    public void updateShippingStatus(Long storeId, Long shippingId) {
        storeCommonService.getStores(storeId);
        ShippingStatus shippingStatus = shippingStatusCommonService.getShipping(shippingId);

        shippingStatus.updateStatus(ShipStatus.DELIVERED);

    }
}
