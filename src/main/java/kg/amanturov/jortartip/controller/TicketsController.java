package kg.amanturov.jortartip.controller;
import kg.amanturov.jortartip.Exceptions.MyFileNotFoundException;
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

    @GetMapping("/byNumber/{number}")
    public ResponseEntity<List<TicketsDto>> findAllByTicketNumber(@PathVariable Integer number) {
        List<TicketsDto> tickets = ticketsService.findAllByTicketNumber(number);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @GetMapping("/uniqueNumbers")
    public ResponseEntity<List<Integer>> getUniqueTicketNumbers() {
        List<Integer> uniqueNumbers = ticketsService.getUniqueTicketNumbers();
        return new ResponseEntity<>(uniqueNumbers, HttpStatus.OK);
    }

    @GetMapping(value ="/ticket/{id}")
    public ResponseEntity<TicketsDto> getTicketById(@PathVariable Long id) {
        return ticketsService.getTicketById(id)
                .map(ticketDto -> new ResponseEntity<>(ticketDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value ="/create")
    public TicketsDto createTicket(@RequestBody TicketsDto ticketsDto) {
        return ticketsService.saveTicket(ticketsDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            ticketsService.deleteTicket(id);
            return ResponseEntity.ok("Applications and associated attachments deleted successfully");
        } catch (MyFileNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting applications: " + e.getMessage());
        }
    }
}
