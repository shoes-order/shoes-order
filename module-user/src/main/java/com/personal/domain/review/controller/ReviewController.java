package com.personal.domain.review.controller;

import com.personal.common.entity.AuthUser;
import com.personal.common.entity.SuccessResponse;
import com.personal.common.enums.UserRole;
import com.personal.domain.review.dto.ReviewRequest;
import com.personal.domain.review.dto.ReviewResponse;
import com.personal.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Secured({UserRole.Authority.USER})
@RestController
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * 리뷰 등록
     * */
    @PostMapping("/orders/{orderId}/review")
    public ResponseEntity<SuccessResponse<Void>> addReview(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long orderId,
            @Valid @ModelAttribute ReviewRequest.AddReview addReview,
            @RequestParam(name = "images" , required = false) List<MultipartFile> files
    ) {
        reviewService.addReview(authUser , orderId , addReview , files);
        return ResponseEntity.ok().body(SuccessResponse.of(null));
    }

    /**
     * 리뷰 수정
     * */
    @PatchMapping("/orders/{orderId}/review")
    public ResponseEntity<SuccessResponse<Void>> modReview(
            @PathVariable Long orderId,
            @Valid @ModelAttribute ReviewRequest.ModReview modReview,
            @RequestParam(name = "images" , required = false) List<MultipartFile> files
    ) {
        reviewService.modReview(orderId, modReview, files);
        return ResponseEntity.ok().body(SuccessResponse.of(null));
    }

    /**
     * 리뷰 삭제
     * */
    @DeleteMapping("/orders/{orderId}/review")
    public ResponseEntity<SuccessResponse<Void>> removeReview(
            @PathVariable Long orderId
    ) {
        reviewService.removeReview(orderId);
        return ResponseEntity.ok().body(SuccessResponse.of(null));
    }

    /**
     * 리뷰 단일 조회
     * */
    @GetMapping("/orders/{orderId}/review")
    public ResponseEntity<SuccessResponse<ReviewResponse.Info>> getReview(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long orderId
    ) {
        return ResponseEntity.ok().body(SuccessResponse.of(reviewService.getReview(authUser , orderId)));
    }

    /**
     * 리뷰 조회(내기준)
     * */
    @GetMapping("/review")
    public ResponseEntity<SuccessResponse<Page<ReviewResponse.Info>>> getMyReviews(
            @AuthenticationPrincipal AuthUser authUser,
            @ModelAttribute ReviewRequest.MyReview myReview
    ) {
        return ResponseEntity.ok().body(SuccessResponse.of(reviewService.getMyReviews(authUser , myReview)));
    }

    /**
     * 리뷰 조회(가게기준)
     * */
    @GetMapping("/stores/{storeId}/review")
    public ResponseEntity<SuccessResponse<Page<ReviewResponse.Info>>> getMyReviewsByStore(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long storeId,
            @ModelAttribute ReviewRequest.MyReview myReview
    ) {
        return ResponseEntity.ok().body(SuccessResponse.of(reviewService.getMyReviewsByStore(authUser , storeId , myReview)));
    }
}