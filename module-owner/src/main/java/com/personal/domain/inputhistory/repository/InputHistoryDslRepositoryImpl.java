package com.personal.domain.inputhistory.repository;

import com.personal.domain.inputhistory.dto.InputHistoryResponse;
import com.personal.domain.inputhistory.dto.InputhistoryRequest;
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

import static com.personal.entity.history.QInputHistory.inputHistory;
import static com.personal.entity.product.QProduct.product;

@RequiredArgsConstructor
public class InputHistoryDslRepositoryImpl implements InputHistoryDslRepository {
    private final JPQLQueryFactory queryFactory;

    @Override
    public Page<InputHistoryResponse.GetInfos> getInputHistories(Long storeId, InputhistoryRequest.GetInputHistories getInputHistories, Pageable pageable) {
        List<InputHistoryResponse.GetInfos> results = queryFactory
                .select(Projections.constructor(InputHistoryResponse.GetInfos.class,
                        inputHistory.productId,
                        inputHistory.name,
                        inputHistory.size,
                        inputHistory.qty,
                        inputHistory.price,
                        inputHistory.lot,
                        inputHistory.inputDate
                ))
                .from(inputHistory)
                .where(
                        inputHistory.store.id.eq(storeId),
                        searchInputDate(getInputHistories.startDate(), getInputHistories.endDate()),
                        searchName(getInputHistories.name()),
                        searchType(getInputHistories.type())
                )
                .orderBy(getInputHistories.sort().equals("ASC") ? inputHistory.inputDate.asc() : inputHistory.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        //총 개수 계산
        Long totalCount = queryFactory
                .select(inputHistory.count())
                .from(inputHistory)
                .where(
                        inputHistory.store.id.eq(storeId),
                        searchInputDate(getInputHistories.startDate(), getInputHistories.endDate()),
                        searchName(getInputHistories.name()),
                        searchType(getInputHistories.type())
                )
                .fetchOne();

        return new PageImpl<>(results, pageable, totalCount);
    }


    private BooleanExpression searchInputDate(LocalDate startDate, LocalDate endDate) {
        return startDate != null && endDate != null ? inputHistory.inputDate.between(startDate, endDate) : null;
    }

    private BooleanExpression searchName(String name) {
        return name != null ? inputHistory.name.contains(name) : null;

    }

    private BooleanExpression searchType(ProductType type) {
        return type != null ? inputHistory.type.eq(type) : null;
    }
}
