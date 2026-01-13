package com.umc.nuvibe.domain.tribe.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TribeJoinReq (
        @NotBlank
        String imageTag
){}
