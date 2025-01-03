package com.personal.domain.bom.repository;


import com.personal.entity.product.ProductBom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductBomRepository extends JpaRepository<ProductBom, Long>, ProductBomDslRepository {


}
