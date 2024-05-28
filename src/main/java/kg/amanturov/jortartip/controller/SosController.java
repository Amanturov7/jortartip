package kg.amanturov.jortartip.controller;

import kg.amanturov.jortartip.dto.SosDto;
import kg.amanturov.jortartip.service.SosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/sos")
public class SosController {

    private final SosService sosService;

    @Autowired
    public SosController(SosService sosService) {
        this.sosService = sosService;
    }

    @PostMapping("/create")
    public ResponseEntity<SosDto> createSos(@RequestBody SosDto sosDto) {
        SosDto createdSos = sosService.createSos(sosDto);
        return new ResponseEntity<>(createdSos, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSos(@PathVariable("id") Long id) {
        sosService.deleteSos(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all")
    public ResponseEntity<List<SosDto>> findAllSos() {
        List<SosDto> sosList = sosService.findAllSos();
        return new ResponseEntity<>(sosList, HttpStatus.OK);
    }

    @GetMapping("/points")
    public ResponseEntity<List<SosDto>> findSosPoints() {
        List<SosDto> sosList = sosService.findAllSos();
        return new ResponseEntity<>(sosList, HttpStatus.OK);
    }

    @GetMapping("/by-id/{userId}")
    public ResponseEntity<List<SosDto>> findByUserId(@PathVariable("userId") Long userId) {
        List<SosDto> sosList = sosService.findByUserId(userId);
        return new ResponseEntity<>(sosList, HttpStatus.OK);
    }
}
