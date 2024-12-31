package com.personal.domain.cart.dto;

public sealed interface CartList permits
        CartList.Cart
{
    record Cart(
        Long storeId,
        String storeName,
        Long productId,
        String productName,
        Long length,
        Long width,
        Long qty,
        boolean customYN,
        Long basePrice,
        Long customPrice
    ) implements CartList {
    }
}
