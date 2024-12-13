package com.personal.entity.history;

import com.personal.common.entity.BaseEntity;
import com.personal.entity.product.ProductType;
import com.personal.entity.store.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
@Table(
        name = "input_history",
        indexes = {
                @Index(name = "idx_input_history_product_id", columnList = "productId")
        }
)
public class InputHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ProductType type;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false)
    private Long qty;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false , unique = true)
    private String lot;

    @Column(nullable = false)
    private LocalDate inputDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id" , nullable = false)
    private Store store;
}
