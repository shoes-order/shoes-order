package com.personal.domain.review.dto;

import java.time.LocalDate;
import java.util.Objects;

public sealed interface ReviewRequest permits
        ReviewRequest.GetReviews {
    record GetReviews(
            Integer page,
            Integer size,
            String sort,
            Double star,
            LocalDate startDate,
            LocalDate endDate
    )
            implements ReviewRequest {
        public GetReviews {
            if (Objects.isNull(page)) page = 1;
            if (Objects.isNull(size)) size = 10;

        }
    }
}

