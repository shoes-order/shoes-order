package com.personal.domain.review.repository;

import com.personal.domain.review.dto.ReviewRequest;
import com.personal.domain.review.dto.ReviewResponse;
import com.personal.entity.review.Review;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.personal.entity.review.QReview.review;
import static com.personal.entity.review.QReviewImage.reviewImage;

@RequiredArgsConstructor
public class ReviewDslRepositoryImpl implements ReviewDslRepository {
    private final JPQLQueryFactory queryFactory;

    @Override
    public Page<ReviewResponse.GetInfos> getReviews(Long storeId, Pageable pageable, ReviewRequest.GetReviews getReviews) {
        List<Review> reviews = fetchReviews(storeId, pageable, getReviews);

        // 리뷰 ID 추출
        List<Long> reviewIds = reviews.stream().map(Review::getId).toList();
        if (reviewIds.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        // 이미지 Map으로 관리(key,value)
        Map<Long, List<ReviewResponse.ImageInfo>> reviewImagesMap = fetchReviewImages(reviewIds);

        // 리뷰, 이미지 dto로 매핑
        List<ReviewResponse.GetInfos> responseList = mapReviewsToResponse(reviews, reviewImagesMap);

        Long totalCount = fetchTotalCount(storeId, getReviews);

        return new PageImpl<>(responseList, pageable, totalCount);


    }

    private List<ReviewResponse.GetInfos> mapReviewsToResponse(List<Review> reviews, Map<Long, List<ReviewResponse.ImageInfo>> reviewImagesMap) {
        return reviews.stream()
                .map(r -> new ReviewResponse.GetInfos(
                        r.getOrders().getUser().getName(),
                        r.getStar(),
                        r.getTitle(),
                        //null값 처리
                        reviewImagesMap.getOrDefault(r.getId(), Collections.emptyList()),
                        r.getContent(),
                        r.getCreatedAt().toLocalDate(),
                        r.getUpdatedAt().toLocalDate()
                )).toList();
    }

    /**
     * ReviewId 기반으로 ReviewImage 조회 (
     */
    private Map<Long, List<ReviewResponse.ImageInfo>> fetchReviewImages(List<Long> reviewIds) {
        return queryFactory
                .select(Projections.constructor(ReviewResponse.ImageInfo.class,
                        reviewImage.review.id,
                        reviewImage.imageUrl
                ))
                .from(reviewImage)
                .where(reviewImage.review.id.in(reviewIds))
                .transform(GroupBy.groupBy(reviewImage.review.id).as(GroupBy.list(
                        Projections.constructor(ReviewResponse.ImageInfo.class,
                                reviewImage.id,
                                reviewImage.imageUrl
                        )
                )));
    }

    /**
     * 리뷰 기준 총 개수 구하기
     */
    private Long fetchTotalCount(Long storeId, ReviewRequest.GetReviews getReviews) {
        return queryFactory
                .select(review.count())
                .from(review)
                .where(
                        storeId != null ? review.orders.store.id.eq(storeId) : null,
                        starCondition(getReviews.star()),
                        searchOrderDate(getReviews.startDate(), getReviews.endDate())
                )
                .fetchOne();
    }

    /**
     * 리뷰만 반환
     */
    private List<Review> fetchReviews(Long storeId, Pageable pageable, ReviewRequest.GetReviews getReviews) {
        return queryFactory
                .selectFrom(review)
                .where(
                        storeId != null ? review.orders.store.id.eq(storeId) : null,
                        starCondition(getReviews.star()),
                        searchOrderDate(getReviews.startDate(), getReviews.endDate())
                )
                .orderBy(review.updatedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression searchOrderDate(LocalDate startDate, LocalDate endDate) {
        return startDate != null && endDate != null ?
                review.createdAt.between
                        (startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX)) : null;
    }

    private BooleanExpression starCondition(Double star) {
        return star != null ? review.star.eq(star) : null;
    }
}
