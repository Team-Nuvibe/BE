package com.umc.nuvibe.domain.image.service;

import com.umc.nuvibe.domain.archive.entity.ArchiveBoard;
import com.umc.nuvibe.domain.archive.entity.BoardImage;
import com.umc.nuvibe.domain.archive.repository.BoardImageRepository;
import com.umc.nuvibe.domain.image.dto.response.ImageDetailRes;
import com.umc.nuvibe.domain.image.dto.response.ImageRes;
import com.umc.nuvibe.domain.image.entity.Image;
import com.umc.nuvibe.domain.image.repository.ImageRepository;
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
    public ImageRes uploadAndSave(MultipartFile file, ImageTag tag) {

        validateFile(file);

        if (tag == null) {
            throw new BusinessException(ImageErrorCode.IMAGETAG_IS_NULL);
        }

        String imageURL=uploadToS3(file);

        Image newImage = Image.builder()
                .imageUrl(imageURL)
                .imageTag(tag)
                .build();

        imageRepository.save(newImage);

        return new ImageRes(imageURL, tag);
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
    @Transactional
    public Image uploadAndSaveEntity(MultipartFile file, ImageTag tag) {
        validateFile(file);

        if (tag == null) {
            throw new BusinessException(ImageErrorCode.IMAGETAG_IS_NULL);
        }

        String imageURL=uploadToS3(file);

        Image newImage = Image.builder()
                .imageUrl(imageURL)
                .imageTag(tag)
                .build();

        return imageRepository.save(newImage);
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
