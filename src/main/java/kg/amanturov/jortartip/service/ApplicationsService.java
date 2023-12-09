package kg.amanturov.jortartip.service;

import kg.amanturov.jortartip.dto.ApplicationsDto;
import kg.amanturov.jortartip.model.Applications;
import kg.amanturov.jortartip.model.User;

import java.util.List;

public interface ApplicationsService {
    List<Applications> findByUser(Long id);

    List<Applications> findByStatusAndUserId(Long status, Long id);

    Applications save(Applications application);

    ApplicationsDto update(ApplicationsDto applicationsDto);

    Applications findById(Long id);

    ApplicationsDto convertEntityToDto(Applications applications);

    void deleteApplications(Long id);

    List<ApplicationsDto> findAll();

    Applications convertDtoToEntity(ApplicationsDto applicationsDto);
}
