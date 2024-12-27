package com.personal.domain.review.service;

import com.personal.common.code.ResponseCode;
import com.personal.common.entity.AuthUser;
import com.personal.common.exception.custom.BadRequestException;
import com.personal.common.exception.custom.ConflictException;
import com.personal.common.exception.custom.ForbiddenException;
import com.personal.common.exception.custom.NotFoundException;
import com.personal.domain.order.service.OrdersCommonService;
import com.personal.domain.review.dto.ReviewRequest;
import com.personal.domain.review.dto.ReviewResponse;
import com.personal.domain.review.event.ReviewImageRemoveEvent;
import com.personal.domain.review.event.ReviewImageSaveEvent;
import com.personal.domain.review.event.ReviewImageUpdateEvent;
import com.personal.domain.review.repository.ReviewRepository;
import com.personal.entity.order.OrderStatus;
import com.personal.entity.order.Orders;
import com.personal.entity.review.Review;
import com.personal.entity.review.ReviewImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReviewService {

    private final ReviewImageCommonService reviewImageCommonService;
    private final ApplicationEventPublisher eventPublisher;
    private final OrdersCommonService ordersCommonService;
    private final ReviewRepository reviewRepository;

    @Transactional
    public void addReview(AuthUser authUser , Long orderId , ReviewRequest.AddReview addReview , List<MultipartFile> files) {
        Orders orders = ordersCommonService.getOrderByOrdersIdAndUserId(orderId , authUser.getUserId());

        // 주문이 완료된 건에 한해서만 리뷰 작성 가능!
        if (!orders.getOrderStatus().equals(OrderStatus.COMPLETED)) {
            throw new BadRequestException(ResponseCode.INVALID_REVIEW_ACCESS);
        }

        // orders 기준으로 조회하여 리뷰가 등록되어있는지 아닌지 검증
        // ※ review가 등록되어 있다면 예외처리
        Review rv = reviewRepository.findByOrdersId(orderId);
        if (Objects.nonNull(rv)) {
            throw new ConflictException(ResponseCode.ALREADY_REGISTERED_REVIEW);
        }

        // 리뷰 테이블 등록
        Review review = Review.builder()
                .title(addReview.title())
                .content(addReview.contents())
                .star(addReview.star())
                .orders(orders)
                .build();
        Review saveReview = reviewRepository.save(review);

        if (Objects.nonNull(files)) {
            eventPublisher.publishEvent(new ReviewImageSaveEvent(saveReview , files));
        }
    }

    @Transactional
    public void modReview(Long orderId , ReviewRequest.ModReview modReview , List<MultipartFile> files) {
        Review review = reviewRepository.findByOrdersId(orderId);
        if (Objects.isNull(review)) {
            throw new NotFoundException(ResponseCode.NOT_FOUND_REVIEW);
        }

        // 리뷰 수정
        review.updateReview(modReview.title(), modReview.contents(), modReview.star());

        // 리뷰 이미지 삭제?
        if (!modReview.modImageNumber().isEmpty() && Objects.nonNull(files)) {
            eventPublisher.publishEvent(new ReviewImageUpdateEvent(review , modReview.modImageNumber() , files));
        }
    }

    @Transactional
    public void removeReview(Long orderId) {
        Review review = reviewRepository.findByOrdersId(orderId);
        if (Objects.isNull(review)) {
            throw new NotFoundException(ResponseCode.NOT_FOUND_REVIEW);
        }

        reviewRepository.delete(review);
        eventPublisher.publishEvent(new ReviewImageRemoveEvent(review));
    }

    public ReviewResponse.Info getReview(AuthUser authUser , Long orderId) {
        Review review = reviewRepository.findByOrdersId(orderId);
        if (!review.getOrders().getUser().getId().equals(authUser.getUserId())) {
            throw new ForbiddenException(ResponseCode.INVALID_REVIEW_ACCESS);
        }
        List<ReviewImage> reviewImages = reviewImageCommonService.getInfos(review);
        List<ReviewResponse.ImageInfo> imageInfos = reviewImages.stream().map(reviewImage -> new ReviewResponse.ImageInfo(reviewImage.getId() , reviewImage.getImageUrl())).toList();
        return new ReviewResponse.Info(review.getTitle() , review.getContent() , review.getStar() , review.getCreatedAt() , review.getUpdatedAt() , imageInfos);
    }

    public Page<ReviewResponse.Info> getMyReviews(AuthUser authUser, ReviewRequest.MyReview myReview) {
        Pageable pageable = PageRequest.of(myReview.page() - 1, myReview.size());
        return reviewRepository.getMyReviews(authUser.getUserId(), myReview , pageable);
    }

    public Page<ReviewResponse.Info> getMyReviewsByStore(AuthUser authUser, Long storeId, ReviewRequest.MyReview myReview) {
        Pageable pageable = PageRequest.of(myReview.page() - 1, myReview.size());
        return reviewRepository.getMyReviewsByStore(authUser.getUserId(), storeId , myReview , pageable);
    }
}
