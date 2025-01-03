package com.personal.domain.cart.service;

import com.personal.common.entity.AuthUser;
import com.personal.domain.cart.dto.CartList;
import com.personal.domain.cart.dto.CartResponse;
import com.personal.domain.cart.dto.TotalResponse;
import com.personal.domain.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CartCommonService {
    private final CartRepository cartRepository;

    public CartResponse.GetCart getCarts(Long userId) {
        List<CartList.Cart> cartList = cartRepository.getCarts(userId);

        Long sum = 0L;
        for (CartList.Cart cart : cartList) {
            sum += cart.basePrice();
            if (cart.customyn()) sum += cart.customPrice();
        }
        TotalResponse.TotalAmt totalAmt = new TotalResponse.TotalAmt(sum , 0L);

        return new CartResponse.GetCart(cartList, totalAmt);
    }

    @Transactional
    public void emptyCart(Long userId) {
        cartRepository.deleteByUserId(userId);
    }
}
