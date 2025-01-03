package com.personal.domain.shippingstatus.dto;

import java.time.LocalDate;
import java.util.Objects;

public sealed interface ShippingStatusRequest permits
        ShippingStatusRequest.GetShippingStatus {
    record GetShippingStatus(
            Integer page,
            Integer size,
            LocalDate startDate,
            LocalDate endDate
    )
            implements ShippingStatusRequest {

        public GetShippingStatus {
            if (Objects.isNull(page)) page = 1;
            if (Objects.isNull(size)) size = 10;
        }

    }
}
