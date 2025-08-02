package com.ppu.ppu.perfume;

import com.ppu.ppu.perfume.domain.Perfume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PerfumeRepository extends JpaRepository<Perfume, Long> {
    @Query("SELECT p FROM Perfume p WHERE p.brandId = :brandId")
    Perfume findByBrandId(int brandId);

    @Query("SELECT p FROM Perfume p WHERE p.koreanName LIKE %:searchKeyword% OR p.originalName LIKE %:searchKeyword%")
    Perfume findByKoreanNameOrOriginalName(String searchKeyword);
}