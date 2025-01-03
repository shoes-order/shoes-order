package com.personal.domain.shippingstatus.repository;

import com.personal.entity.ship.ShippingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingStatusRepository extends JpaRepository<ShippingStatus, Long> , ShippingStatusDslRepository {
}
