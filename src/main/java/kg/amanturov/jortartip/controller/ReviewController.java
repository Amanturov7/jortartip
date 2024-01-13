package kg.amanturov.jortartip.controller;


import kg.amanturov.jortartip.Exceptions.MyFileNotFoundException;
import kg.amanturov.jortartip.dto.ReviewDto;
import kg.amanturov.jortartip.model.Review;
import kg.amanturov.jortartip.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/all")
    public ResponseEntity<Page<ReviewDto>> getFilteredReviews(
            @RequestParam(name = "ecologicFactorsId", required = false) String ecologicFactors,
            @RequestParam(name = "roadSignId", required = false) String roadSign,
            @RequestParam(name = "lightsId", required = false) String lights,
            @RequestParam(name = "sort", defaultValue = "asc") String sortOrder,
            @RequestParam(name = "page", defaultValue = "1") int page,
            Pageable pageable
    ) {
        Sort sort = Sort.by("id");
        if ("desc".equals(sortOrder)) {
            sort = sort.descending();
        }
        pageable = PageRequest.of(page - 1, pageable.getPageSize(), sort);
        Page<ReviewDto> reviewDtoPage = reviewService.findAllReviewsByFilters(ecologicFactors, roadSign, lights, pageable);
        return new ResponseEntity<>(reviewDtoPage, HttpStatus.OK);
    }


    @GetMapping(value = "/alll")
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        List<ReviewDto> reviews = reviewService.findAll();
        return ResponseEntity.ok(reviews);
    }
    @GetMapping(value ="/{id}")
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            reviewService.deleteReviews(id);
            return ResponseEntity.ok("Applications and associated attachments deleted successfully");
        } catch (MyFileNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting applications: " + e.getMessage());
        }
    }
}
