package kg.amanturov.jortartip.service;

import kg.amanturov.jortartip.dto.SosDto;
import kg.amanturov.jortartip.model.Sos;
import kg.amanturov.jortartip.repository.SosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SosServiceImpl implements SosService {

    private final SosRepository sosRepository;
    private final CommonReferenceService commonReferenceService;
    private final UserService userService;

    @Autowired
    public SosServiceImpl(SosRepository sosRepository, CommonReferenceService commonReferenceService, UserService userService) {
        this.sosRepository = sosRepository;
        this.commonReferenceService = commonReferenceService;
        this.userService = userService;
    }



    @Override
    public SosDto createSos(SosDto sosDto) {
        Sos sos = convertDtoToEntity(sosDto);
        Date currentDate = new Date();
        sos.setCreated(new Timestamp(currentDate.getTime()));
        Sos savedSos = sosRepository.save(sos);
        return convertEntityToDto(savedSos);
    }

    @Override
    public void deleteSos(Long id) {
        sosRepository.deleteById(id);
    }

    @Override
    public List<SosDto> findAllSos() {
        List<Sos> allSos = sosRepository.findAll();
        return allSos.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SosDto> findByUserId(Long userId) {
        List<Sos> sosList = sosRepository.findAllByUserId(userId);
        return sosList.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }


    @Override
    public SosDto updateSos(Long id, SosDto sosDto) {
        Sos existingSos = sosRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("SOS с указанным id не найден"));

        existingSos.setTitle(sosDto.getTitle());
        existingSos.setDescription(sosDto.getDescription());
        existingSos.setLat(sosDto.getLat());
        existingSos.setLon(sosDto.getLon());
        existingSos.setAddress(sosDto.getAddress());
        existingSos.setCreated(sosDto.getCreated());
        Date currentDate = new Date();
        existingSos.setUpdated(new Timestamp(currentDate.getTime()));
        Sos updatedSos = sosRepository.save(existingSos);
        return convertEntityToDto(updatedSos);
    }


    public SosDto convertEntityToDto(Sos sos) {
        SosDto sosDto = new SosDto();
        sosDto.setId(sos.getId());
        sosDto.setTitle(sos.getTitle());
        sosDto.setDescription(sos.getDescription());
        sosDto.setLat(sos.getLat());
        sosDto.setLon(sos.getLon());
        sosDto.setAddress(sos.getAddress());
        sosDto.setCreated(sos.getCreated());
        sosDto.setUpdated(sos.getUpdated());
        sosDto.setTypeSosId(sos.getTypeSos().getId());
        sosDto.setTypeSosName(sos.getTypeSos().getTitle());
        sosDto.setUserId(sos.getUser().getId());
        sosDto.setPhone(sos.getUser().getPhone());

        return sosDto;
    }


    public Sos convertDtoToEntity(SosDto sosDto) {
        Sos sos = new Sos();
        sos.setTitle(sosDto.getTitle());
        sos.setDescription(sosDto.getDescription());
        sos.setLat(sosDto.getLat());
        sos.setLon(sosDto.getLon());
        sos.setAddress(sosDto.getAddress());
        sos.setCreated(sosDto.getCreated());
        sos.setUpdated(sosDto.getUpdated());
        sos.setTypeSos(commonReferenceService.findById(sosDto.getTypeSosId()));
        sos.setUser(userService.findById(sosDto.getUserId()));
        return sos;
    }
}
