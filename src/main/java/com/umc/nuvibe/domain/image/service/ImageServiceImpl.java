package com.umc.nuvibe.domain.image.service;

import com.umc.nuvibe.domain.archive.entity.ArchiveBoard;
import com.umc.nuvibe.domain.archive.entity.BoardImage;
import com.umc.nuvibe.domain.archive.repository.BoardImageRepository;
import com.umc.nuvibe.domain.image.dto.request.PreSignedUrlReq;
import com.umc.nuvibe.domain.image.dto.response.ImageDetailRes;
import com.umc.nuvibe.domain.image.dto.response.ImageRes;
import com.umc.nuvibe.domain.image.dto.response.ImageStatusRes;
import com.umc.nuvibe.domain.image.dto.response.PreSignedUrlRes;
import com.umc.nuvibe.domain.image.entity.Image;
import com.umc.nuvibe.domain.image.repository.ImageRepository;
import com.umc.nuvibe.domain.image.vo.ImageStatus;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.user.entity.User;
import com.umc.nuvibe.global.apiPayLoad.error.ImageErrorCode;
import com.umc.nuvibe.global.apiPayLoad.exception.BusinessException;
import com.umc.nuvibe.global.service.S3Service;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final S3Service s3Service;
    private final ImageRepository imageRepository;
    private final BoardImageRepository boardImageRepository;



    @Override
    @Transactional
    public ImageRes preSaveAndGetUrl(PreSignedUrlReq request, ImageTag tag) {
        // 썸네일 적용을 위해 prefix를 raw로 설정, 썸네일은 이 파일에서만 생성됨
        String prefix = "raw";

        PreSignedUrlRes preSignedUrl = s3Service.getPreSignedUrl(request, prefix);

        // fileName에서 prefix 제거
        String pureFileName = preSignedUrl.fileName().replace("raw/", "");

        Image pendingImage = Image.builder()
                .imageUrl(preSignedUrl.url()) // Presigned URL (raw/ 경로)
                .fileName(pureFileName)        // "uuid.jpg" (prefix 제거)
                .imageTag(tag)
                .status(ImageStatus.PENDING)
                .build();
        imageRepository.save(pendingImage);

        return new ImageRes(preSignedUrl.url(), pureFileName, pendingImage.getId(), pendingImage.getImageTag());
    }

    @Override
    @Transactional
    public String uploadImage(MultipartFile file) {
        validateFile(file);
        return uploadToS3(file);
    }

    @Override
    @Transactional(readOnly = true)
    public ImageDetailRes getImageDetail (Long userId, Long imageId) {
        BoardImage boardImage = boardImageRepository.findByImageId(imageId)
                .orElseThrow(() -> new BusinessException(ImageErrorCode.IMAGE_NOT_FOUND));

        Image image = boardImage.getImage();
        ArchiveBoard board = boardImage.getBoard();
        User user = board.getUser();

        if (!user.getId().equals(userId)) {
            throw new BusinessException(ImageErrorCode.IMAGE_ACCESS_DENIED);
        }

        return ImageDetailRes.from(image, user, board);
    }

    @Override
    @Transactional(readOnly = true)
    public ImageStatusRes getImageStatus(Long userId, Long imageId) {

        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new BusinessException(ImageErrorCode.IMAGE_NOT_FOUND));

        return ImageStatusRes.from(image);
    }

    // 파일 검증 분리
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ImageErrorCode.IMAGE_IS_EMPTY);
        }
    }

    // 파일 업로드 분리
    private String uploadToS3(MultipartFile file) {
        String imageURL = s3Service.upload(file);
        if (imageURL == null) {
            throw new BusinessException(ImageErrorCode.IMAGE_UPLOAD_FAIL);
        }
        return imageURL;
    }
}
