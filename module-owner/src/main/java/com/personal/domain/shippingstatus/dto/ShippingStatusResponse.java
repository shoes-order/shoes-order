package com.personal.domain.shippingstatus.dto;

import com.personal.entity.ship.ShipStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public sealed interface ShippingStatusResponse permits
        ShippingStatusResponse.Infos {
    record Infos(
            ShipStatus shippingStatus,
            LocalDate shipStartDate,
            LocalDate shipCompleteDate,
            String trackingNumber,
            String carrier,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    )
            implements ShippingStatusResponse {

    }
}
