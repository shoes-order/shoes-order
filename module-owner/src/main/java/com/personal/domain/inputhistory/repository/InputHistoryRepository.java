package com.personal.domain.inputhistory.repository;

import com.personal.domain.inputhistory.dto.InputHistoryResponse;
import com.personal.domain.inputhistory.dto.InputhistoryRequest;
import com.personal.entity.history.InputHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InputHistoryRepository extends JpaRepository<InputHistory, Long>,InputHistoryDslRepository {


    boolean existsByLot(String newLot);
}
