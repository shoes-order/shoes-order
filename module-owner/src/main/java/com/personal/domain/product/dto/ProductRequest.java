package com.personal.domain.product.dto;

import com.personal.entity.product.ProductType;

import java.util.Objects;

public sealed interface ProductRequest permits
        ProductRequest.GetProducts,
        ProductRequest.AddProduct,
        ProductRequest.UpdateProduct {
    record GetProducts(
            String type,
            String value,
            Boolean isSold,
            String sort,
            Integer page,
            Integer size
    ) implements ProductRequest {
        public GetProducts {
            if (Objects.isNull(page)) page = 1;
            if (Objects.isNull(size)) size = 10;
        }
    }

    record AddProduct(
            ProductType type,
            String name,
            String category,
            String material,
            Long spacing,
            Long basePrice,
            Long customPrice,
            String description

    )
            implements ProductRequest {

    }

    record UpdateProduct(
            ProductType type,
            String name,
            String category,
            String material,
            Long spacing,
            Long basePrice,
            Long customPrice,
            String description
    )
            implements ProductRequest {
    }
}
