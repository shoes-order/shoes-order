package com.personal.entity.store;

import com.personal.common.entity.BaseEntity;
import com.personal.entity.order.Orders;
import com.personal.entity.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "store")
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String tel;

    @Column(nullable = false)
    private String zip;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String addressDetail;

    private String description;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "store" , cascade = CascadeType.REMOVE , orphanRemoval = true)
    private List<Orders> orders = new ArrayList<>();

    @Builder
    public Store(String name, String tel, String zip, String address, String addressDetail, String description, User user) {
        this.name = name;
        this.tel = tel;
        this.zip = zip;
        this.address = address;
        this.addressDetail = addressDetail;
        this.description = description;
        this.user = user;
    }

    public void updateInfos(String name, String tel, String zip, String address, String addressDetail, String description) {
        this.name = name;
        this.tel = tel;
        this.zip = zip;
        this.address = address;
        this.addressDetail = addressDetail;
        this.description = description;
    }

    public void updateIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
