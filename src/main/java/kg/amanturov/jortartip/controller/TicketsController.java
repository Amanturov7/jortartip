package kg.amanturov.jortartip.controller;
import kg.amanturov.jortartip.dto.TicketsDto;
import kg.amanturov.jortartip.service.TicketsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/tickets")
public class TicketsController {

    private final TicketsService ticketsService;

    public TicketsController(TicketsService ticketsService) {
        this.ticketsService = ticketsService;
    }

    @GetMapping
    public ResponseEntity<List<TicketsDto>> getAllTickets() {
        List<TicketsDto> tickets = ticketsService.getAllTickets();
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @GetMapping("/ticket/{id}")
    public ResponseEntity<TicketsDto> getTicketById(@PathVariable Long id) {
        return ticketsService.getTicketById(id)
                .map(ticketDto -> new ResponseEntity<>(ticketDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    public ResponseEntity<TicketsDto> createTicket(@RequestBody TicketsDto ticketsDto) {
        TicketsDto createdTicket = ticketsService.saveTicket(ticketsDto);
        return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketsService.deleteTicket(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
