package kg.amanturov.jortartip.service;

import jakarta.transaction.Transactional;
import kg.amanturov.jortartip.dto.ApplicationsDto;
import kg.amanturov.jortartip.model.Applications;
import kg.amanturov.jortartip.model.CommonReference;
import kg.amanturov.jortartip.model.CommonReferenceType;
import kg.amanturov.jortartip.repository.ApplicationsRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationsServiceImpl  implements  ApplicationsService{

    private final ApplicationsRepository repository;
    private final UserService userService;
    private final ViolationsService violationsService;

    private final CommonReferenceService commonReferenceService;


    public ApplicationsServiceImpl(ApplicationsRepository repository, UserService userService, ViolationsService violationsService, CommonReferenceService commonReferenceService) {
        this.repository = repository;
        this.userService = userService;
        this.violationsService = violationsService;
        this.commonReferenceService = commonReferenceService;
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
    public void deleteApplications(Long id){
         repository.deleteById(id);
    }
    @Override
    public List<ApplicationsDto> findAll() {
        List<Applications> applicationsList = repository.findAll();

        return applicationsList.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
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
