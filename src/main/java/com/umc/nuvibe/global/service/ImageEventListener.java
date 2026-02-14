package com.umc.nuvibe.global.service;

import com.umc.nuvibe.domain.image.dto.response.S3EventDto;
import com.umc.nuvibe.domain.image.repository.ImageRepository;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class ImageEventListener {

    private final ImageRepository imageRepository;

    @SqsListener(value = "${spring.cloud.aws.sqs.queue.name}")
    @Transactional
    public void receiveMessage(S3EventDto event) {

        // 필수 필드 검증
        if (event.fileId() == null || event.originalUrl() == null || event.thumbnailUrl() == null) {
            log.warn("잘못된 Lambda 메시지 수신: {}", event);
            return;
        }

        // Lambda 처리 실패 메시지 처리
        if (!"SUCCESS".equals(event.status())) {
            log.error("Lambda 이미지 처리 실패: fileId={}, status={}", event.fileId(), event.status());
            return;
        }

        log.info("Lambda 메시지 수신: fileId={}", event.fileId());
        handleLambdaMessage(event);
    }

    //Lambda가 생성한 original, thumbnail URL로 DB 업데이트
    private void handleLambdaMessage(S3EventDto event) {
        imageRepository.findByFileName(event.fileId())
            .ifPresentOrElse(
                image -> {
                    // URL 업데이트
                    image.updateImageUrl(event.originalUrl());
                    image.updateThumbnailUrl(event.thumbnailUrl());
                    image.activate();

                    log.info("이미지 처리 완료 - ID: {}, fileId: {}, original: {}, thumb: {}",
                        image.getId(), event.fileId(), event.originalUrl(), event.thumbnailUrl());
                },
                () -> {
                    log.warn("DB에 존재하지 않는 fileId: {}", event.fileId());
                    // Lambda가 생성한 파일이지만 DB에 없는 경우 (고아 파일)
                    // TODO: S3에서 삭제 로직 추가 고려
                }
            );
    }
}
