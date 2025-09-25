package com.ppu.ppu.utils.image;


import com.ppu.ppu.exception.ErrorCode;
import com.ppu.ppu.exception.domain.ImageException;
import com.ppu.ppu.utils.image.s3.S3Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public List<UUID> toList(UUID imageId) {
        return Stream.ofNullable(imageId).toList();
    }

    public List<MultipartFile> toList(MultipartFile file) {
        return Stream.ofNullable(file).toList();
    }

    public List<UUID> uploadImages(UUID ownerId, String bucket, String dirName, List<MultipartFile> files) {
        if(files == null || files.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> objectKeys = new ArrayList<>();
        List<UUID> imageIds = new ArrayList<>();
        try {
            for(MultipartFile file : files) {
                ImageUploadResultDto image = s3Storage.putObject(bucket, dirName, file);
                objectKeys.add(image.getObjectKey());

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
                imageIds.add(img.getId());
            }

            return imageIds;
        } catch (Exception e) {
            for(String objectKey : objectKeys) {
                try {
                    s3Storage.deleteObject(bucket, objectKey);
                } catch (Exception ignore) {}
            }
            throw new ImageException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }

    public void deleteObjectsAfterCommit(List<UUID> imageIds) {
        if (imageIds == null || imageIds.isEmpty()) return;

        List<ImageEntity> entities = imageRepository.findAllById(imageIds);
        if (entities.isEmpty()) return;

        Map<String, List<String>> bucketToKeys = entities.stream()
                .collect(Collectors.groupingBy(ImageEntity::getBucket,
                        Collectors.mapping(ImageEntity::getObjectKey, Collectors.toList())));

        imageRepository.deleteAllInBatch(entities);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override public void afterCommit() {
                bucketToKeys.forEach((bucket, keys) -> keys.forEach(key -> s3Storage.deleteObject(bucket, key)));
            }
        });
    }

    public void deleteObjectsOnRollback(List<UUID> imageIds) {
        if (imageIds == null || imageIds.isEmpty()) return;

        List<ImageEntity> entities = imageRepository.findAllById(imageIds);
        if (entities.isEmpty()) return;

        Map<String, List<String>> bucketToKeys = entities.stream()
                .collect(Collectors.groupingBy(ImageEntity::getBucket,
                        Collectors.mapping(ImageEntity::getObjectKey, Collectors.toList())));

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                    System.out.println("Delete Rollback accessed");
                    bucketToKeys.forEach((bucket, keys) -> keys.forEach(key -> s3Storage.deleteObject(bucket, key)));
                }
            }
        });
    }

    private void checkIsAuthorizedUser(UUID ownerId, UUID imgOwnerId) {
        if(imgOwnerId == null || !imgOwnerId.equals(ownerId)) {
            throw new ImageException(ErrorCode.IMAGE_UNAUTHORIZED);
        }
    }

    private void validateObject() {

    }
}
