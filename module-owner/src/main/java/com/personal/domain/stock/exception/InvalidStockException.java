package com.personal.domain.stock.exception;

import com.personal.common.code.ResponseCode;
import com.personal.common.exception.custom.BadRequestException;

public class InvalidStockException extends BadRequestException {
    public InvalidStockException(ResponseCode code) {
        super(code);
    }
}
