package com.ppu.ppu.perfume;

import com.ppu.ppu.perfume.dto.PerfumeResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfumeService {
    private final PerfumeRepository perfumeRepository;

    public PerfumeService(PerfumeRepository perfumeRepository) {
        this.perfumeRepository = perfumeRepository;
    }

    public List<PerfumeResponseDto> getAllPerfumes() {
        return perfumeRepository.findAll().stream()
                .map(PerfumeResponseDto::fromEntity)
                .toList();
    }

    public PerfumeResponseDto getPerfumeById(Long perfumeId) {
        return perfumeRepository.findById(perfumeId)
                .map(PerfumeResponseDto::fromEntity)
                .orElseThrow(() -> new RuntimeException("Perfume not found"));
    }

    public List<PerfumeResponseDto> searchPerfumesByKeyword(String searchKeyword) {
        return perfumeRepository.findByKoreanNameOrOriginalName(searchKeyword).stream()
                .map(PerfumeResponseDto::fromEntity)
                .toList();
    }
}
