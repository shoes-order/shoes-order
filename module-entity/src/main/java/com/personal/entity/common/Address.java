package com.personal.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {
    @Column(nullable = false)
    private String zip;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String addressDetail;
}
