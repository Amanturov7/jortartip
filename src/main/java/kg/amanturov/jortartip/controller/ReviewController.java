package kg.amanturov.jortartip.controller;


import kg.amanturov.jortartip.Exceptions.MyFileNotFoundException;
import kg.amanturov.jortartip.dto.ApplicationsDto;
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


    @GetMapping(value = "/points")
    public ResponseEntity<List<ReviewDto>> getAllReviewsPoints() {
        List<ReviewDto> reviews = reviewService.findAll();
        return ResponseEntity.ok(reviews);
    }


    @GetMapping("/latest")
    public List<ReviewDto> getLatest4Rewiews() {
        return reviewService.findLatest4Reviews();
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ReviewDto>> getFilteredReviews(
            @RequestParam(name = "ecologicFactorId", required = false) Long ecologicFactorsId,
            @RequestParam(name = "roadSignId", required = false) Long roadSignId,
            @RequestParam(name = "roadId", required = false) Long roadsId,
            @RequestParam(name = "lightId", required = false) Long lightsId,
            @RequestParam(name = "sort", defaultValue = "asc") String sortOrder,
            @RequestParam(name = "page", defaultValue = "1") int page,
            Pageable pageable
    ) {
        Sort sort = Sort.by("id");
        if ("desc".equals(sortOrder)) {
            sort = sort.descending();
        }
        pageable = PageRequest.of(page - 1, pageable.getPageSize(), sort);
        Page<ReviewDto> reviewDtoPage = reviewService.findAllReviewsByFilters(ecologicFactorsId, roadSignId, roadsId,lightsId, pageable);
        return new ResponseEntity<>(reviewDtoPage, HttpStatus.OK);
    }


    @GetMapping(value ="/{id}")
    public ReviewDto getApplicationById(
            @PathVariable Long id) {
        return reviewService.findReviewById(id);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<Review> createReview(@RequestBody ReviewDto reviewDto) {
        Review createdReview = reviewService.save(reviewDto);
        return ResponseEntity.ok(createdReview);
    }


    @PutMapping(value = "/update/status/accept/{id}")
    public void updateStatusAccept(@PathVariable Long id) {
        reviewService.updateStatusAccept(id);
    }
    @PutMapping(value = "/update/status/protocol/{id}")
    public void updateStatusProtocol(@PathVariable Long id) {
        reviewService.updateStatusProtocol(id);
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
