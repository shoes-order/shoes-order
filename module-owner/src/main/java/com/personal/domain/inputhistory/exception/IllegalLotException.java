package com.personal.domain.inputhistory.exception;

import com.personal.common.code.ResponseCode;
import com.personal.common.exception.custom.BadRequestException;

public class IllegalLotException extends BadRequestException {
    public IllegalLotException(ResponseCode code) {
        super(code);
    }
}
