package kg.amanturov.jortartip.service;

import kg.amanturov.jortartip.dto.TicketsDto;
import kg.amanturov.jortartip.model.Attachments;
import kg.amanturov.jortartip.model.Tickets;
import kg.amanturov.jortartip.repository.AttachmentRepository;
import kg.amanturov.jortartip.repository.TicketsRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketsServiceImpl implements TicketsService {

    private final TicketsRepository repository;
    private final AttachmentRepository attachmentRepository;
    private final CommonReferenceService commonReferenceService;
    private final FileStorageService fileStorageService;


    public TicketsServiceImpl(TicketsRepository repository, AttachmentRepository attachmentRepository, CommonReferenceService commonReferenceService, FileStorageService fileStorageService) {
        this.repository = repository;
        this.attachmentRepository = attachmentRepository;
        this.commonReferenceService = commonReferenceService;
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
    public List<Integer> getUniqueTicketNumbers() {
        return repository.findDistinctTicketNumbers();
    }
    @Override
    public TicketsDto findById(Long id) {
        return repository.findById(id)
                .map(this::convertEntityToDto)
                .orElse(null);
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
        Attachments attachment = attachmentRepository.findByTicketsId(id);
        try {
            if (attachment != null){
                Files.delete(Paths.get(attachment.getPath()));
                attachmentRepository.deleteById(attachment.getId());
            }
            repository.deleteById(id);
        } catch (IOException e) {
            throw new RuntimeException("Error deleting attachments: " + e.getMessage());
        }    }
    @Override
    public List<TicketsDto> findAllByTicketNumber(Integer number) {
        return repository.findAllByTicketNumber(number)
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }
    public TicketsDto convertEntityToDto(Tickets ticket) {
        TicketsDto ticketsDto = new TicketsDto();
        ticketsDto.setId(ticket.getId());
        ticketsDto.setQuestion(ticket.getQuestion());
        ticketsDto.setCorrectAnswer(ticket.getCorrectAnswer());
        ticketsDto.setTicketNumber(ticket.getTicketNumber());
        if(ticket.getTheme()!=null) {
            ticketsDto.setThemeName(ticket.getTheme().getTitle());
            ticketsDto.setThemeId(ticket.getTheme().getId());

        }
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
        ticket.setTicketNumber(ticketsDto.getTicketNumber());
        if(ticketsDto.getThemeId()!=null){
            ticket.setTheme(commonReferenceService.findById(ticketsDto.getThemeId()));
        }
        ticket.setCorrectAnswer(ticketsDto.getCorrectAnswer());
        ticket.setOption1(ticketsDto.getOption1());
        ticket.setOption2(ticketsDto.getOption2());
        ticket.setOption3(ticketsDto.getOption3());
        ticket.setOption4(ticketsDto.getOption4());

        return ticket;
    }
}
