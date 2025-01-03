package com.personal.domain.shippingstatus.repository;

import com.personal.common.entity.SuccessResponse;
import com.personal.domain.shippingstatus.dto.ShippingStatusRequest;
import com.personal.domain.shippingstatus.dto.ShippingStatusResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

import static com.personal.entity.ship.QShippingStatus.shippingStatus1;

@RequiredArgsConstructor
public class ShippingStatusDslRepositoryImpl implements ShippingStatusDslRepository {
    private final JPQLQueryFactory queryFactory;

    @Override
    public Page<ShippingStatusResponse.Infos> getShippingStatus(Pageable pageable, ShippingStatusRequest.GetShippingStatus getShippingStatus) {
        List<ShippingStatusResponse.Infos> results = queryFactory
                .select(Projections.constructor(ShippingStatusResponse.Infos.class,
                        shippingStatus1.shippingStatus,
                        shippingStatus1.shipStartDate,
                        shippingStatus1.shipCompleteDate,
                        shippingStatus1.trackingNumber,
                        shippingStatus1.carrier,
                        shippingStatus1.createdAt,
                        shippingStatus1.updatedAt
                ))
                .from(shippingStatus1)
                .where(
                        searchOrderDate(getShippingStatus.startDate(), getShippingStatus.endDate())
                )
                .orderBy(shippingStatus1.updatedAt.desc())
                .offset(getShippingStatus.page())
                .limit(getShippingStatus.size())
                .fetch();

        Long totalCount = queryFactory
                .select(shippingStatus1.count())
                .from(shippingStatus1)
                .where(
                        searchOrderDate(getShippingStatus.startDate(), getShippingStatus.endDate())
                )
                .fetchOne();
        return new PageImpl<>(results,pageable,totalCount);
    }

    //배송 시작일을 기준으로 검색
    private BooleanExpression searchOrderDate(LocalDate startDate, LocalDate endDate) {
        return startDate != null && endDate != null ? shippingStatus1.shipStartDate.between(startDate, endDate) : null;
    }
}
