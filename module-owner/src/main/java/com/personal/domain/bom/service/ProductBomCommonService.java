package com.personal.domain.bom.service;

import com.personal.common.code.ResponseCode;
import com.personal.common.exception.custom.ConflictException;
import com.personal.domain.bom.repository.ProductBomRepository;
import com.personal.domain.product.exception.NotFoundProductException;
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


    public void checkForDuplicateBom(Long productId, Long materialProductId) {
        //중복 확인
        boolean exists = productBomRepository.existsBaseAndMaterial(productId,materialProductId);
        if (exists) {
            throw new ConflictException(ResponseCode.DUPLICATE_PRODUCT);
        }
    }

    // 수정할 때 bom값이 같은
    public void checkForDuplicatesBom(Long productId, Long materialProductId, Long bomId) {
        //중복 확인
        boolean exists = productBomRepository.existsBaseAndMaterialAndBomId(productId,materialProductId,bomId);
    }
}
