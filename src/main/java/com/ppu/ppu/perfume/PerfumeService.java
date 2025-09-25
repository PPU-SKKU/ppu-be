package com.ppu.ppu.perfume;

import com.ppu.ppu.perfume.domain.Perfume;
import com.ppu.ppu.perfume.dto.PerfumeResponseDto;
import com.ppu.ppu.utils.image.ImageService;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;

@Service
public class PerfumeService {
    private final PerfumeRepository perfumeRepository;
    private final ImageService imageService;

    public PerfumeService(PerfumeRepository perfumeRepository, ImageService imageService) {
        this.perfumeRepository = perfumeRepository;
        this.imageService = imageService;
    }

    public List<PerfumeResponseDto> getAllPerfumes() {
        return perfumeRepository.findAll().stream()
                .map(perfume -> {
                    URL imageUrl = imageService.getDownloadUrl(perfume.getImage());
                    return PerfumeResponseDto.fromEntity(perfume, imageUrl);
                })
                .toList();
    }

    public PerfumeResponseDto getPerfumeById(Integer perfumeId) {
        Perfume perfume = perfumeRepository.findById(perfumeId).orElseThrow(() -> new RuntimeException("Perfume not found"));
        URL imageUrl = imageService.getDownloadUrl(perfume.getImage());

        return PerfumeResponseDto.fromEntity(perfume, imageUrl);
        // TODO 커스텀 예외 처리
    }

    public List<PerfumeResponseDto> searchPerfumesByKeyword(String searchKeyword) {
        return perfumeRepository.findByKoreanNameOrOriginalName(searchKeyword).stream()
                .map(perfume -> {
                    URL imageUrl = imageService.getDownloadUrl(perfume.getImage());
                    return PerfumeResponseDto.fromEntity(perfume, imageUrl);
                })
                .toList();
    }
}
