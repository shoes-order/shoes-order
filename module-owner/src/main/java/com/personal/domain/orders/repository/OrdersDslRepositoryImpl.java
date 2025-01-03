package com.personal.domain.orders.repository;

import com.personal.domain.orders.dto.OrdersRequest;
import com.personal.domain.orders.dto.OrdersResponse;
import com.personal.domain.review.dto.ReviewResponse;
import com.personal.entity.order.OrderStatus;
import com.personal.entity.order.Orders;
import com.personal.entity.review.Review;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.personal.entity.product.QProduct.product;
import static com.personal.entity.user.QUser.user;
import static com.personal.entity.store.QStore.store;
import static com.personal.entity.order.QOrders.orders;
import static com.personal.entity.order.QOrdersDetail.ordersDetail;

@Slf4j
@RequiredArgsConstructor
public class OrdersDslRepositoryImpl implements OrdersDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<OrdersResponse.OrdersInfo> getOrders(Long userId, Long storeId, OrdersRequest.GetOrders getOrders, Pageable pageable) {
        List<Orders> ordersList = queryFactory
                .selectFrom(orders)
                .innerJoin(store).on(store.id.eq(orders.store.id))
                .innerJoin(user).on(user.id.eq(store.user.id))
                .where(
                        user.id.eq(userId),
                        store.id.eq(storeId),
                        searchStartOrderDate(getOrders.startDate()) ,
                        searchEndOrderDate(getOrders.endDate()) ,
                        searchOrderStatus(getOrders.status())
                )
                .orderBy(orders.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Long> ordersIds = ordersList.stream().map(Orders::getId).toList();

        if (ordersIds.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        Map<Long, List<OrdersResponse.OrdersDetailInfo>> map = queryFactory
                .select(Projections.constructor(OrdersResponse.OrdersDetailInfo.class ,
                        product.id,
                        ordersDetail.productName,
                        ordersDetail.length,
                        ordersDetail.width,
                        ordersDetail.qty,
                        ordersDetail.customYN,
                        ordersDetail.customPrice,
                        ordersDetail.basePrice,
                        ordersDetail.amt,
                        ordersDetail.tax
                ))
                .from(ordersDetail)
                .innerJoin(product).on(product.id.eq(ordersDetail.product.id)).fetchJoin()
                .where(ordersDetail.orders.id.in(ordersIds))
                .transform(GroupBy.groupBy(ordersDetail.orders.id).as(GroupBy.list(
                        Projections.constructor(OrdersResponse.OrdersDetailInfo.class,
                                product.id,
                                ordersDetail.productName,
                                ordersDetail.length,
                                ordersDetail.width,
                                ordersDetail.qty,
                                ordersDetail.customYN,
                                ordersDetail.customPrice,
                                ordersDetail.basePrice,
                                ordersDetail.amt,
                                ordersDetail.tax)
                )));

        List<OrdersResponse.OrdersInfo> result = ordersList.stream()
                .map(o -> new OrdersResponse.OrdersInfo(
                        o.getId(),
                        o.getStore().getId(),
                        o.getStore().getName(),
                        o.getOrderNo(),
                        o.getOrderDate(),
                        o.getRecipi(),
                        o.getTel(),
                        o.getRequest(),
                        o.getOrderStatus(),
                        o.getPaymentMethod(),
                        o.getTotalAmt(),
                        o.getTotalTax(),
                        o.getZip(),
                        o.getAddress(),
                        o.getAddressDetail(),
                        map.getOrDefault(o.getId() , Collections.emptyList())
                )).toList();

        Long totalCount = queryFactory
                .select(orders.count())
                .from(orders)
                .innerJoin(store).on(store.id.eq(orders.store.id))
                .innerJoin(user).on(user.id.eq(store.user.id))
                .where(
                        user.id.eq(userId),
                        store.id.eq(storeId),
                        searchStartOrderDate(getOrders.startDate()) ,
                        searchEndOrderDate(getOrders.endDate()) ,
                        searchOrderStatus(getOrders.status())
                )
                .fetchOne();

        return new PageImpl<>(result, pageable, totalCount);
    }

    /**
     * 주문 기간 조건문 시작일
     * */
    private BooleanExpression searchStartOrderDate(LocalDate startDate) {
        return startDate != null ? orders.orderDate.goe(startDate) : null;
    }

    /**
     * 주문 기간 조건문 종료일
     * */
    private BooleanExpression searchEndOrderDate(LocalDate endDate) {
        return endDate != null ? orders.orderDate.loe(endDate) : null;
    }

    /**
     * 주문 상태 조건문
     * */
    private BooleanExpression searchOrderStatus(String status) {
        return status != null ? orders.orderStatus.eq(OrderStatus.of(status)) : null;
    }
}
