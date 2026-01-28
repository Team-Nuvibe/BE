package com.umc.nuvibe.domain.image.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PreSignedUrlReq(
        @NotBlank
        String originalFileName
) {
}
