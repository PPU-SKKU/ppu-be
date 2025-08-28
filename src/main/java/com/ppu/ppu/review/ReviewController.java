package com.ppu.ppu.review;

import com.ppu.ppu.review.dto.ReviewCreateDto;
import com.ppu.ppu.review.dto.ReviewResponseDto;
import com.ppu.ppu.review.dto.ReviewUpdateDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@Tag(name = "review", description = "시향기 API")
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("")
    public ResponseEntity<List<ReviewResponseDto>> getReview(@RequestParam(name = "perfumeId", required = true) Integer perfumeId) {
        return ResponseEntity.ok(reviewService.getAllReviewsByPerfumeId(perfumeId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ReviewResponseDto>> getMyReviews(HttpServletRequest request) {
        UUID userId = (UUID) request.getAttribute("id");
        return ResponseEntity.ok(reviewService.getMyReviews(userId));
    }

    @PostMapping("")
    public ResponseEntity<Void> createReview(HttpServletRequest request, @RequestBody ReviewCreateDto reviewCreateDto) {
        UUID userId = (UUID) request.getAttribute("id");
        reviewCreateDto.setUserId(userId);
        reviewService.createReview(reviewCreateDto);
        return ResponseEntity.status(201).build();
    }

    @PatchMapping("")
    public void updateReview(HttpServletRequest request, @PathVariable UUID reviewId, @RequestBody ReviewUpdateDto reviewUpdateDto) {
        UUID userId = (UUID) request.getAttribute("id");
        ReviewResponseDto review = reviewService.getReviewById(reviewId);
        if(review == null) throw new RuntimeException("Review not found");
        else if(!review.getUserId().equals(userId)) {
            throw new RuntimeException("No permission to update this review");
        }
        reviewService.updateReview(reviewUpdateDto);
    }

    @DeleteMapping("")
    public ResponseEntity<Void> deleteReview(HttpServletRequest request, @RequestBody List<UUID> reviewIds) {
        UUID userId = (UUID) request.getAttribute("id");
        reviewService.getReviewsByIds(reviewIds).forEach(review -> {
            if(!review.getUserId().equals(userId)) {
                throw new RuntimeException("No permission to delete this review");
            }
        });
        reviewService.deleteReview(reviewIds);
        return ResponseEntity.noContent().build();
    }

}
