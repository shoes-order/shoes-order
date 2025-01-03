package com.personal.entity.order;

import com.personal.common.entity.BaseEntity;
import com.personal.entity.store.Store;
import com.personal.entity.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "orders")
public class Orders extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderNo;

    @Column(nullable = false)
    private LocalDate orderDate;

    @Column(nullable = false)
    private String recipi;

    @Column(nullable = false)
    private String tel;

    @Column(nullable = false)
    private String request;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    private Long totalAmt;

    @Column(nullable = false)
    private Long totalTax;

    @Column(nullable = false)
    private String zip;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String addressDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Builder
    public Orders(
            String orderNo ,
            LocalDate orderDate ,
            String recipi ,
            String tel,
            String request,
            OrderStatus orderStatus,
            String paymentMethod,
            Long totalAmt,
            Long totalTax,
            String zip,
            String address,
            String addressDetail,
            User user,
            Store store
            ) {
        this.orderNo = orderNo;
        this.orderDate = orderDate;
        this.recipi = recipi;
        this.tel = tel;
        this.request = request;
        this.orderStatus = orderStatus;
        this.paymentMethod = paymentMethod;
        this.totalAmt = totalAmt;
        this.totalTax = totalTax;
        this.zip = zip;
        this.address = address;
        this.addressDetail = addressDetail;
        this.user = user;
        this.store = store;
    }

    public void updateStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
