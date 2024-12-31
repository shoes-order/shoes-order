package com.personal.domain.product.dto;

import java.util.Objects;

public sealed interface ProductBomRequest permits
        ProductBomRequest.CreateBom,
        ProductBomRequest.UpdateBom,
        ProductBomRequest.GetProductBoms {
    record CreateBom(
            Long baseProductId,
            String baseProductName,
            Long baseProductQty,
            Long materialProductId,
            String materialProductName,
            Long materialProductQty

    )
            implements ProductBomRequest {
    }

    record UpdateBom(
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

