package com.personal.domain.bom.repository;


import com.personal.entity.product.ProductBom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductBomRepository extends JpaRepository<ProductBom, Long>, ProductBomDslRepository {


}
