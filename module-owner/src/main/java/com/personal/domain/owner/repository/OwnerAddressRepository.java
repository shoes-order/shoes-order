package com.personal.domain.owner.repository;

import com.personal.entity.user.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerAddressRepository extends JpaRepository<UserAddress, Long> {


    Optional<UserAddress> findByUserIdAndRepYNTrue(Long userId);
}
