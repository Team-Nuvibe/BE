package com.umc.nuvibe.global.service;

import com.umc.nuvibe.domain.image.dto.request.PreSignedUrlReq;
import com.umc.nuvibe.domain.image.dto.response.PreSignedUrlRes;
import com.umc.nuvibe.global.apiPayLoad.error.ImageErrorCode;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

    private final S3Template s3Template;
    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    private static final String REGION = "ap-northeast-2";

    // 이미지 확장자 화이트리스트
    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");

    // pre-signed-url 발급
    public PreSignedUrlRes getPreSignedUrl(PreSignedUrlReq request, String prefix) {

        // 확장자 검증
        validateFileExtension(request.originalFileName());

        // 저장 경로 및 파일명 생성
        String fileName = createPath(prefix, request.originalFileName());

        //S3 요청 객체 생성 (PUT 메서드)
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();

        // preSigned URL 요청 설정
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .putObjectRequest(putObjectRequest)
                .build();

        //url 발급
        PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(presignRequest);
        String url = presignedPutObjectRequest.url().toString();

        log.info("Presigned URL 발급 완료: {}", url);

        return new PreSignedUrlRes(url, fileName);
    }

    public String upload(MultipartFile file) {

        if (file.isEmpty()) {
            throw new BusinessException(ImageErrorCode.IMAGE_IS_EMPTY);
        }

        // 파일 검증
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new BusinessException(ImageErrorCode.INVALID_FILE_EXTENSION);
        }
        validateFileExtension(originalFilename);

        // UUID로 파일명 안 겹치게 난수화
        String fileName = createFileName(originalFilename);

        // s3에 업로드
        try (InputStream inputStream = file.getInputStream()) {
            S3Resource resource = s3Template.upload(bucket, fileName, inputStream,
                    ObjectMetadata.builder().contentType(file.getContentType()).build());

            return resource.getURL().toString();

        } catch (IOException e) {
            log.error("s3 업로드 실패", e);
            throw new BusinessException(ImageErrorCode.IMAGE_UPLOAD_FAIL);
        }
    }

    // S3에서 파일 삭제
    public boolean deleteFile(String fileName) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            log.info("S3 파일 삭제 완료: {}", fileName);
            return true;
        } catch (Exception e) {
            log.error("S3 파일 삭제 실패 : {}", fileName, e);
            return false;
        }
    }

    // fileName을 실제 S3 URL로 변환
    public String getS3Url(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, REGION, fileName);
    }


    // 파일 확장자 검증
    private void validateFileExtension(String originalFilename) {
        String extension = getFileExtension(originalFilename);
        if (!IMAGE_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new BusinessException(ImageErrorCode.INVALID_FILE_EXTENSION);
        }
    }

    // 확장자 추출
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");

        // .이 없거나 맨 앞에 있을 때
        if (dotIndex == -1 || dotIndex == 0) {
            throw new BusinessException(ImageErrorCode.INVALID_FILE_EXTENSION);
        }

        // .이 맨 끝에 있을 때
        if (dotIndex == fileName.length() - 1) {
            return "";
        }

        return fileName.substring(dotIndex + 1);
    }

    // UUID로 랜덤 파일명 생성
    private String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(".").concat(getFileExtension(originalFileName));
    }

    // 경로 포함 파일명 생성 로직
    private String createPath(String prefix, String originalFileName) {
        String fileName = createFileName(originalFileName);

        // prefix가 유효하면 prefix/파일명, 없으면 그냥 파일명
        if (prefix != null && !prefix.isBlank()) {
            return prefix.concat("/").concat(fileName);
        }
        return fileName;
    }

}