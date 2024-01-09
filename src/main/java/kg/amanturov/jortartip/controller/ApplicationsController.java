package kg.amanturov.jortartip.controller;

import kg.amanturov.jortartip.Exceptions.MyFileNotFoundException;
import kg.amanturov.jortartip.dto.ApplicationsDto;
import kg.amanturov.jortartip.model.Applications;

import kg.amanturov.jortartip.service.ApplicationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/applications")
public class ApplicationsController {

    private final ApplicationsService applicationsService;


    @Autowired
    public ApplicationsController(ApplicationsService applicationsService ) {
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
        return applicationsService.save(applicationsDto);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ApplicationsDto>> getFilteredApplications(
            @RequestParam(name = "typeViolations", required = false) Long typeViolations,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "sort", defaultValue = "asc") String sortOrder,
            @RequestParam(name = "page", defaultValue = "1") int page,
            Pageable pageable
    ) {
        Sort sort = Sort.by("id");
        if ("desc".equals(sortOrder)) {
            sort = sort.descending();
        }
        pageable = PageRequest.of(page - 1, pageable.getPageSize(), sort);
        Page<ApplicationsDto> applicationsDtoPage = applicationsService.findAllApplicationsByFilters(typeViolations, title, pageable);
        return new ResponseEntity<>(applicationsDtoPage, HttpStatus.OK);
    }
    @GetMapping(value ="/{id}")
    public ApplicationsDto getApplicationById(
            @PathVariable Long id) {
        return applicationsService.findApplicationById(id);
    }

    @PutMapping(value = "/update")
    public ApplicationsDto updateApplication(
            @RequestBody ApplicationsDto applicationsDto
    ) {
        return applicationsService.update(applicationsDto);
    }

    @PutMapping(value = "/update/status/accept/{id}")
    public void updateStatusAccept(@PathVariable Long id) {
        applicationsService.updateStatusAccept(id);
    }
    @PutMapping(value = "/update/status/protocol/{id}")
    public void updateStatusProtocol(@PathVariable Long id) {
        applicationsService.updateStatusProtocol(id);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            applicationsService.deleteApplications(id);
            return ResponseEntity.ok("Applications and associated attachments deleted successfully");
        } catch (MyFileNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting applications: " + e.getMessage());
        }
    }

}
