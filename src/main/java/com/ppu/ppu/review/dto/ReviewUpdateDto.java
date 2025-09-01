package com.ppu.ppu.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewUpdateDto {
    boolean isLiked;
    int score;
    boolean wearTested;
    LocalDate testedDate;
    String content;
}
