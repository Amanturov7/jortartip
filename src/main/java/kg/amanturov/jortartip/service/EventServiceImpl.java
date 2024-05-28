package kg.amanturov.jortartip.service;

import kg.amanturov.jortartip.bot.MyTelegramBot;
import kg.amanturov.jortartip.dto.ApplicationsDto;
import kg.amanturov.jortartip.dto.EventDto;
import kg.amanturov.jortartip.model.Applications;
import kg.amanturov.jortartip.model.Event;
import kg.amanturov.jortartip.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CommonReferenceService commonReferenceService;
    private final UserService userService;
    private final MyTelegramBot myTelegramBot;


    public EventServiceImpl(EventRepository eventRepository, CommonReferenceService commonReferenceService, UserService userService, MyTelegramBot myTelegramBot) {
        this.eventRepository = eventRepository;
        this.commonReferenceService = commonReferenceService;
        this.userService = userService;
        this.myTelegramBot = myTelegramBot;
    }

    @Override
    public List<EventDto> findAll() {
        List<Event> events = eventRepository.findAll();
        List<EventDto> eventDtos = new ArrayList<>();
        for (Event event : events) {
            eventDtos.add(convertToDto(event));
        }
        return eventDtos;
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
        String caption = "Событие №: " + event.getId() + "\n" +
                "Тип: " + event.getTitle() + "\n" +
                "Описание: " + event.getDescription() + "\n" +
                "Адрес: " + event.getAddress() + "\n" +
                "Дата: " + event.getStartDate();
        myTelegramBot.sendMessageToChannel(caption);

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
        eventDto.setAddress(event.getAddress());
        eventDto.setLat(event.getLat());
        if (event.getTypeEvent() != null){
            eventDto.setTypeEventId(event.getTypeEvent().getId());
            eventDto.setTypeEventName(event.getTypeEvent().getTitle());
        }
        eventDto.setLon(event.getLon());
        eventDto.setDescription(event.getDescription());
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        eventDto.setStartDate(timestamp);
        eventDto.setEndDate(event.getEndDate());
        eventDto.setUserId(event.getUser().getId());
        return eventDto;
    }

    private Event convertToEntity(EventDto eventDto) {
        Event event = new Event();
        event.setId(eventDto.getId());
        event.setTitle(commonReferenceService.findById(eventDto.getTypeEventId()).getTitle());
        event.setAddress(eventDto.getAddress());
        event.setLat(eventDto.getLat());
        if(eventDto.getTypeEventId() != null) {
            event.setTypeEvent(commonReferenceService.findById(eventDto.getTypeEventId()));
        }
        event.setLon(eventDto.getLon());
        event.setDescription(eventDto.getDescription());
        event.setStartDate(eventDto.getStartDate());
        event.setEndDate(eventDto.getEndDate());
        event.setUser(userService.findById(eventDto.getUserId()));
        return event;
    }
}
