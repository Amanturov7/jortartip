package kg.amanturov.jortartip.controller;

import kg.amanturov.jortartip.dto.ViolationsDto;
import kg.amanturov.jortartip.model.Violations;
import kg.amanturov.jortartip.service.ViolationsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/violations")
public class ViolationsController {

    private final ViolationsService violationsService;

    public ViolationsController(ViolationsService violationsService) {
        this.violationsService = violationsService;
    }

    @GetMapping
    public ResponseEntity<List<ViolationsDto>> getAllViolations() {
        List<Violations> violationsList = violationsService.findAll();
        List<ViolationsDto> violationsDtoList = violationsList.stream()
                .map(violationsService::convertEntityToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(violationsDtoList, HttpStatus.OK);
    }

    @GetMapping(value ="/{id}")
    public ResponseEntity<ViolationsDto> getViolationsById(@PathVariable Long id) {
        Violations violations = violationsService.findById(id);
        if (violations != null) {
            ViolationsDto violationsDto = violationsService.convertEntityToDto(violations);
            return new ResponseEntity<>(violationsDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value ="/create")
    public ResponseEntity<ViolationsDto> createViolations(@RequestBody ViolationsDto violationsDto) {
        Violations violationsEntity = violationsService.convertDtoToEntity(violationsDto);
        Violations createdViolations = violationsService.save(violationsEntity);
        ViolationsDto createdViolationsDto = violationsService.convertEntityToDto(createdViolations);
        return new ResponseEntity<>(createdViolationsDto, HttpStatus.CREATED);
    }

    @PutMapping(value ="/update/{id}")
    public ResponseEntity<ViolationsDto> updateViolations(
            @PathVariable Long id,
            @RequestBody ViolationsDto violationsDto
    ) {
        Violations existingViolations = violationsService.findById(id);
        if (existingViolations != null) {
            Violations updatedViolations = violationsService.convertDtoToEntity(violationsDto);
            updatedViolations.setId(existingViolations.getId());
            Violations savedViolations = violationsService.save(updatedViolations);
            ViolationsDto savedViolationsDto = violationsService.convertEntityToDto(savedViolations);
            return new ResponseEntity<>(savedViolationsDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value ="/delete/{id}")
    public ResponseEntity<Void> deleteViolations(@PathVariable Long id) {
        violationsService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
