package com.umc.nuvibe.domain.tribe.dto.request;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import jakarta.validation.constraints.NotNull;


public record TribeJoinReq (
        @NotNull
        ImageTag imageTag
){}
