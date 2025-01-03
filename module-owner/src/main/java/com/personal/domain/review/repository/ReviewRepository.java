package com.personal.domain.review.repository;

import com.personal.entity.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewDslRepository {
}
