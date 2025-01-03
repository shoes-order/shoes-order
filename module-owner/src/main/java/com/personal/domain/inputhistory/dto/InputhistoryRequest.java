package com.personal.domain.inputhistory.dto;


import com.personal.entity.product.ProductType;

import java.time.LocalDate;
import java.util.Objects;

public sealed interface InputhistoryRequest permits
        InputhistoryRequest.CreateInput,
        InputhistoryRequest.GetInputHistories
        {
    record CreateInput(
            Long productId,
            String name,
            Long size,
            Long qty,
            Long price,
            String lot,
            String description,
            LocalDate inputDate

    )
            implements InputhistoryRequest {
    }

    record GetInputHistories(
            Integer page,
            Integer size,
            String sort,
            ProductType type,
            String name,
            LocalDate startDate,
            LocalDate endDate
    )
            implements InputhistoryRequest {

        public GetInputHistories {
            if(Objects.isNull(page)) page =1;
            if(Objects.isNull(size)) size =10;
        }
    }
}
