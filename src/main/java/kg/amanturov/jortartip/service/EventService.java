package kg.amanturov.jortartip.service;

import kg.amanturov.jortartip.dto.EventDto;
import kg.amanturov.jortartip.model.Event;

import java.util.List;

public interface EventService {


    List<EventDto> findAll();

    EventDto findById(Long id);

    EventDto save(EventDto eventDto);

    void deleteById(Long id);
}
