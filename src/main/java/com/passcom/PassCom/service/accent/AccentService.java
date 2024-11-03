package com.passcom.PassCom.service.accent;

import com.passcom.PassCom.domain.accent.Accent;
import com.passcom.PassCom.domain.ticket.Ticket;
import com.passcom.PassCom.domain.user.User;
import com.passcom.PassCom.exceptions.AccentAlreadySoldException;
import com.passcom.PassCom.exceptions.AccentNotFoundException;
import com.passcom.PassCom.exceptions.UserNotFoundException;
import com.passcom.PassCom.repostories.AccentRepository;
import com.passcom.PassCom.repostories.TicketRepository;
import com.passcom.PassCom.repostories.UserRepository;
import com.passcom.PassCom.service.infra.security.TokenSecurity;
import com.passcom.PassCom.service.travel.RouterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AccentService {

    private final AccentRepository accentRepository;
    private final UserRepository userRepository;
    private final RouterService travelService;
    private final TokenSecurity tokenSecurity;
    private final TicketRepository ticketRepository;

    public AccentService(AccentRepository accentRepository, UserRepository userRepository, RouterService travelService, TokenSecurity tokenSecurity, TicketRepository ticketRepository) {
        this.accentRepository = accentRepository;
        this.userRepository = userRepository;
        this.travelService = travelService;
        this.tokenSecurity = tokenSecurity;
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public Accent sellAccent(String userEmail, int accentNumber ,String travelId) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UserNotFoundException("User not found"));
        Accent accent = accentRepository.findByTravelIdAndNumber(travelId, accentNumber).orElseThrow(() -> new AccentNotFoundException("Accent not found"));

        if (accent.getUser() != null && accent.getStatusConfirmation() == Accent.Status.SOLD) {
            throw new AccentAlreadySoldException("Accent already sold");
        } else if (accent.getUser() != null && accent.getStatusConfirmation() == Accent.Status.RESERVED
                && accent.getExpire().isAfter(LocalDateTime.now())) {
            throw new AccentAlreadySoldException("Accent reserved and not expired");
        }

        accent.setUser(user);
        accent.setExpire(LocalDateTime.now().plusSeconds(30));
        accent.setStatusConfirmation(Accent.Status.RESERVED);
        return accentRepository.save(accent);
    }

    @Transactional
    public Accent confirmAccent(int accentNumber , String travelId, boolean confirm, Ticket ticket) {
        Accent accent = accentRepository.findByTravelIdAndNumber(travelId, accentNumber).orElseThrow(() -> new AccentNotFoundException("Accent not found"));

        if (accent.getUser() == null) {
            throw new UserNotFoundException("User not found");
        } else if (accent.getStatusConfirmation() == Accent.Status.SOLD) {
            throw new AccentAlreadySoldException("Accent already sold");
        } else if (accent.getStatusConfirmation() == Accent.Status.RESERVED
                && accent.getExpire().isBefore(LocalDateTime.now())) {
            throw new AccentAlreadySoldException("Reservation expired");
        }

        if (confirm) {
            if (ticket != null) {
                User user = userRepository.findByEmail(ticket.getUser().getEmail()).orElseThrow(() -> new UserNotFoundException("User not found"));
                ticket.setUser(user);
                ticketRepository.save(ticket);
            }

            accent.setStatusConfirmation(Accent.Status.SOLD);

        } else {
            accent.setStatusConfirmation(Accent.Status.AVAILABLE);
            accent.setUser(null);
            accent.setExpire(null);
        }
        return accentRepository.save(accent);
    }


    public String generateTokenForRequest() {
        User user = new User();
        user.setEmail("email@gmail.com");
        return tokenSecurity.generateToken(user);
    }


}
