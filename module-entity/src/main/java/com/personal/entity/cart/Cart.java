package com.personal.entity.cart;

import com.personal.entity.product.Product;
import com.personal.entity.store.Store;
import com.personal.entity.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long length;

    @Column(nullable = false)
    private Long width;

    @Column(nullable = false)
    private Long qty;

    @Column(nullable = false)
    private boolean customyn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" , nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id" , nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id" , nullable = false)
    private Product product;

    @Builder
    public Cart(
            Long length,
            Long width,
            Long qty,
            boolean customyn,
            User user,
            Store store,
            Product product) {
        this.length = length;
        this.width = width;
        this.qty = qty;
        this.customyn = customyn;
        this.user = user;
        this.store = store;
        this.product = product;
    }

    public void updateCart(Long length ,Long width , Long qty , boolean customyn) {
        this.length = length;
        this.width = width;
        this.qty = qty;
        this.customyn = customyn;
    }
}
