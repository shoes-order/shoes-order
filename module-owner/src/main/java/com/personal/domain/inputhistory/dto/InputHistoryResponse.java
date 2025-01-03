package com.personal.domain.inputhistory.dto;


import java.time.LocalDate;

public sealed interface InputHistoryResponse permits
        InputHistoryResponse.GetInfos {
    record GetInfos(
            Long productId,
            String name,
            Long size,
            Long qty,
            Long price,
            String lot,
            LocalDate inputDate
    )
            implements InputHistoryResponse {

    }
}
