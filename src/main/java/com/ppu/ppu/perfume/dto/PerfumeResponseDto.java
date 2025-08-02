package com.ppu.ppu.perfume.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfumeResponseDto {
    Long id;
    String koreanName;
    String originalName;
    String brandKoreanName;
    String brandOriginalName;
    String image;

    public PerfumeResponseDto() {
    }

    public PerfumeResponseDto(Long id, String koreanName, String originalName, String brandKoreanName, String brandOriginalName, String image) {
        this.id = id;
        this.koreanName = koreanName;
        this.originalName = originalName;
        this.brandKoreanName = brandKoreanName;
        this.brandOriginalName = brandOriginalName;
        this.image = image;
    }
}
