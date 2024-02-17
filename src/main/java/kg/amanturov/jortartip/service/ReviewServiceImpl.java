package kg.amanturov.jortartip.service;


import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import kg.amanturov.jortartip.dto.ApplicationsDto;
import kg.amanturov.jortartip.dto.ReviewDto;
import kg.amanturov.jortartip.model.*;
import kg.amanturov.jortartip.repository.AttachmentRepository;
import kg.amanturov.jortartip.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    @PersistenceContext
    private EntityManager entityManager;
    private final ReviewRepository repository;
    private final UserService userService;
    private final AttachmentRepository attachmentRepository;
    private final CommonReferenceService commonReferenceService;

    public ReviewServiceImpl(ReviewRepository repository, UserService userService, AttachmentRepository attachmentRepository, CommonReferenceService commonReferenceService) {
        this.repository = repository;
        this.userService = userService;
        this.attachmentRepository = attachmentRepository;
        this.commonReferenceService = commonReferenceService;
    }

    @Override
    public List<ReviewDto> findAll() {
        List<Review> reviews = repository.findAll();
        return reviews.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDto> findLatest4Reviews() {
        return repository.findTop4ByOrderByCreatedDateDesc().stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }


    @Override
    public ReviewDto findReviewById(Long id) {
        Review review = repository.findById(id).orElse(null);
        if (review != null) {
            return convertEntityToDto(review);
        } else {
            return null;
        }
    }


    @Override
    public void updateStatusAccept(Long id) {
        Review existingReview = repository.findById(id).orElse(null);
        CommonReferenceType commonReferenceType = commonReferenceService.findTypeByCode("009");
        CommonReference reference = commonReferenceService.findByTypeIdAndCode(commonReferenceType.getId(),"2");
        if (existingReview != null) {
            existingReview.setStatus(reference);
            repository.save(existingReview);
        }
    }

    @Override
    public void updateStatusProtocol(Long id) {
        Review existingReview = repository.findById(id).orElse(null);
        CommonReferenceType commonReferenceType = commonReferenceService.findTypeByCode("009");
        CommonReference reference = commonReferenceService.findByTypeIdAndCode(commonReferenceType.getId(),"3");
        if (existingReview != null) {
            existingReview.setStatus(reference);
            repository.save(existingReview);
        }
    }


    @Override
    public Page<ReviewDto> findAllReviewsByFilters(Long ecologicFactorsId, Long roadSignId, Long roadsId, Long lightsId, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Review> query = cb.createQuery(Review.class);
        Root<Review> root = query.from(Review.class);
        Predicate predicate = cb.conjunction();

        if (ecologicFactorsId != null) {
            predicate = cb.and(predicate, cb.equal(root.get("ecologicFactors").get("id"), ecologicFactorsId));
        }
        if (roadSignId != null) {
            predicate = cb.and(predicate, cb.equal(root.get("roadSign").get("id"), roadSignId));
        }
        if (roadsId != null) {
            predicate = cb.and(predicate, cb.equal(root.get("roads").get("id"), roadsId));
        }
        if (lightsId != null) {
            predicate = cb.and(predicate, cb.equal(root.get("lights").get("id"), lightsId));
        }
        query.where(predicate);
        query.select(root);

        if (pageable.getSort().isSorted()) {
            query.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb));
        }

        TypedQuery<Review> typedQuery = entityManager.createQuery(query);

        // Установка пагинации
        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Review> paginatedReviews = typedQuery.getResultList();
        List<ReviewDto> reviewDtos = paginatedReviews.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());

        // Получение общего количества записей для пагинации
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(Review.class)));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(reviewDtos, pageable, totalCount);
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

    }

    @Override
    public void deleteReviews(Long id) {
        Attachments attachment = attachmentRepository.findByReviewsId(id);
        try {
            if (attachment != null){
                Files.delete(Paths.get(attachment.getPath()));
                attachmentRepository.deleteById(attachment.getId());
            }
            repository.deleteById(id);
        } catch (IOException e) {
            throw new RuntimeException("Error deleting attachments: " + e.getMessage());
        }
    }

    private Review convertDtoToEntity(ReviewDto reviewDto) {
        Review review = new Review();
        review.setLat(reviewDto.getLat());
        review.setId(reviewDto.getId());
        review.setLon(reviewDto.getLon());
        review.setLocationAddress(reviewDto.getLocationAddress());
        review.setDescription(reviewDto.getDescription());
        CommonReferenceType commonReferenceType = commonReferenceService.findTypeByCode("009");
        review.setStatus(commonReferenceService.findByTypeIdAndCode(commonReferenceType.getId(),"1"));
        if (reviewDto.getRoadId() != null){
            review.setRoads(commonReferenceService.findById(reviewDto.getRoadId()));
        }
        if (reviewDto.getLightId() != null){
            review.setLights(commonReferenceService.findById(reviewDto.getLightId()));
        }
        if (reviewDto.getRoadSignId() != null){
            review.setRoadSign(commonReferenceService.findById(reviewDto.getRoadSignId()));
        }
        if (reviewDto.getEcologicFactorsId() != null){
        review.setEcologicFactors(commonReferenceService.findById(reviewDto.getEcologicFactorsId()));
        }

        review.setUser(userService.findById(reviewDto.getUserId()));

        return review;
    }

    private ReviewDto convertEntityToDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setLat(review.getLat());
        reviewDto.setId(review.getId());
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
        if(review.getStatus() != null){
            reviewDto.setStatusId(review.getStatus().getId());
            reviewDto.setStatusName(review.getStatus().getTitle());
        }
        if (review.getEcologicFactors() != null) {
            reviewDto.setEcologicFactorsId(review.getEcologicFactors().getId());
        }
        return reviewDto;
    }



}
