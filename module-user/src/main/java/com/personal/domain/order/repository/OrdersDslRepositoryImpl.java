package com.personal.domain.order.repository;

import com.personal.domain.order.dto.OrderRequest;
import com.personal.domain.order.dto.OrderResponse;
import com.personal.entity.order.OrderStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

import static com.personal.entity.user.QUser.user;
import static com.personal.entity.order.QOrders.orders;
import static com.personal.entity.order.QOrdersDetail.ordersDetail;

@Slf4j
@RequiredArgsConstructor
public class OrdersDslRepositoryImpl implements OrdersDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<OrderResponse.Infos> getOrders(Long userId, OrderRequest.GetOrder getOrder, Pageable pageable) {

        JPQLQuery<Long> ordersDetailCnt = JPAExpressions
                .select(ordersDetail.count())
                .from(ordersDetail)
                .where(ordersDetail.orders.id.eq(orders.id));

        List<OrderResponse.Infos> results = queryFactory
                .select(Projections.constructor(OrderResponse.Infos.class ,
                        orders.id,
                        orders.orderNo,
                        orders.orderDate,
                        orders.recipi,
                        orders.tel,
                        orders.request,
                        orders.zip,
                        orders.address,
                        orders.addressDetail,
                        orders.orderStatus,
                        orders.totalAmt,
                        orders.totalTax,
                        ordersDetailCnt
                ))
                .from(orders)
                .innerJoin(user).on(user.id.eq(orders.user.id))
                .where(
                        user.id.eq(userId),
                        searchStartOrderDate(getOrder.startDate()),
                        searchEndOrderDate(getOrder.endDate()),
                        searchOrderStatus(getOrder.status())
                )
                .orderBy(orders.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory
                .select(orders.count())
                .from(orders)
                .innerJoin(user).on(user.id.eq(orders.user.id))
                .where(
                        user.id.eq(userId),
                        searchStartOrderDate(getOrder.startDate()),
                        searchEndOrderDate(getOrder.endDate()),
                        searchOrderStatus(getOrder.status())
                )
                .fetchOne();

        return new PageImpl<>(results, pageable, totalCount);
    }

    private BooleanExpression searchStartOrderDate(LocalDate startDate) {
        return startDate != null ? orders.orderDate.goe(startDate) : null;
    }

    private BooleanExpression searchEndOrderDate(LocalDate endDate) {
        return endDate != null ? orders.orderDate.loe(endDate) : null;
    }

    private BooleanExpression searchOrderStatus(String status) {
        return status != null ? orders.orderStatus.eq(OrderStatus.of(status)) : null;
    }
}
