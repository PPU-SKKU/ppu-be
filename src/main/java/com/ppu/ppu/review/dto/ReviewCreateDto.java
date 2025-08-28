package com.ppu.ppu.review.dto;

import com.ppu.ppu.review.domain.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class ReviewCreateDto {
    UUID userId;
    Integer perfumeId;
    boolean isLiked;
    int score;
    boolean wearTested;
    LocalDate testedDate;
    String content;

    public ReviewCreateDto() {
    }

    public ReviewCreateDto(UUID userId, Integer perfumeId, boolean isLiked, int score, boolean wearTested, LocalDate testedDate, String content) {
        this.userId = userId;
        this.perfumeId = perfumeId;
        this.isLiked = isLiked;
        this.score = score;
        this.wearTested = wearTested;
        this.testedDate = testedDate;
        this.content = content;
    }

    public static Review fromDto(ReviewCreateDto dto) {
        Review review = new Review();
        return review;
    }
}
