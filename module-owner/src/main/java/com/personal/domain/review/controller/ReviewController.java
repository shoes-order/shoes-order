package com.personal.domain.review.controller;

import com.personal.common.entity.SuccessResponse;
import com.personal.common.enums.UserRole;
import com.personal.domain.review.dto.ReviewRequest;
import com.personal.domain.review.dto.ReviewResponse;
import com.personal.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores")
@Secured({UserRole.Authority.OWNER})
public class ReviewController {
    private final ReviewService reviewService;

    /**
     *
     * @param getReviews startDate,endDate,star
     * @param storeId
     * @return
     */
    @GetMapping("/{storeId}/review")
    public ResponseEntity<SuccessResponse<Page<ReviewResponse.GetInfos>>> getReviews(
            @ModelAttribute ReviewRequest.GetReviews getReviews,
            @PathVariable Long storeId
    ) {
        return ResponseEntity.ok()
                .body(SuccessResponse.of(reviewService.getReviews(getReviews, storeId)));
    }
}
