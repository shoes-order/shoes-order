package com.personal.domain.product.repository;


import com.personal.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductDslRepository {


    @Query("select p from Product p where p.store.id=:storeId and p.id =:productId")
    Optional<Product> getStoreProduct(@Param("storeId") Long storeId, @Param("productId") Long productId);
}
