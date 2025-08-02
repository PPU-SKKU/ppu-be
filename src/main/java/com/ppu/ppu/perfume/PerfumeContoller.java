package com.ppu.ppu.perfume;


import com.ppu.ppu.perfume.dto.PerfumeResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/perfumes")
public class PerfumeContoller {
    private final PerfumeService perfumeService;

    public PerfumeContoller(PerfumeService perfumeService) {
        this.perfumeService = perfumeService;
    }

    @GetMapping("/")
    public List<PerfumeResponseDto> getAllPerfumes() {
        return new ArrayList<>();
    }

    @GetMapping("/")
    public PerfumeResponseDto getPerfumeById(@PathVariable(name = "perfumeId", required = true) Long perfumeId) {
        return new PerfumeResponseDto();
    }

    @GetMapping("/search")
    public List<PerfumeResponseDto> searchPerfumes(String searchKeyword) {
        return new ArrayList<>();
    }
}
