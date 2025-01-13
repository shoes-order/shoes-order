package com.personal.domain.store.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public sealed interface StoreRequest permits
        StoreRequest.SaveStores,
        StoreRequest.UpdateStores,
        StoreRequest.GetStores {


    record SaveStores(
            @NotBlank
            String name,
            @NotBlank
            String tel,
            @NotBlank
            String zip,
            @NotBlank
            String address,
            @NotBlank
            String addressDetail,
            @NotBlank
            String description
    ) implements StoreRequest {
    }

    record UpdateStores(
            @NotBlank
            String name,
            @NotBlank
            String tel,
            @NotBlank
            String zip,
            @NotBlank
            String address,
            @NotBlank
            String addressDetail,
            @NotBlank
            String description
    ) implements StoreRequest {

    }

    record GetStores(
            Integer page,
            Integer size
    ) implements StoreRequest {
        public GetStores {
            if (Objects.isNull(page)) page = 1;
            if (Objects.isNull(size)) size = 10;
        }

    }
}

