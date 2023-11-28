package kg.amanturov.jortartip.service;

import kg.amanturov.jortartip.dto.ApplicationsDto;
import kg.amanturov.jortartip.model.Applications;
import kg.amanturov.jortartip.repository.ApplicationsRepository;
import org.springframework.stereotype.Service;
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
    public List<Applications> findByStatusAndUserId(Long status, Long id ) {
        return repository.findApplicationsByStatusIdAndUserId(status, id);
    }


    @Override
    public Applications save(Applications application) {
        return repository.save(application);
    }

    @Override
    public Applications findById(Long id) {
        return repository.findById(id).orElse(null);
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
        applicationsDto.setTitle(applications.getTitle());
        applicationsDto.setCreatedDate(applications.getCreatedDate());
        applicationsDto.setUpdateDate(applications.getUpdateDate());

        if(applications.getStatus() != null){
            applicationsDto.setStatus(applications.getStatus().getId());
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
        applications.setStatus(commonReferenceService.findById(applicationsDto.getStatus()));
        applications.setTitle(applicationsDto.getTitle());
        applications.setCreatedDate(applicationsDto.getCreatedDate());
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
