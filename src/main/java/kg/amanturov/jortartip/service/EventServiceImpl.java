package kg.amanturov.jortartip.service;

import kg.amanturov.jortartip.dto.EventDto;
import kg.amanturov.jortartip.model.Event;
import kg.amanturov.jortartip.repository.EventRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<EventDto> findAll() {
        return eventRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventDto findById(Long id) {
        return eventRepository.findById(id)
                .map(this::convertToDto)
                .orElse(null);
    }

    @Override
    public EventDto save(EventDto eventDto) {
        Event event = convertToEntity(eventDto);
        Event savedEvent = eventRepository.save(event);
        return convertToDto(savedEvent);
    }

    @Override
    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }

    private EventDto convertToDto(Event event) {
        EventDto eventDto = new EventDto();
        eventDto.setId(event.getId());
        eventDto.setTitle(event.getTitle());
        eventDto.setDescription(event.getDescription());
        eventDto.setStartDate(event.getStartDate());
        eventDto.setEndDate(event.getEndDate());
        return eventDto;
    }

    private Event convertToEntity(EventDto eventDto) {
        Event event = new Event();
        event.setId(eventDto.getId());
        event.setTitle(eventDto.getTitle());
        event.setDescription(eventDto.getDescription());
        event.setStartDate(eventDto.getStartDate());
        event.setEndDate(eventDto.getEndDate());
        return event;
    }
}
