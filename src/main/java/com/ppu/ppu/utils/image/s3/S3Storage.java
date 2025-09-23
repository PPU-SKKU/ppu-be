package com.ppu.ppu.utils.image.s3;

import com.ppu.ppu.exception.ErrorCode;
import com.ppu.ppu.exception.domain.ImageException;
import com.ppu.ppu.utils.image.ImageUploadResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Storage {
    private final S3Config s3Config;
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    public URL createDownloadUrl(String bucket, String objectKey) {
        HeadObjectResponse head = getObjectHead(bucket, objectKey);;

        GetObjectRequest getReq = GetObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .responseContentType(head.contentType())
                .responseCacheControl("public, max-age=86400")
                .build();

        // Presign
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMillis(s3Config.getPresignExpiration()))
                .getObjectRequest(getReq)
                .build();

        try {
            return s3Presigner.presignGetObject(presignRequest).url();
        } catch (Exception e) {
            throw new ImageException(ErrorCode.IMAGE_DOWNLOAD_FAILED);
        }
    }

    public ImageUploadResultDto putObject(String bucket, String dirName, MultipartFile file) {
        validateFile(file);
        String objectKey = buildObjectKey(dirName, file);

        PutObjectRequest req = PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(objectKey)
                        .contentType(file.getContentType())
                        .build();

        Dimension size = getImageDimensions(file);

        try(InputStream in = file.getInputStream()) {
            s3Client.putObject(req, RequestBody.fromInputStream(in, file.getSize()));
        } catch (IOException | S3Exception e) {
            throw new ImageException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }

        return new ImageUploadResultDto(objectKey, file.getContentType(), size);
    }

    public void deleteObject(String bucket, String objectKey) {
        validateKey(objectKey);

        try {
            s3Client.deleteObject(DeleteObjectRequest
                    .builder()
                    .bucket(bucket)
                    .key(objectKey)
                    .build()
            );
        } catch(NoSuchKeyException e) {
            // ignore
        }
        catch (S3Exception e) {
            throw new ImageException(ErrorCode.IMAGE_DELETE_FAILED);
        }
    }

    public String getDirNameFromObjectKey(String objectKey) {
        validateKey(objectKey);
        int idx = objectKey.lastIndexOf("/");
        if(idx <= 0) throw new ImageException(ErrorCode.IMAGE_INVALID_OBJECT_KEY);
        return objectKey.substring(0, idx);
    }

    private void validateKey(String objectKey) {
        if(objectKey == null || objectKey.isEmpty()) {
            throw new ImageException(ErrorCode.IMAGE_BLANK_OBJECT_KEY);
        }
    }

    private void validateFile(MultipartFile file) {
        String contentType = file.getContentType();
        if(file.isEmpty()) {
            throw new ImageException(ErrorCode.IMAGE_INVALID_FILE_TYPE);
        }

        if(!Objects.requireNonNull(contentType).startsWith("image/")) {
            throw new ImageException(ErrorCode.IMAGE_INVALID_FILE_TYPE);
        }

        // Exception on file size above than 10MB
        if(file.getSize() > 10 * 1024 * 1024) {
            throw new ImageException(ErrorCode.IMAGE_FILE_SIZE_EXCEEDED);
        }
    }

    private String buildObjectKey(String dirName, MultipartFile file) {
        UUID uuid = UUID.randomUUID();
        String ext = Objects
                .requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf("."));
        return dirName + "/" + uuid.toString() + ext;
    }

    private Dimension getImageDimensions(MultipartFile file) {
        try(InputStream in = file.getInputStream()) {
            BufferedImage image = ImageIO.read(in);
            if(image == null) {
                throw new ImageException(ErrorCode.IMAGE_INVALID_FILE_TYPE);
            }
            return new Dimension(image.getWidth(), image.getHeight());
        } catch (IOException e) {
            throw new ImageException(ErrorCode.IMAGE_INVALID_FILE_TYPE);
        }
    }

    private HeadObjectResponse getObjectHead(String bucket, String objectKey) {
        validateKey(objectKey);
        try {
            return s3Client.headObject(HeadObjectRequest.builder().bucket(bucket).key(objectKey).build());
        } catch(NoSuchKeyException e) {
            throw new ImageException(ErrorCode.IMAGE_NOT_FOUND);
        } catch (Exception e) {
            throw new ImageException(ErrorCode.IMAGE_DOWNLOAD_FAILED);
        }
    }
}
