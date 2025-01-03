package com.personal.domain.product.exception;

import com.personal.common.code.ResponseCode;
import com.personal.common.exception.custom.BadRequestException;

public class NotFoundProductException extends BadRequestException {
    public NotFoundProductException(ResponseCode code) {
        super(code);
    }
}
