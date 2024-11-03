package com.passcom.PassCom.service.accent;

import com.passcom.PassCom.domain.accent.Accent;
import com.passcom.PassCom.domain.user.User;
import com.passcom.PassCom.dto.ServersTravelsDTO;
import com.passcom.PassCom.dto.UserAccentDTO;
import com.passcom.PassCom.exceptions.AccentAlreadySoldException;
import com.passcom.PassCom.exceptions.AccentNotFoundException;
import com.passcom.PassCom.exceptions.UserNotFoundException;
import com.passcom.PassCom.repostories.AccentRepository;
import com.passcom.PassCom.repostories.UserRepository;
import com.passcom.PassCom.service.infra.security.TokenSecurity;
import com.passcom.PassCom.service.travel.TravelService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AccentService {

    private final AccentRepository accentRepository;
    private final UserRepository userRepository;
    private final TravelService travelService;
    private final TokenSecurity tokenSecurity;

    public AccentService(AccentRepository accentRepository, UserRepository userRepository, TravelService travelService, TokenSecurity tokenSecurity) {
        this.accentRepository = accentRepository;
        this.userRepository = userRepository;
        this.travelService = travelService;
        this.tokenSecurity = tokenSecurity;
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
    public Accent confirmAccent(int accentNumber ,String travelId, boolean confirm) {
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
