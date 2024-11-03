package com.passcom.PassCom.service.travel;

import com.passcom.PassCom.domain.accent.Accent;
import com.passcom.PassCom.domain.travel.Travel;
import com.passcom.PassCom.dto.TravelAndServerDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RouteService {

    public List<Route> findPossibleRoutes(List<TravelAndServerDTO> serversData) {
        List<Route> routes = new ArrayList<>();

        // Construir um índice de destino para encontrar viagens que começam no destino de outra viagem
        Map<String, List<TravelInfo>> destinationsMap = new HashMap<>();
        for (TravelAndServerDTO serverData : serversData) {
            for (Travel travel : serverData.travel()) {
                destinationsMap.computeIfAbsent(travel.getCityDestiny(), k -> new ArrayList<>())
                        .add(new TravelInfo(serverData.server(), travel));
            }
        }

        // Percorrer cada viagem e encontrar rotas concatenadas a partir dela
        for (TravelAndServerDTO serverData : serversData) {
            for (Travel travel : serverData.travel()) {
                Route route = buildRoute(travel, serverData.server(), destinationsMap);
                if (route != null) {
                    routes.add(route);
                }
            }
        }

        return routes;
    }

    // Método auxiliar para construir a rota
    // Método auxiliar para construir a rota concatenada
    private Route buildRoute(Travel travel, String startServer, Map<String, List<TravelInfo>> destinationsMap) {
        Route route = new Route();
        route.setOrigin(travel.getCityOrigin());
        route.setSegments(new ArrayList<>());
        route.getSegments().add(new Segment(startServer, travel.getId()));
        route.setAbility(checkAvailability(travel.getAccents()));

        String currentCity = travel.getCityDestiny();
        List<String> visitedCities = new ArrayList<>(); // Para evitar ciclos

        // Procurar trechos conectados ao destino atual
        while (destinationsMap.containsKey(currentCity) && !visitedCities.contains(currentCity)) {
            visitedCities.add(currentCity);
            List<TravelInfo> nextTravels = destinationsMap.get(currentCity);
            String finalCurrentCity = currentCity;
            TravelInfo nextSegment = nextTravels.stream()
                    .filter(t -> t.getTravel().getCityOrigin().equals(finalCurrentCity))
                    .findFirst()
                    .orElse(null);

            if (nextSegment == null) break;

            currentCity = nextSegment.getTravel().getCityDestiny();
            route.getSegments().add(new Segment(nextSegment.getServer(), nextSegment.getTravel().getId()));

            // Atualizar a disponibilidade de assentos para a rota
            if (!checkAvailability(nextSegment.getTravel().getAccents())) {
                route.setAbility(false);
            }
        }

        // Se encontramos uma rota concatenada, define o destino final
        route.setDestination(currentCity);
        return route;
    }


    // Verifica disponibilidade de assentos para uma lista de acentos
    private boolean checkAvailability(List<Accent> accents) {
        return accents.stream().anyMatch(accent -> "AVAILABLE".equals(accent.getStatusConfirmation()));
    }
}

// Classes auxiliares

@Getter
@Setter
@NoArgsConstructor
class ServerData {
    private String server;
    private List<Travel> travels;
}


@Getter
@Setter
@NoArgsConstructor
class TravelInfo {
    private String server;
    private Travel travel;

    public TravelInfo(String server, Travel travel) {
        this.server = server;
        this.travel = travel;
    }
}

@Getter
@Setter
@NoArgsConstructor
class Segment {
    private String server;
    private String travelID;

    public Segment(String server, String travelID) {
        this.server = server;
        this.travelID = travelID;
    }
}
