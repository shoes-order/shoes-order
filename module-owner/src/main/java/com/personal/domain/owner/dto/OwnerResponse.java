package com.personal.domain.owner.dto;

public sealed interface OwnerResponse permits
        OwnerResponse.GetProfile {
    record GetProfile(
            String email,
            String name
    ) implements OwnerResponse {
    }
}
