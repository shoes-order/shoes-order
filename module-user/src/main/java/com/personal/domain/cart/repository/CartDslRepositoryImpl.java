package com.personal.domain.cart.repository;

import com.personal.domain.cart.dto.CartList;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.personal.entity.product.QProduct.product;
import static com.personal.entity.store.QStore.store;
import static com.personal.entity.user.QUser.user;
import static com.personal.entity.cart.QCart.cart;

@Slf4j
@RequiredArgsConstructor
public class CartDslRepositoryImpl implements CartDslRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CartList.Cart> getCarts(Long userId) {

        return jpaQueryFactory
                .select(Projections.constructor(CartList.Cart.class ,
                        store.id,
                        store.name,
                        product.id,
                        product.name,
                        cart.length,
                        cart.width,
                        cart.qty,
                        cart.customyn,
                        product.basePrice,
                        product.customPrice))
                .from(cart)
                .innerJoin(user).on(user.id.eq(cart.user.id))
                .innerJoin(store).on(store.id.eq(cart.store.id))
                .innerJoin(product).on(product.id.eq(cart.product.id))
                .where(user.id.eq(userId))
                .fetch();
    }
}
