package kg.amanturov.jortartip.service;

import kg.amanturov.jortartip.dto.ViolationsDto;
import kg.amanturov.jortartip.model.Violations;

import java.util.List;

public interface ViolationsService {
    List<Violations> findAll();
    Violations findById(Long id);
    Violations save(Violations violations);
    void deleteById(Long id);
    ViolationsDto convertEntityToDto(Violations violations);

    Violations convertDtoToEntity(ViolationsDto violationsDto);
}
