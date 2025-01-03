package com.personal.domain.orders.dto;

import com.personal.entity.order.OrderStatus;

import java.time.LocalDate;
import java.util.List;

public sealed interface OrdersResponse permits
            OrdersResponse.OrdersInfo ,
            OrdersResponse.OrdersDetailInfo
{
    record OrdersInfo (
            Long orderId,
            Long storeId,
            String storeName,
            String orderNo,
            LocalDate orderDate,
            String recipi,
            String tel,
            String request,
            OrderStatus orderStatus,
            String paymentMethod,
            Long totalAmt,
            Long totalTax,
            String zip,
            String address,
            String addressDetail,
            List<OrdersDetailInfo> ordersDetails
    ) implements OrdersResponse {
    }

    record OrdersDetailInfo(
            Long productId,
            String productName,
            Long length,
            Long width,
            Long qty,
            Boolean customyn,
            Long customPrice,
            Long basePrice,
            Long amt,
            Long tax
    ) implements OrdersResponse {
    }
}
