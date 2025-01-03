package com.personal.domain.bom.dto;

import java.util.Objects;

public sealed interface ProductBomRequest permits
        ProductBomRequest.CreateBom,
        ProductBomRequest.UpdateBom,
        ProductBomRequest.GetProductBoms {
    record CreateBom(
            Long baseProductId,
            Long baseProductQty,
            Long materialProductId,
            Long materialProductQty

    )
            implements ProductBomRequest {
    }

    record UpdateBom(
            Long baseProductId,
            Long baseProductQty,
            Long materialProductId,
            Long materialProductQty
    )
            implements ProductBomRequest {
    }

    record GetProductBoms(
            Integer page,
            Integer size
    )
            implements ProductBomRequest {
        public GetProductBoms {
            if (Objects.isNull(page)) page = 1;
            if (Objects.isNull(size)) size = 10;
        }

    }
}

