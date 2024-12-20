package com.personal.domain.owner.dto;

import jakarta.validation.constraints.NotBlank;

public sealed interface OwnerRequest permits
        OwnerRequest.Login ,
        OwnerRequest.Register
{
    record Login(
            @NotBlank
            String email,
            @NotBlank
            String password
    ) implements OwnerRequest {
    }

    record Register(
            @NotBlank
            String email,
            @NotBlank
            String password,
            @NotBlank
            String name
    ) implements OwnerRequest {
    }
}