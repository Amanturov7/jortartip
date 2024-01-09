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
import kg.amanturov.jortartip.model.Applications;
import kg.amanturov.jortartip.model.Attachments;
import kg.amanturov.jortartip.model.CommonReference;
import kg.amanturov.jortartip.model.CommonReferenceType;
import kg.amanturov.jortartip.repository.ApplicationsRepository;
import kg.amanturov.jortartip.repository.AttachmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationsServiceImpl  implements  ApplicationsService{



    @PersistenceContext
    private EntityManager entityManager;

    private final ApplicationsRepository repository;
    private final UserService userService;
    private final ViolationsService violationsService;
    private final CommonReferenceService commonReferenceService;
    private final AttachmentRepository attachmentRepository;


    public ApplicationsServiceImpl(ApplicationsRepository repository , UserService userService, ViolationsService violationsService, CommonReferenceService commonReferenceService, AttachmentRepository attachmentRepository) {
        this.repository = repository;
        this.userService = userService;
        this.violationsService = violationsService;
        this.commonReferenceService = commonReferenceService;
        this.attachmentRepository = attachmentRepository;
    }


    @Override
    public List<Applications> findByUser(Long id) {
        return repository.findAllByUserId(id);
    }

    @Override
    public List<Applications> findByStatusAndUserId(Long status, Long id) {
        return repository.findApplicationsByStatusIdAndUserId(status, id);
    }


    @Override
    public ApplicationsDto save(ApplicationsDto applicationsDto) {
        return convertEntityToDto(repository.save(convertDtoToEntity(applicationsDto)));
    }
    @Override
    public ApplicationsDto update(ApplicationsDto applicationsDto) {
        Applications applications = new Applications();
        Applications existingApplication = repository.findById(applicationsDto.getId()).orElse(null);
        if (existingApplication != null) {
            applications.setDescription(applicationsDto.getDescription());
            applications.setLat(applicationsDto.getLat());
            applications.setLon(applicationsDto.getLon());
            applications.setPlace(applicationsDto.getPlace());
            applications.setId(applicationsDto.getId());
            applications.setDateOfViolation(applicationsDto.getDateOfViolation());
            applications.setNumberAuto(applicationsDto.getNumberAuto());

            if (applicationsDto.getStatus() != null) {
                applications.setStatus(commonReferenceService.findById(applicationsDto.getStatus()));
            }
            applications.setTitle(applicationsDto.getTitle());
            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
            applications.setUpdateDate(timestamp);
            applications.setCreatedDate(applicationsDto.getCreatedDate());

            if (applicationsDto.getUserId() != null) {
                applications.setUser(userService.findById(applicationsDto.getUserId()));
            }
            if (applicationsDto.getTypeViolationsId() != null) {
                applications.setTypeViolations(violationsService.findById(applicationsDto.getTypeViolationsId()));
            }
        }
        Applications updatedApplication = repository.save(applications);
        return convertEntityToDto(updatedApplication); // Implement this method to convert Applications to ApplicationsDto
    }


    @Override
    public void updateStatusAccept(Long id) {
        Applications existingApplication = repository.findById(id).orElse(null);
        CommonReferenceType commonReferenceType = commonReferenceService.findTypeByCode("009");
        CommonReference reference = commonReferenceService.findByTypeIdAndCode(commonReferenceType.getId(),"2");
        if (existingApplication != null) {
            existingApplication.setStatus(reference);
            repository.save(existingApplication);
        }
    }

    @Override
    public void updateStatusProtocol(Long id) {
        Applications existingApplication = repository.findById(id).orElse(null);
        CommonReferenceType commonReferenceType = commonReferenceService.findTypeByCode("009");
        CommonReference reference = commonReferenceService.findByTypeIdAndCode(commonReferenceType.getId(),"3");
        if (existingApplication != null) {
            existingApplication.setStatus(reference);
            repository.save(existingApplication);
        }
    }


    @Override
    public Applications findById(Long id) {
        return repository.findById(id).orElse(null);
    }


    @Override
    public ApplicationsDto findApplicationById(Long id) {
        Applications applications = repository.findById(id).orElse(null);
        if (applications != null) {
            return convertEntityToDto(applications);
        } else {
            return null;
        }
    }


    @Override
    public void deleteApplications(Long id) {
        Attachments attachment = attachmentRepository.findByApplicationsId(id);
        try {
            Files.delete(Paths.get(attachment.getPath()));
            attachmentRepository.deleteById(attachment.getId());
            repository.deleteById(id);
        } catch (IOException e) {
            throw new RuntimeException("Error deleting attachments: " + e.getMessage());
        }
    }

    @Override
    public List<ApplicationsDto> findAll() {
        List<Applications> applications = repository.findAll();
        List<ApplicationsDto> applicationsDtos = new ArrayList<>();
        for (Applications application : applications) {
            applicationsDtos.add(convertEntityToDto(application));
        }
        return applicationsDtos;
    }

    @Override
    public Page<ApplicationsDto> findAllApplicationsByFilters(Long typeViolations, String title, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Applications> query = cb.createQuery(Applications.class);
        Root<Applications> root = query.from(Applications.class);
        Predicate predicate = cb.conjunction();

        if (typeViolations != null) {
            predicate = cb.and(predicate, cb.equal(root.get("typeViolations").get("id"), typeViolations));
        }
        if (StringUtils.isNotBlank(title)) {
            predicate = cb.and(predicate, cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }
        query.where(predicate);
        query.select(root);

        if (pageable.getSort().isSorted()) {
            query.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb));
        }

        TypedQuery<Applications> typedQuery = entityManager.createQuery(query);
        List<Applications> applications = typedQuery.getResultList();

        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Applications> paginatedApplications = typedQuery.getResultList();
        List<ApplicationsDto> applicationsDtos = paginatedApplications.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(applicationsDtos, pageable, applications.size());
    }



    @Override
    public ApplicationsDto convertEntityToDto(Applications applications) {
        ApplicationsDto applicationsDto = new ApplicationsDto();
        applicationsDto.setId(applications.getId());
        applicationsDto.setDescription(applications.getDescription());
        applicationsDto.setLat(applications.getLat());
        applicationsDto.setLon(applications.getLon());
        applicationsDto.setPlace(applications.getPlace());
        applicationsDto.setTitle(applications.getTypeViolations().getTitle());
        applicationsDto.setCreatedDate(applications.getCreatedDate());
        applicationsDto.setUpdateDate(applications.getUpdateDate());
        applicationsDto.setDateOfViolation(applications.getDateOfViolation());
        applicationsDto.setNumberAuto(applications.getNumberAuto());

        if(applications.getStatus() != null){
            applicationsDto.setStatus(applications.getStatus().getId());
            applicationsDto.setStatusName(applications.getStatus().getTitle());
        }
        if(applications.getRegion() != null){
            applicationsDto.setRegionId(applications.getRegion().getId());
        }
        if(applications.getDistrict() != null){
            applicationsDto.setDistrictId(applications.getDistrict().getId());
        }

        if (applications.getUser() != null) {
            applicationsDto.setUserId(applications.getUser().getId());
        }
        if (applications.getTypeViolations() != null) {
            applicationsDto.setTypeViolationsId(applications.getTypeViolations().getId());
        }

        return applicationsDto;
    }
    @Override
    public Applications convertDtoToEntity(ApplicationsDto applicationsDto) {
        Applications applications = new Applications();
        applications.setId(applicationsDto.getId());
        applications.setDescription(applicationsDto.getDescription());
        applications.setLat(applicationsDto.getLat());
        applications.setLon(applicationsDto.getLon());
        applications.setPlace(applicationsDto.getPlace());
        applications.setDateOfViolation(applicationsDto.getDateOfViolation());
        applications.setNumberAuto(applicationsDto.getNumberAuto());
        CommonReferenceType commonReferenceType = commonReferenceService.findTypeByCode("009");
        applications.setStatus(commonReferenceService.findByTypeIdAndCode(commonReferenceType.getId(),"1"));
        if(applicationsDto.getRegionId() != null){
            applications.setRegion(commonReferenceService.findById(applicationsDto.getRegionId()));
        }
        if(applicationsDto.getDistrictId() != null){
            applications.setDistrict(commonReferenceService.findById(applicationsDto.getDistrictId()));
        }
        applications.setTitle(violationsService.findById(applicationsDto.getTypeViolationsId()).getTitle());

        LocalDate currentDate = LocalDate.now();
        Timestamp timestamp = Timestamp.valueOf(currentDate.atStartOfDay());
        applications.setCreatedDate(timestamp);

        applications.setUpdateDate(applicationsDto.getUpdateDate());

        if (applicationsDto.getUserId() != null) {
            applications.setUser(userService.findById(applicationsDto.getUserId()));
        }
        if (applicationsDto.getTypeViolationsId() != null) {
            applications.setTypeViolations(violationsService.findById(applicationsDto.getTypeViolationsId()));
        }
        return applications;
    }


}
