package com.personal.domain.product.exception;

import com.personal.common.code.ResponseCode;
import com.personal.common.exception.custom.BadRequestException;

public class ForBiddenProductsException extends BadRequestException {
    public ForBiddenProductsException(ResponseCode code) {
        super(code);
    }
}
