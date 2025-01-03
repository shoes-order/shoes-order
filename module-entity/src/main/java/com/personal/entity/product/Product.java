package com.personal.entity.product;

import com.personal.common.entity.BaseEntity;
import com.personal.entity.store.Store;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "product")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ProductType type;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    private String material;

    @Column(nullable = false)
    private Long spacing;

    @Column(nullable = false)
    private Long basePrice;

    @Column(nullable = false)
    private Long customPrice;

    @Column(nullable = false)
    private boolean isSold = true;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Builder
    public Product(Store store, ProductType type, String name, String category, String material, Long spacing, Long basePrice, Long customPrice, String description) {
        this.store = store;
        this.type = type;
        this.name = name;
        this.category = category;
        this.material = material;
        this.spacing = spacing;
        this.basePrice = basePrice;
        this.customPrice = customPrice;
        this.description = description;

    }

    public void updateProducts(ProductType type, String name, String category, String material, Long spacing, Long basePrice, Long customPrice, String description) {
        this.type = type;
        this.name = name;
        this.category = category;
        this.material = material;
        this.spacing = spacing;
        this.basePrice = basePrice;
        this.customPrice = customPrice;
        this.description = description;

    }


    public void updateIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
