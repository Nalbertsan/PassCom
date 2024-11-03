package com.passcom.PassCom.service.accent;

import com.passcom.PassCom.domain.accent.Accent;
import com.passcom.PassCom.dto.ConfirmAccentDTO;
import com.passcom.PassCom.dto.ServersTravelsDTO;
import com.passcom.PassCom.dto.UserAccentDTO;
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

    public SellAccentService(AccentService accentService) {
        this.accentService = accentService;
    }


    public Accent scheduleAccent(List<ServersTravelsDTO> serversTravels, String email, int accentNumber) {
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
                    e.printStackTrace();
                }
            }
        }

        boolean allSuccess = successList.stream().allMatch(Boolean::booleanValue);

        ConfirmAccentDTO confirmAccentDTO = new ConfirmAccentDTO(accentNumber, allSuccess);

        for (ServersTravelsDTO travel : serversTravels) {
            if (Objects.equals(travel.path(), "local")) {
                accentService.confirmAccent(accentNumber, travel.travelId(), allSuccess);
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
                } catch (Exception e) {
                    System.out.println("Erro ao realizar requisição para " + travel.travelId() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return allSuccess ? new Accent() : null;
    }

}
