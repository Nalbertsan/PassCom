package com.passcom.PassCom.service.travel;

import com.passcom.PassCom.domain.accent.Accent;
import com.passcom.PassCom.dto.TravelAndServerDTO;
import lombok.Getter;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RouterService {

    private final Graph<String, TravelEdge> travelGraph;

    public RouterService() {
        this.travelGraph = new DirectedMultigraph<>(TravelEdge.class);
    }

    /**
     * Classe que representa uma aresta (edge) no grafo de viagens, conectando duas cidades.
     * Cada aresta contém informações sobre o servidor, o ID da viagem e a lista de acentos associados.
     */
    public static class TravelEdge extends DefaultEdge {
        private final String server;
        private final String travelId;
        private final List<Accent> accents;

        public TravelEdge(String server, String travelId, List<Accent> accents) {
            this.server = server;
            this.travelId = travelId;
            this.accents = accents;
        }

        public String getServer() { return server; }
        public String getTravelId() { return travelId; }
        public List<Accent> getAccents() { return accents; }
    }

    /**
     * Classe para representar uma rota completa entre cidades, incluindo uma lista de arestas.
     */
        public record Route(String startCity, String endCity, List<TravelEdge> edges) {
    }

    /**
     * Método responsável por construir o grafo de viagens e retornar todas as rotas possíveis entre cidades.
     *
     * @param data Lista de dados de viagens e servidores.
     * @return Uma lista com todas as rotas possíveis.
     */
    public List<Route> buildTravelGraph(List<TravelAndServerDTO> data) {
        // Limpa o grafo para garantir que estamos começando do zero
        travelGraph.removeAllVertices(new HashSet<>(travelGraph.vertexSet()));

        Set<String> cities = new HashSet<>();

        // Construção do grafo
        data.forEach(serverData -> {
            serverData.travel().forEach(travel -> {
                String origin = travel.getCityOrigin();
                String destination = travel.getCityDestiny();

                // Adiciona cidades como nós no grafo
                travelGraph.addVertex(origin);
                travelGraph.addVertex(destination);
                // Adiciona uma aresta entre as cidades com as informações da viagem
                TravelEdge edge = new TravelEdge(
                        serverData.server(),
                        travel.getId(),
                        travel.getAccents()
                );
                travelGraph.addEdge(origin, destination, edge);

                // Armazena todas as cidades para a busca de rotas
                cities.add(origin);
                cities.add(destination);
            });
        });

        // Encontra todas as rotas possíveis entre todas as cidades
        List<Route> allRoutes = new ArrayList<>();
        for (String startCity : cities) {
            for (String endCity : cities) {
                if (!startCity.equals(endCity)) {
                    List<Route> routes = findRoutes(startCity, endCity);
                    allRoutes.addAll(routes);
                }
            }
        }
        return removeDuplicateRoutes(allRoutes);
    }


    /**
     * Método para buscar todas as rotas possíveis entre duas cidades.
     *
     * @param startCity A cidade de origem.
     * @param endCity A cidade de destino.
     * @return Uma lista de rotas entre as cidades fornecidas.
     */
    public List<Route> findRoutes(String startCity, String endCity) {
        List<Route> routes = new ArrayList<>();
        findRoutesDFS(startCity, endCity, new ArrayList<>(), routes);
        return routes;
    }

    /**
     * Algoritmo DFS para encontrar todas as rotas entre duas cidades.
     *
     * @param currentCity A cidade atual durante a busca.
     * @param endCity A cidade de destino.
     * @param path O caminho atual de arestas.
     * @param routes A lista de rotas encontradas.
     */
    private void findRoutesDFS(String currentCity, String endCity, List<TravelEdge> path, List<Route> routes) {
        if (currentCity.equals(endCity)) {
            routes.add(new Route(path.isEmpty() ? currentCity : travelGraph.getEdgeSource(path.get(0)), endCity, new ArrayList<>(path)));
            return;
        }

        travelGraph.outgoingEdgesOf(currentCity).forEach(edge -> {
            String nextCity = travelGraph.getEdgeTarget(edge);
            if (!containsCity(path, nextCity)) {
                path.add(edge);
                findRoutesDFS(nextCity, endCity, path, routes);
                path.remove(path.size() - 1); // Backtracking
            }
        });
    }

    /**
     * Método auxiliar para verificar se a cidade já está presente no caminho atual.
     *
     * @param path O caminho atual de arestas.
     * @param city A cidade a ser verificada.
     * @return True se a cidade estiver no caminho, false caso contrário.
     */
    private boolean containsCity(List<TravelEdge> path, String city) {
        return path.stream().anyMatch(edge -> travelGraph.getEdgeTarget(edge).equals(city) || travelGraph.getEdgeSource(edge).equals(city));
    }

    /**
     * Método para remover rotas duplicadas.
     *
     * @param routes A lista de rotas.
     * @return Uma lista com rotas únicas.
     */
    public static List<Route> removeDuplicateRoutes(List<Route> routes) {
        Set<RouteKey> uniqueRouteKeys = new HashSet<>();
        List<Route> uniqueRoutes = new ArrayList<>();

        for (Route route : routes) {
            RouteKey routeKey = new RouteKey(route.startCity(), route.endCity(), route.edges());
            if (uniqueRouteKeys.add(routeKey)) {  // Adiciona apenas se for único
                uniqueRoutes.add(route);
            }
        }
        return uniqueRoutes;
    }

    /**
     * Classe auxiliar para criar uma chave única para cada rota, usada para identificar rotas duplicadas.
     */
        private record RouteKey(String startCity, String endCity, List<TravelEdge> edges) {

        @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                RouteKey routeKey = (RouteKey) o;
                return Objects.equals(startCity, routeKey.startCity) &&
                        Objects.equals(endCity, routeKey.endCity) &&
                        edgesEqual(edges, routeKey.edges);
            }

            @Override
            public int hashCode() {
                return Objects.hash(startCity, endCity, edgesHash(edges));
            }

            // Método auxiliar para comparar listas de TravelEdge
            private static boolean edgesEqual(List<TravelEdge> edges1, List<TravelEdge> edges2) {
                if (edges1.size() != edges2.size()) return false;
                for (int i = 0; i < edges1.size(); i++) {
                    TravelEdge edge1 = edges1.get(i);
                    TravelEdge edge2 = edges2.get(i);
                    if (!Objects.equals(edge1.getServer(), edge2.getServer()) ||
                            !Objects.equals(edge1.getTravelId(), edge2.getTravelId())) {
                        return false;
                    }
                }
                return true;
            }

            // Método auxiliar para gerar um hash único para a lista de TravelEdge
            private static int edgesHash(List<TravelEdge> edges) {
                return edges.stream()
                        .mapToInt(edge -> Objects.hash(edge.getServer(), edge.getTravelId()))
                        .reduce(1, (a, b) -> 31 * a + b);
            }
        }
}
