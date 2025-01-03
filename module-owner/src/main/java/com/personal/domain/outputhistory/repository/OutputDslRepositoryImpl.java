package com.personal.domain.outputhistory.repository;

import com.personal.domain.outputhistory.dto.OutputRequest;
import com.personal.domain.outputhistory.dto.OutputResponse;
import com.personal.entity.product.ProductType;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

import static com.personal.entity.history.QOutputHistory.outputHistory;

@RequiredArgsConstructor
public class OutputDslRepositoryImpl implements OutputDslRepository {
    private final JPQLQueryFactory queryFactory;

    @Override
    public Page<OutputResponse.GetInfos> getOutputs(Pageable pageable, Long storeId, OutputRequest.GetOutputs getOutputs) {

        List<OutputResponse.GetInfos> results = queryFactory
                .select(Projections.constructor(OutputResponse.GetInfos.class,
                        outputHistory.productId,
                        outputHistory.name,
                        outputHistory.size,
                        outputHistory.qty,
                        outputHistory.price,
                        outputHistory.lot,
                        outputHistory.outputDate

                ))
                .from(outputHistory)
                .where(
                        outputHistory.store.id.eq(storeId),
                        searchOutputDate(getOutputs.startDate(), getOutputs.endDate()),
                        searchName(getOutputs.name()),
                        searchType(getOutputs.type())
                )
                .orderBy(getOutputs.sort().equals("asc") ? outputHistory.outputDate.asc() : outputHistory.outputDate.desc())
                .fetch();
        Long totalcount = queryFactory
                .select(outputHistory.count())
                .from(outputHistory)
                .where(
                        outputHistory.store.id.eq(storeId),
                        searchOutputDate(getOutputs.startDate(), getOutputs.endDate()),
                        searchName(getOutputs.name()),
                        searchType(getOutputs.type())
                )
                .fetchOne();

        return new PageImpl<>(results, pageable, totalcount);
    }

    private BooleanExpression searchOutputDate(LocalDate startDate, LocalDate endDate) {
        return startDate != null && endDate != null ? outputHistory.outputDate.between(startDate, endDate) : null;

    }

    private BooleanExpression searchName(String name) {
        return name != null ? outputHistory.name.contains(name) : null;

    }

    private BooleanExpression searchType(ProductType type) {
        return type != null ? outputHistory.type.eq(type) : null;
    }


}
