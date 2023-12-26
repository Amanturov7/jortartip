package kg.amanturov.jortartip.controller;


import kg.amanturov.jortartip.dto.ReviewDto;
import kg.amanturov.jortartip.model.Review;
import kg.amanturov.jortartip.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        List<ReviewDto> reviews = reviewService.findAll();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping(value ="/review/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        Review review = reviewService.findById(id);
        if (review != null) {
            return ResponseEntity.ok(review);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/create")
    public ResponseEntity<Review> createReview(@RequestBody ReviewDto reviewDto) {
        Review createdReview = reviewService.save(reviewDto);
        return ResponseEntity.ok(createdReview);
    }

    @PutMapping(value ="/update/{id}")
    public ResponseEntity<Review> updateReview(
            @PathVariable Long id,
            @RequestBody ReviewDto reviewDto
    ) {
        Review updatedReview = reviewService.update(id, reviewDto);
        if (updatedReview != null) {
            return ResponseEntity.ok(updatedReview);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value ="/delete/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
