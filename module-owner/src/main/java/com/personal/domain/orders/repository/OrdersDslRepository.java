package com.personal.domain.orders.repository;

import com.personal.domain.orders.dto.OrdersRequest;
import com.personal.domain.orders.dto.OrdersResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrdersDslRepository {
    Page<OrdersResponse.OrdersInfo> getOrders(Long userId , Long storeId , OrdersRequest.GetOrders getOrders , Pageable pageable);
}
