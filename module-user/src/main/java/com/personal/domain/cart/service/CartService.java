package com.personal.domain.cart.service;

import com.personal.common.code.ResponseCode;
import com.personal.common.entity.AuthUser;
import com.personal.common.exception.custom.BadRequestException;
import com.personal.common.exception.custom.NotFoundException;
import com.personal.domain.cart.dto.CartList;
import com.personal.domain.cart.dto.CartRequest;
import com.personal.domain.cart.dto.CartResponse;
import com.personal.domain.cart.dto.TotalResponse;
import com.personal.domain.cart.repository.CartRepository;
import com.personal.domain.product.service.ProductCommonService;
import com.personal.domain.store.service.StoreCommonService;
import com.personal.domain.user.service.UserCommonService;
import com.personal.entity.cart.Cart;
import com.personal.entity.product.Product;
import com.personal.entity.store.Store;
import com.personal.entity.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CartService {

    private final CartCommonService cartCommonService;
    private final UserCommonService userCommonService;
    private final StoreCommonService storeCommonService;
    private final ProductCommonService productCommonService;
    private final CartRepository cartRepository;

    public CartResponse.GetCart getCarts(AuthUser authUser) {
        return cartCommonService.getCarts(authUser.getUserId());
    }

    @Transactional
    public void addCart(AuthUser authUser , Long storeId , CartRequest.AddCart addCart) {
        // 현재 담겨 있는 장바구니에 다른 매장의 상품이 담겨 있다면 초기화 작업!
        List<Cart> carts = cartRepository.findByUserIdAndStoreId(authUser.getUserId(), storeId);
        if (!carts.isEmpty() && !carts.get(0).getStore().getId().equals(storeId)) {
            emptyCart(authUser);
        }

        User user = userCommonService.getUserById(authUser.getUserId());
        Store store = storeCommonService.getStoreById(storeId);
        Product product = productCommonService.getProductByIdAndStoreId(addCart.productId() , storeId);

        if (Objects.isNull(product)) {
            throw new NotFoundException(ResponseCode.NOT_FOUND_PRODUCT);
        }

        Cart cart = Cart.builder()
                .length(addCart.length())
                .width(addCart.width())
                .qty(addCart.qty())
                .customYN(addCart.customYN())
                .user(user)
                .store(store)
                .product(product)
                .build();

        cartRepository.save(cart);
    }

    @Transactional
    public void modCart(AuthUser authUser , Long storeId , Long cartId , CartRequest.ModCart modCart) {
        Cart cart = cartRepository.findByIdAndUserIdAndStoreId(cartId , authUser.getUserId(), storeId).orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_CART));
        cart.updateCart(modCart.length(), modCart.width(), modCart.qty(), modCart.customYN());
    }

    @Transactional
    public void removeCart(AuthUser authUser , Long storeId , Long cartId) {
        Cart cart = cartRepository.findByIdAndUserIdAndStoreId(cartId , authUser.getUserId(), storeId).orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_CART));
        cartRepository.delete(cart);
    }

    @Transactional
    public void emptyCart(AuthUser authUser) {
        cartCommonService.emptyCart(authUser.getUserId());
    }
}
