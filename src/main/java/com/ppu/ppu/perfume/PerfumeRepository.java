package com.ppu.ppu.perfume;

import com.ppu.ppu.perfume.domain.Perfume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PerfumeRepository extends JpaRepository<Perfume, Integer> {
    @Query("SELECT p FROM Perfume p JOIN p.brand b WHERE p.id = :brandId")
    Perfume findByBrandId(int brandId);

    @Query("SELECT p FROM Perfume p JOIN p.brand b WHERE p.koreanName LIKE %:searchKeyword% OR p.originalName LIKE %:searchKeyword%")
    List<Perfume> findByKoreanNameOrOriginalName(String searchKeyword);
}