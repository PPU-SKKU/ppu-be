package com.ppu.ppu.review.dto;

import java.time.LocalDate;

public class ReviewUpdateDto {
    boolean isLiked;
    int score;
    boolean wearTested;
    LocalDate testedDate;
    String content;

    public ReviewUpdateDto() {
    }

    public ReviewUpdateDto(boolean isLiked, int score, boolean wearTested, LocalDate testedDate, String content) {
        this.isLiked = isLiked;
        this.score = score;
        this.wearTested = wearTested;
        this.testedDate = testedDate;
        this.content = content;
    }
}
