package kg.amanturov.jortartip.service;


import kg.amanturov.jortartip.dto.EventDto;
import kg.amanturov.jortartip.dto.TicketsDto;
import kg.amanturov.jortartip.model.Tickets;

import java.util.List;
import java.util.Optional;

public interface TicketsService {
    List<TicketsDto> getAllTickets();

    Optional<TicketsDto>getTicketById(Long id);

    List<Integer> getUniqueTicketNumbers();

    TicketsDto findById(Long id);

    TicketsDto saveTicket(TicketsDto ticketsDto);

    void deleteTicket(Long id);

    List<TicketsDto> findAllByTicketNumber(Integer number);
}
