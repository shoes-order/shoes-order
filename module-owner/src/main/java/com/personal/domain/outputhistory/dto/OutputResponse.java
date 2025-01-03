package com.personal.domain.outputhistory.dto;

import com.personal.entity.product.ProductType;

import java.time.LocalDate;

public sealed interface OutputResponse permits
        OutputResponse.GetInfos {
    record GetInfos(
            Long productId,
            String name,
            Long size,
            Long qty,
            Long price,
            String lot,
            LocalDate outputDate
    )
            implements OutputResponse {

    }
}
