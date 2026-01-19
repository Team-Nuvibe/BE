package com.umc.nuvibe.global.service;

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

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

    private final S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    // 이미지 확장자 화이트리스트
    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");

    // 업로드 기능
    public String upload(MultipartFile file) {

        // 빈 업로드 예외처리
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
        String fileName=createFileName(originalFilename);

        // s3에 업로드
        try (InputStream inputStream= file.getInputStream()) {
            S3Resource resource=s3Template.upload(bucket, fileName, inputStream,
                    ObjectMetadata.builder().contentType(file.getContentType()).build());

            return resource.getURL().toString();

        } catch (IOException e) {
            log.error("s3 업로드 실패",e);
            throw new BusinessException(ImageErrorCode.IMAGE_UPLOAD_FAIL);
        }
    }


    // 파일 검증
    private void validateFileExtension(String originalFilename) {

        String extension = getFileExtension(originalFilename);
        if (!IMAGE_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new BusinessException(ImageErrorCode.INVALID_FILE_EXTENSION);
        }

    }

    // 확장자 추출
    private String getFileExtension(String fileName) {

        int dotIndex=fileName.lastIndexOf(".");

        // .이 없거나 맨 앞에 있을 때
        if (dotIndex==-1||dotIndex==0) {
            throw new BusinessException(ImageErrorCode.INVALID_FILE_EXTENSION);
        }

        // .이 맨 껕에 있을 때
        if (dotIndex==fileName.length()-1) {
            return "";
        }

        return fileName.substring(dotIndex+1);

    }

    // UUID로 랜덤 파일명 생성
    private String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(".").concat(getFileExtension(originalFileName));
    }

}
