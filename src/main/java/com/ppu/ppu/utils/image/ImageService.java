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

    /*@Transactional
    public void replaceOwnerImage(UUID ownerId, UUID imageId, MultipartFile file) {
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
                try {
                    s3Storage.deleteObject(bucket, oldObjectKey);
                } catch (Exception ignore) {}
            }
        });
    }

    @Transactional
    public List<UUID> replaceOwnerImages(UUID ownerId, String bucket, String dirName, List<UUID> oldImageIds, List<MultipartFile> newImages) {
        if (newImages == null) newImages = Collections.emptyList();
        if (oldImageIds == null) oldImageIds = Collections.emptyList();

        for(UUID imageId : oldImageIds) {
            checkIsAuthorizedUser(ownerId, imageId);
        }

        List<UUID> imageIds = uploadImages(ownerId, bucket, dirName, newImages);
        try {
            deleteOwnerImages(ownerId, oldImageIds);
        } catch (Exception e) {
            deleteOwnerImages(ownerId, imageIds);
        }

        return imageIds;
    }

    @Transactional
    public void deleteOwnerImage(UUID ownerId, UUID imageId) {
        ImageEntity imageDb = imageRepository.findById(imageId)
                        .orElse(null);

        if(imageDb == null) {
            System.out.printf("Delete image failed. No Such keys: %s\n", imageId);
            return;
        }

        checkIsAuthorizedUser(ownerId, imageDb.getOwnerId());

        String bucket = imageDb.getBucket();
        String objectKey = imageDb.getObjectKey();

        imageRepository.delete(imageDb);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override public void afterCommit() {
                try {
                    s3Storage.deleteObject(bucket, objectKey);
                } catch (Exception ignore) {
                    System.out.printf("Delete image on S3 failed. imageId: %s, bucket: %s, objectKey: %s\n", imageId, bucket, objectKey);
                }
            }
        });
    }

    @Transactional
    public void deleteOwnerImages(UUID ownerId, List<UUID> imageIds) {
        if (imageIds == null || imageIds.isEmpty()) return;
        imageIds.forEach(imageId -> deleteOwnerImage(ownerId, imageId));
    }*/

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
