package com.ppu.ppu.review;

import com.ppu.ppu.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findByUserId(UUID userId);
    List<Review> findByPerfumeId(Integer perfumeId);
    //Review findById(UUID id);
}
