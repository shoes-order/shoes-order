package com.personal.domain.product.dto;

import com.personal.entity.product.ProductType;
import jakarta.validation.constraints.NotBlank;

public sealed interface ProductResponse permits
        ProductResponse.Infos,
        ProductResponse.Info {
    record Infos(
            Long id,
            ProductType type,
            String name,
            String category,
            String material,
            Long basePrice,
            Long customPrice,
            Long storeId


    ) implements ProductResponse {

    }

    record Info(
            @NotBlank
            Long id,
            @NotBlank
            ProductType type,
            @NotBlank
            String name,
            @NotBlank
            String category,
            String material,
            Long basePrice,
            Long customPrice,
            String description
    )
            implements ProductResponse {
    }
}
