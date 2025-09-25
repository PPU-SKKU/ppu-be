package com.ppu.ppu.perfume.dto;


import com.ppu.ppu.perfume.domain.Brand;
import com.ppu.ppu.perfume.domain.Perfume;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;

@Getter
@Setter
public class PerfumeResponseDto {
    Integer id;
    String koreanName;
    String originalName;
    String brandKoreanName;
    String brandOriginalName;
    URL image;

    public PerfumeResponseDto() {
    }

    public PerfumeResponseDto(Integer id, String koreanName, String originalName, String brandKoreanName, String brandOriginalName, URL image) {
        this.id = id;
        this.koreanName = koreanName;
        this.originalName = originalName;
        this.brandKoreanName = brandKoreanName;
        this.brandOriginalName = brandOriginalName;
        this.image = image;
    }

    public static PerfumeResponseDto fromEntity(Perfume perfume, URL imageUrl) {
        PerfumeResponseDto dto = new PerfumeResponseDto();
        dto.setId(perfume.getId());
        dto.setKoreanName(perfume.getKoreanName());
        dto.setOriginalName(perfume.getOriginalName());

        Brand brand = perfume.getBrand();
        if (brand != null) {
            dto.setBrandKoreanName(brand.getKoreanName());
            dto.setBrandOriginalName(brand.getOriginalName());
        }

        dto.setImage(imageUrl);

        return dto;
    }
}
