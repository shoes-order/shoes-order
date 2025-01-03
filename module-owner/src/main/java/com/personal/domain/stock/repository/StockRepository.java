package com.personal.domain.stock.repository;


import com.personal.entity.stock.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long>, StockDslRepository {



    Optional<Stock> findByProductId(Long productId);
}
