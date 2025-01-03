package com.personal.domain.store.dto;


import jakarta.validation.constraints.NotBlank;

public sealed interface StoreResponse permits
    StoreResponse.GetStores
{
    record GetStores(
            @NotBlank
            String name,
            @NotBlank
            String tel,
            @NotBlank
            String zip,
            @NotBlank
            String address,
            @NotBlank
            String addressDetail
    )implements StoreResponse{}

}
