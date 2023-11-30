package kg.amanturov.jortartip.service;

import kg.amanturov.jortartip.dto.TicketsDto;
import kg.amanturov.jortartip.model.Tickets;
import kg.amanturov.jortartip.repository.TicketsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketsServiceImpl implements TicketsService {

    private final TicketsRepository repository;
    private final FileStorageService fileStorageService;


    public TicketsServiceImpl(TicketsRepository repository, FileStorageService fileStorageService) {
        this.repository = repository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public List<TicketsDto> getAllTickets() {
        List<Tickets> tickets = repository.findAll();
        return tickets.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TicketsDto> getTicketById(Long id) {
        return repository.findById(id).map(this::convertEntityToDto);
    }


    @Override
    public TicketsDto saveTicket(TicketsDto ticketsDto) {
        Tickets savedTicket = repository.save(convertDtoToEntity(ticketsDto));
        return convertEntityToDto(savedTicket);
    }


    @Override
    public void deleteTicket(Long id) {
        repository.deleteById(id);
    }


    public TicketsDto convertEntityToDto(Tickets ticket) {
        TicketsDto ticketsDto = new TicketsDto();
        ticketsDto.setId(ticket.getId());
        ticketsDto.setQuestion(ticket.getQuestion());
        ticketsDto.setCorrectAnswer(ticket.getCorrectAnswer());
        ticketsDto.setOption1(ticket.getOption1());
        ticketsDto.setOption2(ticket.getOption2());
        ticketsDto.setOption3(ticket.getOption3());
        ticketsDto.setOption4(ticket.getOption4());
        return ticketsDto;
    }

    public Tickets convertDtoToEntity(TicketsDto ticketsDto) {
        Tickets ticket = new Tickets();
        ticket.setId(ticketsDto.getId());
        ticket.setQuestion(ticketsDto.getQuestion());
        ticket.setCorrectAnswer(ticketsDto.getCorrectAnswer());
        ticket.setOption1(ticketsDto.getOption1());
        ticket.setOption2(ticketsDto.getOption2());
        ticket.setOption3(ticketsDto.getOption3());
        ticket.setOption4(ticketsDto.getOption4());

        return ticket;
    }
}
