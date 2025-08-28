package com.ppu.ppu.review;

import com.ppu.ppu.review.domain.Review;
import com.ppu.ppu.review.dto.ReviewCreateDto;
import com.ppu.ppu.review.dto.ReviewResponseDto;
import com.ppu.ppu.review.dto.ReviewUpdateDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<ReviewResponseDto> getMyReviews(UUID userId) {
        return reviewRepository.findByUserId(userId).stream()
                .map(ReviewResponseDto::fromEntity)
                .toList();
    }

    public List<ReviewResponseDto> getAllReviewsByPerfumeId(Integer perfumeId) {
        return reviewRepository.findByPerfumeId(perfumeId).stream()
                .map(ReviewResponseDto::fromEntity)
                .toList();
    }

    public ReviewResponseDto getReviewById(UUID id) {
        return reviewRepository.findById(id)
                .map(ReviewResponseDto::fromEntity)
                .orElseThrow(() -> new RuntimeException("Review not found"));
    }

    public void createReview(ReviewCreateDto reviewCreateDto) {
        Review review = ReviewCreateDto.fromDto(reviewCreateDto);
        reviewRepository.save(review);
    }

    public void updateReview(ReviewUpdateDto reviewUpdateDto) {
        //TODO: update 작성
    }

    public void deleteReview(List<UUID> reviewIds) {
        reviewRepository.deleteAllById(reviewIds);
    }
}
