package com.umc.nuvibe.global.scheduler;

import com.umc.nuvibe.domain.image.entity.Image;
import com.umc.nuvibe.domain.image.repository.ImageRepository;
import com.umc.nuvibe.domain.image.vo.ImageStatus;
import com.umc.nuvibe.global.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ImageCleanupScheduler {

    private final ImageRepository imageRepository;
    private final S3Service s3Service;

    // 10분마다 스케쥴러 돌림
    private static final int IMAGE_EXPIRY_MINUTES = 10;

    @Scheduled(cron = "0 */10 * * * *") // 매 10분마다 실행
    @Transactional
    public void cleanupPendingImages() {
        log.info("==== PENDING 이미지 정리 스케줄러 시작 ====");

        try {
            // 10분 전 시간 계산
            LocalDateTime expiryTime = LocalDateTime.now().minusMinutes(IMAGE_EXPIRY_MINUTES);

            // PENDING 상태이면서 만료된 이미지 조회
            List<Image> expiredImages = imageRepository.findByStatusAndCreatedAtBefore(
                    ImageStatus.PENDING,
                    expiryTime
            );

            if (expiredImages.isEmpty()) {
                log.debug("정리할 PENDING 이미지가 없습니다.");
                return;
            }

            log.info("정리 대상 PENDING 이미지 개수: {}", expiredImages.size());

            int successCount = 0;
            int failCount = 0;

            for (Image image : expiredImages) {
                try {
                    boolean s3Deleted = true;

                    // S3에서 파일 삭제 (fileName이 있는 경우만)
                    if (image.getFileName() != null && !image.getFileName().isBlank()) {
                        s3Deleted = s3Service.deleteFile(image.getFileName());
                        if (s3Deleted) {
                            log.info("S3 파일 삭제 성공 - ID: {}, FileName: {}", image.getId(), image.getFileName());
                        } else {
                            log.warn("S3 파일 삭제 실패 - ID: {}, FileName: {}", image.getId(), image.getFileName());
                        }
                    }

                    // DB에서 삭제 (S3 삭제 실패해도 DB는 삭제)
                    imageRepository.delete(image);
                    successCount++;
                    log.info("DB 레코드 삭제 성공 - ID: {}", image.getId());

                } catch (Exception e) {
                    failCount++;
                    log.error("이미지 정리 실패 - ID: {}, FileName: {}",
                            image.getId(), image.getFileName(), e);
                }
            }

            log.info("==== PENDING 이미지 정리 완료 - 성공: {}, 실패: {} ====", successCount, failCount);

        } catch (Exception e) {
            log.error("PENDING 이미지 정리 중 예외 발생", e);
        }
    }
}
