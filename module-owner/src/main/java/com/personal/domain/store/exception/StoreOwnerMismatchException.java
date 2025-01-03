package com.personal.domain.store.exception;

import com.personal.common.code.ResponseCode;
import com.personal.common.exception.custom.BadRequestException;

public class StoreOwnerMismatchException extends BadRequestException {
    public StoreOwnerMismatchException(ResponseCode code) {
        super(code);
    }
}
