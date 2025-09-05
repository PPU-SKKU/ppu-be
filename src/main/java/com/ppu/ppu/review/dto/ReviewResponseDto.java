package com.ppu.ppu.review.dto;

import com.ppu.ppu.perfume.dto.PerfumeResponseDto;
import com.ppu.ppu.review.domain.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class ReviewResponseDto {
    UUID id;
    UUID userId;
    PerfumeResponseDto perfume;
    boolean isLiked;
    int score;
    boolean wearTested;
    LocalDate testedDate;
    String content;

    public ReviewResponseDto() {
    }

    public ReviewResponseDto(UUID id, UUID userId, PerfumeResponseDto perfume, boolean isLiked, int score, boolean wearTested, LocalDate testedDate, String content) {
        this.id = id;
        this.userId = userId;
        this.perfume = perfume;
        this.isLiked = isLiked;
        this.score = score;
        this.wearTested = wearTested;
        this.testedDate = testedDate;
        this.content = content;
    }

    public static ReviewResponseDto fromEntity(Review review) {
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setId(review.getId());
        dto.setUserId(UUID.fromString(String.valueOf(review.getUserId())));
        dto.setPerfume(PerfumeResponseDto.fromEntity(review.getPerfume()));
        dto.setLiked(review.isLiked());
        dto.setScore(review.getScore());
        dto.setWearTested(review.isWearTested());
        dto.setTestedDate(review.getTestedDate());
        dto.setContent(review.getContent());
        return dto;
    }
}
