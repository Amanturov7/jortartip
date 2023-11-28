package kg.amanturov.jortartip.service;


import kg.amanturov.jortartip.dto.ReviewDto;
import kg.amanturov.jortartip.model.Review;

import java.util.List;

public interface ReviewService {
    List<Review> findAll();

    Review findById(Long id);

    Review save(ReviewDto reviewDto);

    Review update(Long id, ReviewDto reviewDto);

    void delete(Long id);
}
