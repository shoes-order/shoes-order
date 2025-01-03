package com.personal.domain.bom.service;

import com.personal.common.code.ResponseCode;
import com.personal.domain.product.exception.NotFoundProductException;
import com.personal.domain.bom.repository.ProductBomRepository;
import com.personal.entity.product.ProductBom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductBomCommonService {
    private final ProductBomRepository productBomRepository;

    public ProductBom getBoms(Long bomId) {
        return productBomRepository.findById(bomId)
                .orElseThrow(() -> new NotFoundProductException(ResponseCode.NOT_FOUND_PRODUCTBOM));
    }
}
