package com.personal.domain.orders.dto;

import java.time.LocalDate;
import java.util.Objects;

public sealed interface OrdersRequest permits
        OrdersRequest.GetOrders
{
    record GetOrders(
            LocalDate startDate ,
            LocalDate endDate ,
            String status ,
            Integer page ,
            Integer size
    ) implements OrdersRequest {
        public GetOrders {
            if(Objects.isNull(page)) page = 1;
            if(Objects.isNull(size)) size = 10;
        }
    }
}
