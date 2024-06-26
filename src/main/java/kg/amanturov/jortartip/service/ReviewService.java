package kg.amanturov.jortartip.service;


import kg.amanturov.jortartip.dto.ApplicationsDto;
import kg.amanturov.jortartip.dto.ReviewDto;
import kg.amanturov.jortartip.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {
    List<ReviewDto> findAll();


    List<ReviewDto> findAllByUserIdAndIsArchived(Boolean bool, Long id);

    List<ReviewDto> findLatest4Reviews();

    ReviewDto findReviewById(Long id);

    void updateStatusAccept(Long id);

    void updateStatusProtocol(Long id);

    Page<ReviewDto> findAllReviewsByFilters(Long ecologicFactors, Long roadSign,Long roads, Long lights, Pageable pageable);

    Review findById(Long id);

    Review save(ReviewDto reviewDto);

    Review update(Long id, ReviewDto reviewDto);

    void delete(Long id);

    void deleteReviews(Long id);
}
