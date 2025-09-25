package com.ppu.ppu.review;

import com.ppu.ppu.review.domain.Review;
import com.ppu.ppu.review.dto.ReviewCreateDto;
import com.ppu.ppu.review.dto.ReviewResponseDto;
import com.ppu.ppu.review.dto.ReviewUpdateDto;
import com.ppu.ppu.utils.image.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ImageService imageService;

    public ReviewService(ReviewRepository reviewRepository, ImageService imageService) {
        this.reviewRepository = reviewRepository;
        this.imageService = imageService;
    }

    public List<ReviewResponseDto> getMyReviews(UUID userId) {
        return reviewRepository.findByUserId(userId).stream()
                .map(review -> {
                    URL perfumeUrl = imageService.getDownloadUrl(review.getPerfume().getImage());
                    return ReviewResponseDto.fromEntity(review, perfumeUrl);
                })
                .toList();
    }

    public List<ReviewResponseDto> getAllReviewsByPerfumeId(Integer perfumeId) {
        return reviewRepository.findByPerfumeId(perfumeId).stream()
                .map(review -> {
                    URL perfumeUrl = imageService.getDownloadUrl(review.getPerfume().getImage());
                    return ReviewResponseDto.fromEntity(review, perfumeUrl);
                })
                .toList();
    }

    public ReviewResponseDto getReviewById(UUID id) {
        return reviewRepository.findById(id)
                .map(review -> {
                    URL perfumeUrl = imageService.getDownloadUrl(review.getPerfume().getImage());
                    return ReviewResponseDto.fromEntity(review, perfumeUrl);
                })
                .orElseThrow(() -> new RuntimeException("Review not found"));
    }

    public List<ReviewResponseDto> getReviewsByIds(List<UUID> ids) {
        return reviewRepository.findAllById(ids).stream()
                .map(review -> {
                    URL perfumeUrl = imageService.getDownloadUrl(review.getPerfume().getImage());
                    return ReviewResponseDto.fromEntity(review, perfumeUrl);
                })
                .toList();
    }

    @Transactional
    public void createReview(ReviewCreateDto reviewCreateDto) {
        Review review = ReviewCreateDto.fromDto(reviewCreateDto);
        reviewRepository.save(review);
    }

    @Transactional
    public void updateReview(UUID reviewId, ReviewUpdateDto reviewUpdateDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("No such review"));
        review.setLiked(reviewUpdateDto.isLiked());
        review.setScore(reviewUpdateDto.getScore());
        review.setWearTested(reviewUpdateDto.isWearTested());
        review.setTestedDate(reviewUpdateDto.getTestedDate());
        review.setContent(reviewUpdateDto.getContent());
    }

    public void deleteReview(List<UUID> reviewIds) {
        reviewRepository.deleteAllById(reviewIds);
    }
}
