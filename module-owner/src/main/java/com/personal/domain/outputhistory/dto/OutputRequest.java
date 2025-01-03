package com.personal.domain.outputhistory.dto;

import com.personal.entity.product.ProductType;

import java.time.LocalDate;
import java.util.Objects;

public sealed interface OutputRequest permits
        OutputRequest.CreateOutPut,
        OutputRequest.GetOutputs {

    record CreateOutPut(
            Long productId,
            String name,
            Long size,
            Long qty,
            Long price,
            String lot,
            LocalDate outputDate
    ) implements OutputRequest {

    }

    record GetOutputs(
            Integer page,
            Integer size,
            String sort,
            LocalDate startDate,
            LocalDate endDate,
            String name,
            ProductType type
    ) implements OutputRequest {
        public GetOutputs {
            if (Objects.isNull(page)) page = 1;
            if (Objects.isNull(size)) size = 10;
        }

    }
}
