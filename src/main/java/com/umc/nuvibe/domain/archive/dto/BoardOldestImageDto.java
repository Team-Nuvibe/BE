package com.umc.nuvibe.domain.archive.dto;

import java.time.LocalDateTime;

public record BoardOldestImageDto(Long boardId, LocalDateTime oldestCreatedAt) {
}
