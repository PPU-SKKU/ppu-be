package com.ppu.ppu.perfume;


import com.ppu.ppu.perfume.dto.PerfumeResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name = "perfumes", description = "향수 정보 API")
@RestController
@RequestMapping("/perfumes")
public class PerfumeController {
    private final PerfumeService perfumeService;

    public PerfumeController(PerfumeService perfumeService) {
        this.perfumeService = perfumeService;
    }

    @GetMapping("/")
    public ResponseEntity<List<PerfumeResponseDto>> getAllPerfumes() {
        return ResponseEntity.ok(perfumeService.getAllPerfumes());
        //TODO 페이지네이션
    }

    @GetMapping("/{perfumeId}")
    public ResponseEntity<PerfumeResponseDto> getPerfumeById(@PathVariable(name = "perfumeId", required = true) Integer perfumeId) {
        if (perfumeId == null) {
            throw new IllegalArgumentException("향수 ID path variable 누락");
        }
        // TODO 커스텀 예외 처리
        return ResponseEntity.ok(
                perfumeService.getPerfumeById(perfumeId)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<PerfumeResponseDto>> searchPerfumes(@RequestParam(name = "keyword", required = true) String searchKeyword) {
        if (searchKeyword == null || searchKeyword.isEmpty()) {
            throw new IllegalArgumentException("검색어 query param 누락");
        }
        // TODO 커스텀 예외 처리
        return ResponseEntity.ok(
                perfumeService.searchPerfumesByKeyword(searchKeyword)
        );
    }
}
