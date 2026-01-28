package com.umc.nuvibe.global.service;

import com.umc.nuvibe.domain.image.dto.response.S3EventDto;
import com.umc.nuvibe.domain.image.repository.ImageRepository;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
@RequiredArgsConstructor
public class ImageEventListener {

    private final ImageRepository imageRepository;
    private final S3Service s3Service;

    @SqsListener(value = "${spring.cloud.aws.sqs.queue.name}")
    @Transactional
    public void receiveMessage(S3EventDto event) {

        // testEvent 등 메세지가 비어있는 경우 처리 x
        if (event.records() == null || event.records().isEmpty()) {
            log.info("레코드가 비어있는 처리할 수 없는 이벤트 수신: {}", event);
            return;
        }

        // 메세지 루프 처리
        for (S3EventDto.S3Record record : event.records()) {
            String eventName = record.eventName();

            // testEvent 처리
            if ("s3:TestEvent".equals(eventName)) {
                log.info("S3 연결 테스트 이벤트 수신 완료");
                continue;
            }

            // 객체 생성 이벤트 아니면 스킵함
            if (!eventName.startsWith("ObjectCreated:")) {
                log.warn("지원하지 않는 이벤트 타입: {}", eventName);
                continue;
            }

            // 파일명 추출 후 디코딩
            String rawKey = record.s3().object().key();
            String decodedKey = URLDecoder.decode(rawKey, StandardCharsets.UTF_8);

            log.info("이미지 업로드 감지: {}", decodedKey);

            // 이미지 엔티티 업로드
            handleImageUpload(decodedKey);
        }
    }

    private void handleImageUpload(String fileName) {
        // db에서 파일명으로 파일 찾아다가 pending 이미지를 active로 바꾸기
        if (!fileName.startsWith("images/")) {
            log.info("이미지 prefix가 아닌 객체는 처리하지 않습니다: {}", fileName);
            return;
        }

        imageRepository.findByFileName(fileName)
                .ifPresentOrElse(
                        image -> {
                            // 상태변경
                            image.activate();

                            // presigned URL을 실제 S3 URL로 변경
                            String s3Url = s3Service.getS3Url(fileName);
                            image.updateImageUrl(s3Url);

                            log.info("이미지 업로드 상태 동기화 완료: ID={}, Key={}, URL={}",
                                    image.getId(), fileName, s3Url);
                        },
                        () -> {
                            // s3에 있는데 db에 없으면 고아 객체이므로 삭제
                            log.warn("고아 객체 감지: {}", fileName);
                            boolean deleted = s3Service.deleteFile(fileName);
                            if (deleted) {
                                log.info("고아 객체 삭제 완료: {}", fileName);
                            } else {
                                log.error("고아 객체 삭제 실패: {}", fileName);
                            }
                        }
                );
    }
}
