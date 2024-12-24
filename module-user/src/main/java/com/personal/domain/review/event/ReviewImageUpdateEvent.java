package com.personal.domain.review.event;

import com.personal.entity.review.Review;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ReviewImageUpdateEvent(Review review , List<Long> imageNumber , List<MultipartFile> files) {
}
