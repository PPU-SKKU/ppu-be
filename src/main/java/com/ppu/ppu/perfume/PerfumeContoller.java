package com.ppu.ppu.perfume;


import com.ppu.ppu.perfume.dto.PerfumeResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/perfumes")
public class PerfumeContoller {
    private final PerfumeService perfumeService;

    public PerfumeContoller(PerfumeService perfumeService) {
        this.perfumeService = perfumeService;
    }

    @GetMapping("/")
    public List<PerfumeResponseDto> getAllPerfumes() {
        return perfumeService.getAllPerfumes();
        //TODO 페이지네이션
    }

    @GetMapping("/{perfumeId}")
    public PerfumeResponseDto getPerfumeById(@PathVariable(name = "perfumeId", required = true) Long perfumeId) {
        if (perfumeId == null) {
            throw new IllegalArgumentException("향수 ID path variable 누락");
        }
        // TODO 커스텀 예외 처리
        return perfumeService.getPerfumeById(perfumeId);
    }

    @GetMapping("/search")
    public List<PerfumeResponseDto> searchPerfumes(@RequestParam(name = "keyword", required = true) String searchKeyword) {
        if (searchKeyword == null || searchKeyword.isEmpty()) {
            throw new IllegalArgumentException("검색어 query param 누락");
        }
        // TODO 커스텀 예외 처리
        return perfumeService.searchPerfumesByKeyword(searchKeyword);
    }
}
