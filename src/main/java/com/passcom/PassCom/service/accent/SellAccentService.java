package com.passcom.PassCom.service.accent;

import com.passcom.PassCom.domain.accent.Accent;
import com.passcom.PassCom.domain.ticket.Ticket;
import com.passcom.PassCom.domain.user.User;
import com.passcom.PassCom.dto.ConfirmAccentDTO;
import com.passcom.PassCom.dto.ServersTravelsDTO;
import com.passcom.PassCom.dto.UserAccentDTO;
import com.passcom.PassCom.exceptions.UserNotFoundException;
import com.passcom.PassCom.repostories.TicketRepository;
import com.passcom.PassCom.repostories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class SellAccentService {

    private final AccentService accentService;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Value("${external.service.url}")
    private String urlServerOne;

    @Value("${secondary.external.service.url}")
    private String urlServerTwo;

    public SellAccentService(AccentService accentService, TicketRepository ticketRepository, UserRepository userRepository) {
        this.accentService = accentService;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }


    /**
     * Agenda a venda de um assento em múltiplas viagens de servidores, confirmando a transação para cada rota e
     * salvando um ticket local se todas as operações forem bem-sucedidas.
     *
     * @param serversTravels Lista de viagens dos servidores com informações de rota e identificador de viagem.
     * @param email Email do usuário que está reservando o assento.
     * @param accentNumber Número do assento a ser reservado.
     * @param origin Origem da viagem.
     * @param destination Destino da viagem.
     * @return Um novo objeto Accent se todas as transações foram bem-sucedidas, ou null caso contrário.
     * @throws UserNotFoundException se o usuário não for encontrado.
     */
    public Accent scheduleAccent(List<ServersTravelsDTO> serversTravels, String email, int accentNumber, String origin, String destination) {
        List<Boolean> successList = new ArrayList<>();

        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

        String token = accentService.generateTokenForRequest();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);
        UserAccentDTO userAccent = new UserAccentDTO(email, accentNumber);

        for (ServersTravelsDTO travel : serversTravels) {
            if (Objects.equals(travel.path(), "local")) {
                Accent accent = accentService.sellAccent(email, accentNumber, travel.travelId());
                successList.add(accent != null);
            } else {
                try {
                    String url = travel.path() + "/accent/sell/" + travel.travelId();

                    HttpEntity<UserAccentDTO> request = new HttpEntity<>(userAccent, headers);
                    ResponseEntity<Accent> response = restTemplate.exchange(
                            url,
                            HttpMethod.PATCH,
                            request,
                            new ParameterizedTypeReference<Accent>() {}
                    );

                    successList.add(response.getStatusCode() == HttpStatus.OK);
                } catch (Exception e) {
                    successList.add(false);
                    System.out.println("Erro ao realizar requisição para " + travel.travelId() + ": " + e.getMessage());

                }
            }
        }

        boolean allSuccess = successList.stream().allMatch(Boolean::booleanValue);

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        Ticket ticketLocal = new Ticket();
        ticketLocal.setAccentNumber(accentNumber);
        ticketLocal.setOrigin(origin);
        ticketLocal.setDestination(destination);
        ticketLocal.setUser(user);

        Ticket ticketExternal = new Ticket();
        ticketExternal.setAccentNumber(accentNumber);
        ticketExternal.setOrigin(origin);
        ticketExternal.setDestination(destination);
        ticketExternal.setUser(user);
        ticketExternal.setServerTwo(true);
        ticketExternal.setServerOne(true);

        ConfirmAccentDTO confirmAccentDTO = new ConfirmAccentDTO(accentNumber, allSuccess, ticketExternal);

        for (ServersTravelsDTO travel : serversTravels) {
            if (Objects.equals(travel.path(), "local")) {
                accentService.confirmAccent(accentNumber, travel.travelId(), allSuccess, null);
            } else {
                try {
                    String url = travel.path() + "/accent/confirm/" + travel.travelId();
                    HttpEntity<ConfirmAccentDTO> request = new HttpEntity<>(confirmAccentDTO, headers);
                    ResponseEntity<Accent> response = restTemplate.exchange(
                            url,
                            HttpMethod.PATCH,
                            request,
                            new ParameterizedTypeReference<Accent>() {}
                    );
                    if (response.getStatusCode() == HttpStatus.OK && allSuccess) {
                        if (Objects.equals(travel.path(), urlServerOne)) {
                            ticketLocal.setServerOne(true);
                        }
                        if (Objects.equals(travel.path(), urlServerTwo)) {
                            ticketLocal.setServerTwo(true);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Erro ao realizar requisição para " + travel.travelId() + ": " + e.getMessage());

                }
            }
        }

        if (allSuccess) ticketRepository.save(ticketLocal);

        return allSuccess ? new Accent() : null;
    }

}
