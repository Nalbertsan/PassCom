package com.passcom.PassCom.service.travel;

import com.passcom.PassCom.domain.accent.Accent;
import com.passcom.PassCom.domain.ticket.Ticket;
import com.passcom.PassCom.domain.travel.Travel;
import com.passcom.PassCom.domain.user.User;
import com.passcom.PassCom.dto.TravelAndServerDTO;
import com.passcom.PassCom.dto.TravelDTO;
import com.passcom.PassCom.exceptions.TravelNotFoundException;
import com.passcom.PassCom.repostories.TicketRepository;
import com.passcom.PassCom.repostories.TravelRepository;
import com.passcom.PassCom.repostories.UserRepository;
import com.passcom.PassCom.service.infra.security.TokenSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TravelService {

    @Value("${external.service.url}")
    private String urlServerOne;

    @Value("${secondary.external.service.url}")
    private String urlServerTwo;

    private final TravelRepository travelRepository;
    private final TicketRepository ticketRepository;
    @Autowired
    private final TokenSecurity tokenSecurity;


    public TravelService(TravelRepository travelRepository, TicketRepository ticketRepository, TokenSecurity tokenSecurity) {
        this.travelRepository = travelRepository;
        this.ticketRepository = ticketRepository;
        this.tokenSecurity = tokenSecurity;
    }

    public List<Travel> getAllTravels() {
        List<Travel> travels = travelRepository.findAllWithAccents();
        return travels;
    }

    public List<TravelAndServerDTO> getAllServersTravels() {
        List<Travel> travelsInternal = travelRepository.findAllWithAccents();
        TravelAndServerDTO serversTravels = new TravelAndServerDTO("local", travelsInternal);
        List<TravelAndServerDTO> allTravels = new ArrayList<>(Collections.singleton(serversTravels));

        String token = generateTokenForRequest();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);

        List<String> externalServerUrls = List.of(
                urlServerOne + "/travels",
                urlServerTwo + "/travels"
        );

        // RestTemplate para fazer as requisições
        RestTemplate restTemplate = new RestTemplate();

        // Para cada servidor, fazer a requisição HTTP
        for (String url : externalServerUrls) {
            try {
                HttpEntity<Void> request = new HttpEntity<>(headers);
                ResponseEntity<List<Travel>> response = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        request,
                        new ParameterizedTypeReference<List<Travel>>() {}
                );

                if (response.getStatusCode().is2xxSuccessful()) {
                    List<Travel> externalTravels = response.getBody();
                    if (externalTravels != null) {
                        TravelAndServerDTO serverTravel = new TravelAndServerDTO(url
                                .replaceAll("/travels$", ""), externalTravels);
                        allTravels.add(serverTravel);
                    }
                }
            } catch (Exception e) {
                // Tratamento de erros para servidores que podem não estar disponíveis
                System.err.println("Erro ao conectar com o servidor: " + url);
                e.printStackTrace();
            }
        }

        return allTravels;
    }

    private String generateTokenForRequest() {
        // Gera o token para autenticação
        User user = new User();
        user.setEmail("email@gmail.com");
        return tokenSecurity.generateToken(user);
    }

    public Travel getTravelById(String id) {
        Travel travels = travelRepository.findWithAccent(id).orElseThrow(()-> new TravelNotFoundException("Travel not found"));
        return travels;
    }

    public Travel createTravel(TravelDTO travelDTO) {
        Travel travel = new Travel();
        travel.setPrice(travelDTO.price());
        travel.setCityDestiny(travelDTO.cityDestiny());
        travel.setCityOrigin(travelDTO.cityOrigin());
        travel.setDescription(travelDTO.description());
        List<Accent> accents = new ArrayList<>();
        for (int i = 1; i <= travelDTO.numberOfAccents(); i++) {
            Accent accent = new Accent();
            accent.setNumber(i);
            accent.setTravel(travel);
            accent.setStatusConfirmation(Accent.Status.AVAILABLE);
            accents.add(accent);
        }

        travel.setAccents(accents);

        return travelRepository.save(travel);
    }

    public Travel updateTravel(String id, Travel travelDetails) {
        Travel existingTravel = getTravelById(id);

        existingTravel.setCityOrigin(travelDetails.getCityOrigin());
        existingTravel.setCityDestiny(travelDetails.getCityDestiny());
        existingTravel.setDescription(travelDetails.getDescription());
        existingTravel.setPrice(travelDetails.getPrice());

        return travelRepository.save(existingTravel);
    }

    public void deleteTravel(String id) {
        Travel travel = getTravelById(id);
        travelRepository.delete(travel);
    }

    public List<Ticket> getAllTickets(String email) {
        return ticketRepository.findAllByUserEmail(email);
    }
}
