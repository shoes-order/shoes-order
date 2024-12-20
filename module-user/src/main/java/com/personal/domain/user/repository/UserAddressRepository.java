package com.personal.domain.user.repository;

import com.personal.entity.user.User;
import com.personal.entity.user.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    Optional<UserAddress> findByIdAndRepYNTrue(Long id, boolean repYN);
    boolean existsByUserAndRepYNTrue(User user, boolean repYN);
}