package com.passcom.PassCom.repostories;

import com.passcom.PassCom.domain.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, String> {
    List<Ticket> findAllByUserEmail(String email);
}
