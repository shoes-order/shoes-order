package com.personal.domain.cart.dto;

import jakarta.validation.constraints.NotNull;

public sealed interface CartRequest permits
    CartRequest.AddCart,
    CartRequest.ModCart
{
    record AddCart(
            @NotNull
            Long productId,
            Long length,
            Long width,
            @NotNull
            Long qty,
            boolean customyn
    ) implements CartRequest {
    }

    record ModCart(
            Long length,
            Long width,
            @NotNull
            Long qty,
            boolean customyn
    ) implements CartRequest {
    }
}
