package com.personal.domain.review.service;

import com.personal.domain.review.dto.ReviewRequest;
import com.personal.domain.review.dto.ReviewResponse;
import com.personal.domain.review.repository.ReviewRepository;
import com.personal.domain.store.service.StoreCommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final StoreCommonService storeCommonService;

    public Page<ReviewResponse.GetInfos> getReviews(ReviewRequest.GetReviews getReviews, Long storeId) {
        storeCommonService.getStores(storeId);
        Pageable pageable = PageRequest.of(getReviews.page() - 1, getReviews.size());

        return reviewRepository.getReviews(storeId,pageable,getReviews);
    }
}
