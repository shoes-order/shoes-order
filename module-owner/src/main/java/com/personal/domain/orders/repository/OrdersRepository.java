package com.personal.domain.orders.repository;

import com.personal.entity.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrdersRepository extends JpaRepository<Orders, Long> , OrdersDslRepository {
    @Query("select o from Orders o join fetch o.store s where o.id = :orderId and s.id = :storeId and s.user.id = :userId")
    Orders findByOrderIdAndStoreIdAndUserId(Long orderId, Long storeId , Long userId);
}
