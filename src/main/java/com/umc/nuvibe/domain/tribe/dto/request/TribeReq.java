package com.umc.nuvibe.domain.tribe.dto.request;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import jakarta.validation.constraints.NotBlank;

public class TribeReq {

    public record JoinReq(
            @NotBlank
            String imageTag
    ){}
}
