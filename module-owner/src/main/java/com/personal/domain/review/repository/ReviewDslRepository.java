package com.personal.domain.review.repository;


import com.personal.domain.review.dto.ReviewRequest;
import com.personal.domain.review.dto.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewDslRepository {
    Page<ReviewResponse.GetInfos> getReviews(Long storeId, Pageable pageable, ReviewRequest.GetReviews getReviews);
}
