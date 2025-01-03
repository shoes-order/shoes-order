package com.personal.domain.orders.service;

import com.personal.common.code.ResponseCode;
import com.personal.common.entity.AuthUser;
import com.personal.common.exception.custom.BadRequestException;
import com.personal.domain.orders.dto.OrdersRequest;
import com.personal.domain.orders.dto.OrdersResponse;
import com.personal.domain.orders.repository.OrdersRepository;
import com.personal.entity.order.OrderStatus;
import com.personal.entity.order.Orders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrdersService {
    private final OrdersRepository ordersRepository;

    public Page<OrdersResponse.OrdersInfo> getOrders(AuthUser authUser , Long storeId , OrdersRequest.GetOrders getOrders) {
        Pageable pageable = PageRequest.of(getOrders.page() - 1, getOrders.size());
        return ordersRepository.getOrders(authUser.getUserId() , storeId , getOrders , pageable);
    }

    @Transactional
    public void orderStatusChange(AuthUser authUser , Long storeId , Long orderId) {
        // 상태 변경 로직!
        Orders orders = ordersRepository.findByOrderIdAndStoreIdAndUserId(orderId , storeId , authUser.getUserId());

        switch (orders.getOrderStatus()) {
            case PENDING -> apply(orders);
            case APPROVED -> processing(orders);
            case PROCESSING -> shipping(orders);
            case SHIPPING -> complete(orders);
            default -> throw new BadRequestException(ResponseCode.INVALID_ORDER_ACCESS);
        }

        ordersRepository.save(orders);
    }

    @Transactional
    public void orderCancel(AuthUser authUser , Long storeId , Long orderId) {
        Orders orders = ordersRepository.findByOrderIdAndStoreIdAndUserId(orderId , storeId , authUser.getUserId());

        switch (orders.getOrderStatus()) {
            case PENDING, APPROVED -> orders.updateStatus(OrderStatus.CANCELLED);
            default -> throw new BadRequestException(ResponseCode.INVALID_ORDER_ACCESS);
        }

        ordersRepository.save(orders);
    }

    private void apply(Orders orders) {
        // 승인 로직

        orders.updateStatus(OrderStatus.APPROVED);
    }

    private void processing(Orders orders) {
        // 생산 되기전 검증 로직

        orders.updateStatus(OrderStatus.PROCESSING);
    }

    private void shipping(Orders orders) {
        // 배송으로 하기 전에 검증해야하는 로직(현재로는 없음)

        orders.updateStatus(OrderStatus.SHIPPING);
    }

    private void complete(Orders orders) {
        // 주문 완료하기 전에 검증해야하는 로직(현재로는 없음)

        orders.updateStatus(OrderStatus.COMPLETED);
    }
}
