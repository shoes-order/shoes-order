package com.personal.domain.inputhistory.repository;

import com.personal.domain.inputhistory.dto.InputHistoryResponse;
import com.personal.domain.inputhistory.dto.InputhistoryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InputHistoryDslRepository {

    Page<InputHistoryResponse.GetInfos> getInputHistories(Long storeId, InputhistoryRequest.GetInputHistories getInputHistories, Pageable pageable);
}
