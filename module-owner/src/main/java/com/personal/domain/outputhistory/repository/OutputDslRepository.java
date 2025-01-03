package com.personal.domain.outputhistory.repository;

import com.personal.domain.outputhistory.dto.OutputRequest;
import com.personal.domain.outputhistory.dto.OutputResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface OutputDslRepository {
    Page<OutputResponse.GetInfos> getOutputs(Pageable pageable, Long storeId, OutputRequest.GetOutputs getOutputs);
}
