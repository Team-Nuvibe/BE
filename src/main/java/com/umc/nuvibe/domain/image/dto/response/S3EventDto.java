package com.umc.nuvibe.domain.image.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record S3EventDto(
        @JsonProperty("Records") List<S3Record> records
) {
    // 내부 레코드 구조
    public record S3Record(
            @JsonProperty("eventName") String eventName,
            @JsonProperty("s3") S3Info s3
    ) {}

    public record S3Info(
            @JsonProperty("object") S3ObjectInfo object
    ) {}

    public record S3ObjectInfo(
            @JsonProperty("key") String key,   //파일명
            @JsonProperty("size") Long size
    ) {}
}