package com.personal.domain.outputhistory.repository;

import com.personal.entity.history.OutputHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutputRepository extends JpaRepository<OutputHistory, Long>, OutputDslRepository{
    boolean existsByLot(String newLot);
}
