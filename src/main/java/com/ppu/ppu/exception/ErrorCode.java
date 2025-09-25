package com.ppu.ppu.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    // common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "server error occured"),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "invalid input"),

    // Auth
    AUTH_LOGIN_FAILED(HttpStatus.BAD_REQUEST, "email or password do not match"),
    AUTH_SIGNUP_FAILED(HttpStatus.BAD_REQUEST, "duplicate email"),

    AUTH_OAUTH_NO_EMAIL(HttpStatus.BAD_REQUEST, "cannot extract email"),
    AUTH_OAUTH_KAKAO_API_FAILED(HttpStatus.BAD_REQUEST, "bad request from kakao api"),

    AUTH_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "invalid token"),
    AUTH_EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "expired token"),
    AUTH_UNAUTHORIZED(HttpStatus.FORBIDDEN, "unauthorized"),

    // User
    USER_LOAD_FAILED(HttpStatus.BAD_REQUEST, "user load failed"),

    // Image
    IMAGE_INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "invalid image type"),
    IMAGE_FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "image size exceeded"),
    IMAGE_UPLOAD_FAILED(HttpStatus.BAD_REQUEST, "image upload failed"),
    IMAGE_BLANK_OBJECT_KEY(HttpStatus.BAD_REQUEST, "object key is blank"),
    IMAGE_EMPTY_COLUMN(HttpStatus.BAD_REQUEST, "accessing empty Image"),
    IMAGE_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "replacing/deleting image who is not owner"),
    IMAGE_DELETE_FAILED(HttpStatus.BAD_REQUEST, "image deletion failed as internal error"),
    IMAGE_INVALID_OBJECT_KEY(HttpStatus.BAD_REQUEST, "object key is invalid format"),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "image not found on s3 with access key"),
    IMAGE_DOWNLOAD_FAILED(HttpStatus.BAD_REQUEST, "image download failed by some error"),

    // Oppu
    OPPU_POST_FAILED(HttpStatus.BAD_REQUEST, "post operation failed"),
    OPPU_NO_ARTICLE(HttpStatus.BAD_REQUEST, "no article with oppu Id"),
    OPPU_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "unauthorized"),
    OPPU_TAG_MAX_LIMIT(HttpStatus.BAD_REQUEST, "tag limit exceeded: 5"),
    OPPU_TAG_NOT_FIND(HttpStatus.BAD_REQUEST, "tag not found with id"),
    OPPU_TAG_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "unauthorized"),
    OPPU_NO_PERFUMES(HttpStatus.BAD_REQUEST, "no(null) perfumes with oppu Id"),
    OPPU_PERFUME_COUNT_LEQ_ZERO(HttpStatus.BAD_REQUEST, "perfume count leq zero"),
    OPPU_NO_TAGS(HttpStatus.BAD_REQUEST, "no(null) tags with oppu Id"),
    OPPU_INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, "invalid date format: should be yyyyMMdd"),
    OPPU_INVALID_MONTH_FORMAT(HttpStatus.BAD_REQUEST, "invalid month format: should be yyyyMM"),
    ;

    private final HttpStatus status;
    private final String message;
}
