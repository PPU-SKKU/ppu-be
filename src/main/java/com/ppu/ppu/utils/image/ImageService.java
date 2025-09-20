package com.ppu.ppu.utils.image;


import com.ppu.ppu.exception.ErrorCode;
import com.ppu.ppu.exception.domain.ImageException;
import com.ppu.ppu.utils.image.s3.S3Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final S3Storage s3Storage;
    private final ImageRepository imageRepository;

    public URL getDownloadUrl(UUID imageId) {
        ImageEntity image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ImageException(ErrorCode.IMAGE_EMPTY_COLUMN));

        String bucket = image.getBucket();
        String objectKey = image.getObjectKey();
        return s3Storage.createDownloadUrl(bucket, objectKey);
    }

    public List<URL> getDownloadUrls(List<UUID> imageIds) {
        return imageIds.stream()
                .map(this::getDownloadUrl)
                .collect(Collectors.toList());
    }

    public UUID uploadImage(UUID ownerId, String bucket, String dirName, MultipartFile file) {
        ImageUploadResultDto image = s3Storage.putObject(bucket, dirName, file);

        try {
            ImageEntity img = ImageEntity.builder()
                    .ownerId(ownerId)
                    .bucket(bucket)
                    .objectKey(image.getObjectKey())
                    .contentType(image.getContentType())
                    .status(ImageUploadStatus.READY)
                    .width(image.getSize().width)
                    .height(image.getSize().height)
                    .build();
            imageRepository.save(img);
            return img.getId();

        } catch (Exception e) {
            s3Storage.deleteObject(bucket, image.getObjectKey());
            throw new ImageException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }

    public List<UUID> uploadImages(UUID ownerId, String bucket, String dirName, List<MultipartFile> files) {
        return files.stream()
                .map(file -> uploadImage(ownerId, bucket, dirName, file))
                .collect(Collectors.toList());
    }

    @Transactional
    public UUID replaceOwnerImage(UUID ownerId, UUID imageId, MultipartFile file) {
        ImageEntity imageDb = imageRepository.findById(imageId)
                .orElseThrow(() -> new ImageException(ErrorCode.IMAGE_EMPTY_COLUMN));

        // 1. Check current user is authorized
        checkIsAuthorizedUser(ownerId, imageDb.getOwnerId());

        // 2. Upload new image to S3
        String bucket = imageDb.getBucket();
        String oldObjectKey = imageDb.getObjectKey();

        ImageUploadResultDto newImage =
                s3Storage.putObject(bucket,
                        s3Storage.getDirNameFromObjectKey(imageDb.getObjectKey()),
                        file
                );

        // 3. Change db to link new image
        try {
            ImageEntity img = ImageEntity.builder()
                    .id(imageDb.getId())
                    .ownerId(ownerId)
                    .bucket(bucket)
                    .objectKey(newImage.getObjectKey())
                    .contentType(newImage.getContentType())
                    .status(ImageUploadStatus.READY)
                    .width(newImage.getSize().width)
                    .height(newImage.getSize().height)
                    .build();

            imageRepository.save(img);
        } catch (Exception e) {
            s3Storage.deleteObject(bucket, newImage.getObjectKey());
            throw new ImageException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }

        // 4. After db is linking new image, remove old image in s3
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override public void afterCommit() {
                s3Storage.deleteObject(bucket, oldObjectKey);
            }
        });

        return imageDb.getId();
    }

    @Transactional
    public void deleteOwnerImage(UUID ownerId, UUID imageId) {
        ImageEntity imageDb = imageRepository.findById(imageId)
                .orElseThrow(() -> new ImageException(ErrorCode.IMAGE_EMPTY_COLUMN));

        checkIsAuthorizedUser(ownerId, imageDb.getOwnerId());

        String bucket = imageDb.getBucket();
        String objectKey = imageDb.getObjectKey();

        imageRepository.delete(imageDb);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override public void afterCommit() {
                s3Storage.deleteObject(bucket, objectKey);
            }
        });
    }

    @Transactional
    public void deleteOwnerImages(UUID ownerId, List<UUID> imageIds) {
        imageIds.forEach(imageId -> deleteOwnerImage(ownerId, imageId));
    }

    private void checkIsAuthorizedUser(UUID ownerId, UUID imgOwnerId) {
        if(imgOwnerId == null || !imgOwnerId.equals(ownerId)) {
            throw new ImageException(ErrorCode.IMAGE_UNAUTHORIZED);
        }
    }
}
