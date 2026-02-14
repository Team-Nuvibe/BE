package com.umc.nuvibe.domain.image.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

// lambda에서 sqs로 보내는 커스텀 메세지로 변경
public record S3EventDto(
        @JsonProperty("fileId") String fileId,
        @JsonProperty("originalUrl") String originalUrl,
        @JsonProperty("thumbnailUrl") String thumbnailUrl,
        @JsonProperty("status") String status
) {}

