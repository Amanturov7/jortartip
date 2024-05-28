package kg.amanturov.jortartip.service;

import kg.amanturov.jortartip.dto.SosDto;

import java.util.List;

public interface SosService {
    SosDto createSos(SosDto sosDto);

    void deleteSos(Long id);

    List<SosDto> findAllSos();

    List<SosDto> findByUserId(Long userId);

    SosDto updateSos(Long id, SosDto sosDto);
}
