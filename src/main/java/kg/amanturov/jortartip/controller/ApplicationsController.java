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

    @GetMapping(value ="/user/{userId}")
    public List<ApplicationsDto> getApplicationsByUser(@PathVariable Long userId) {
        List<Applications> applicationsList = applicationsService.findByUser(userId);
        return applicationsList.stream()
                .map(applicationsService::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value ="/user/status/")
    public List<ApplicationsDto> getApplicationsByStatusAndUser(
            @RequestParam (name = "userId") Long userId,
            @RequestParam (name = "statusId") Long statusId
    ) {
        List<Applications> applicationsList = applicationsService.findByStatusAndUserId(statusId, userId);
        return applicationsList.stream()
                .map(applicationsService::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @PostMapping(value ="/create")
    public ApplicationsDto createApplication(@RequestBody ApplicationsDto applicationsDto) {
        Applications createdApplication = applicationsService.convertDtoToEntity(applicationsDto);
        return applicationsService.convertEntityToDto(applicationsService.save(createdApplication));
    }

    @GetMapping(value ="/all")
    public List<ApplicationsDto> getAllApplications() {
        return applicationsService.findAll();
    }


    @PutMapping(value = "/update")
    public ApplicationsDto updateApplication(
            @RequestBody ApplicationsDto applicationsDto
    ) {
        return applicationsService.update(applicationsDto);
    }


    @DeleteMapping(value = "/delete/{id}")void delete(@PathVariable Long id)
    {applicationsService.deleteApplications(id);}

}
