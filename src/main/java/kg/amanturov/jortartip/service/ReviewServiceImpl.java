package kg.amanturov.jortartip.service;


import kg.amanturov.jortartip.dto.ReviewDto;
import kg.amanturov.jortartip.dto.TicketsDto;
import kg.amanturov.jortartip.model.Review;
import kg.amanturov.jortartip.model.Tickets;
import kg.amanturov.jortartip.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository repository;
    private final UserService userService;
    private final CommonReferenceService commonReferenceService;

    public ReviewServiceImpl(ReviewRepository repository, UserService userService, CommonReferenceService commonReferenceService) {
        this.repository = repository;
        this.userService = userService;
        this.commonReferenceService = commonReferenceService;
    }

    @Override
    public List<Review> findAll() {
        return repository.findAll();
    }

    @Override
    public Review findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Review save(ReviewDto reviewDto) {
        Review review = convertDtoToEntity(reviewDto);
        return repository.save(review);
    }

    @Override
    public Review update(Long id, ReviewDto reviewDto) {
        Review existingReview = repository.findById(id).orElse(null);
        if (existingReview != null) {
            Review updatedReview = convertDtoToEntity(reviewDto);
            updatedReview.setId(existingReview.getId());
            return repository.save(updatedReview);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private Review convertDtoToEntity(ReviewDto reviewDto) {
        Review review = new Review();
        review.setLat(reviewDto.getLat());
        review.setLon(reviewDto.getLon());
        review.setLocationAddress(reviewDto.getLocationAddress());
        review.setDescription(reviewDto.getDescription());
        review.setUser(userService.findById(reviewDto.getUserId()));
        review.setRoads(commonReferenceService.findById(reviewDto.getRoadId()));
        review.setLights(commonReferenceService.findById(reviewDto.getLightId()));
        review.setRoadSign(commonReferenceService.findById(reviewDto.getRoadSignId()));
        return review;
    }

    private ReviewDto convertEntityToDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setLat(review.getLat());
        reviewDto.setLon(review.getLon());
        reviewDto.setLocationAddress(review.getLocationAddress());
        reviewDto.setDescription(review.getDescription());
        if (review.getUser() != null) {
            reviewDto.setUserId(review.getUser().getId());
        }
        if (review.getRoads() != null) {
            reviewDto.setRoadId(review.getRoads().getId());
        }
        if (review.getLights() != null) {
            reviewDto.setLightId(review.getLights().getId());
        }
        if (review.getRoadSign() != null) {
            reviewDto.setRoadSignId(review.getRoadSign().getId());
        }
        return reviewDto;
    }



}
