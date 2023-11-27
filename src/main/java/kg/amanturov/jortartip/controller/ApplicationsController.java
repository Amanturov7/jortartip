package kg.amanturov.jortartip.controller;

import kg.amanturov.jortartip.dto.ApplicationsDto;
import kg.amanturov.jortartip.model.Applications;
import kg.amanturov.jortartip.service.ApplicationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/applications")
public class ApplicationsController {

    private final ApplicationsService applicationsService;

    @Autowired
    public ApplicationsController(ApplicationsService applicationsService) {
        this.applicationsService = applicationsService;
    }

    @GetMapping("/user/{userId}")
    public List<ApplicationsDto> getApplicationsByUser(@PathVariable Long userId) {
        List<Applications> applicationsList = applicationsService.findByUser(userId);
        return applicationsList.stream()
                .map(applicationsService::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/user/status/")
    public List<ApplicationsDto> getApplicationsByStatusAndUser(
            @RequestParam (name = "userId") Long userId,
            @RequestParam (name = "status") String status
    ) {
        List<Applications> applicationsList = applicationsService.findByStatusAndUserId(status, userId);
        return applicationsList.stream()
                .map(applicationsService::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/create")
    public ApplicationsDto createApplication(@RequestBody ApplicationsDto applicationsDto) {
        Applications createdApplication = applicationsService.convertDtoToEntity(applicationsDto);
        return applicationsService.convertEntityToDto(applicationsService.save(createdApplication));
    }

    @GetMapping("/all")
    public List<ApplicationsDto> getAllApplications() {
        return applicationsService.findAll();
    }


    @PutMapping("/update/{id}")
    public ApplicationsDto updateApplication(
            @PathVariable Long id,
            @RequestBody ApplicationsDto applicationsDto
    ) {
        Applications existingApplication = applicationsService.findById(id);
        Applications updatedApplication = applicationsService.convertDtoToEntity(applicationsDto);
        updatedApplication.setId(existingApplication.getId());

        return applicationsService.convertEntityToDto(applicationsService.save(updatedApplication));
    }
    @DeleteMapping("/delete/{id}")void delete(Long id)
    {applicationsService.deleteApplications(id);}

}
