package com.personal.entity.review;

import com.personal.common.entity.BaseEntity;
import com.personal.entity.order.Orders;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "review")
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double star;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders orders;

    @OneToMany(mappedBy = "review" , cascade = CascadeType.REMOVE , orphanRemoval = true)
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @Builder
    public Review(String title, String content , Double star, Orders orders) {
        this.title = title;
        this.content = content;
        this.star = star;
        this.orders = orders;
    }

    public void updateReview(String title, String content , Double star) {
        this.title = title;
        this.content = content;
        this.star = star;
    }
}
