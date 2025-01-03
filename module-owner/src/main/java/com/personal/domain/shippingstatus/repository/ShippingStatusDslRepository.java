package com.personal.domain.shippingstatus.repository;

import com.personal.domain.shippingstatus.dto.ShippingStatusRequest;
import com.personal.domain.shippingstatus.dto.ShippingStatusResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShippingStatusDslRepository {
    Page<ShippingStatusResponse.Infos> getShippingStatus(Pageable pageable, ShippingStatusRequest.GetShippingStatus getShippingStatus);
}
