package com.personal.domain.stock.exception;

import com.personal.common.code.ResponseCode;
import com.personal.common.exception.custom.BadRequestException;

public class NotFoundStockException extends BadRequestException {
    public NotFoundStockException(ResponseCode code) {
        super(code);
    }
}
