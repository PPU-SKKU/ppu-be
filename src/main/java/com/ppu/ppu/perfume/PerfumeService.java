package com.ppu.ppu.perfume;

import org.springframework.stereotype.Service;

@Service
public class PerfumeService {
    private final PerfumeRepository perfumeRepository;

    public PerfumeService(PerfumeRepository perfumeRepository) {
        this.perfumeRepository = perfumeRepository;
    }
}
