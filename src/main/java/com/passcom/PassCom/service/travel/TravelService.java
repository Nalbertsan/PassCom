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

    /**
     * Recupera todas as viagens do banco de dados local.
     *
     * @return Uma lista com todas as viagens, incluindo seus acentos.
     */
    public List<Travel> getAllTravels() {
        List<Travel> travels = travelRepository.findAllWithAccents();
        return travels;
    }

    /**
     * Recupera as viagens do servidor local e de servidores externos.
     * Obtém as viagens do repositório local e faz requisições HTTP GET
     * para servidores externos para buscar viagens adicionais.
     *
     * @return Uma lista de TravelAndServerDTO, incluindo viagens do servidor local e de servidores externos.
     */
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
            }
        }

        return allTravels;
    }

    /**
     * Gera um token para autenticar requisições aos servidores externos.
     * O token é criado para um usuário fictício com um e-mail fixo.
     *
     * @return Um token gerado.
     */
    private String generateTokenForRequest() {
        // Gera o token para autenticação
        User user = new User();
        user.setEmail("email@gmail.com");
        return tokenSecurity.generateToken(user);
    }

    /**
     * Recupera uma viagem pelo seu ID no banco de dados.
     *
     * @param id O ID da viagem.
     * @return O objeto viagem.
     * @throws TravelNotFoundException Se a viagem não for encontrada.
     */
    public Travel getTravelById(String id) {
        Travel travels = travelRepository.findWithAccent(id).orElseThrow(()-> new TravelNotFoundException("Travel not found"));
        return travels;
    }

    /**
     * Cria uma nova viagem utilizando os detalhes fornecidos.
     *
     * @param travelDTO Os detalhes da viagem a ser criada.
     * @return A entidade de viagem criada.
     */
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
    /**
     * Atualiza uma viagem existente com novos detalhes.
     *
     * @param id O ID da viagem a ser atualizada.
     * @param travelDetails Os novos detalhes da viagem.
     * @return A entidade de viagem atualizada.
     */
    public Travel updateTravel(String id, Travel travelDetails) {
        Travel existingTravel = getTravelById(id);

        existingTravel.setCityOrigin(travelDetails.getCityOrigin());
        existingTravel.setCityDestiny(travelDetails.getCityDestiny());
        existingTravel.setDescription(travelDetails.getDescription());
        existingTravel.setPrice(travelDetails.getPrice());

        return travelRepository.save(existingTravel);
    }

    /**
     * Exclui uma viagem do banco de dados pelo seu ID.
     *
     * @param id O ID da viagem a ser excluída.
     */
    public void deleteTravel(String id) {
        Travel travel = getTravelById(id);
        travelRepository.delete(travel);
    }

    /**
     * Recupera todos os ingressos associados a um usuário pelo seu e-mail.
     *
     * @param email O e-mail do usuário cujos ingressos serão recuperados.
     * @return Uma lista de ingressos associados ao e-mail fornecido.
     */
    public List<Ticket> getAllTickets(String email) {
        return ticketRepository.findAllByUserEmail(email);
    }
}
