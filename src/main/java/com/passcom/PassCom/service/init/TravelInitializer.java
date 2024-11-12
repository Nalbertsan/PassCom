package com.passcom.PassCom.service.init;

import com.passcom.PassCom.domain.travel.Travel;
import com.passcom.PassCom.dto.TravelDTO;
import com.passcom.PassCom.repostories.TravelRepository;
import com.passcom.PassCom.service.travel.TravelService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Classe responsável por inicializar e salvar uma viagem no banco de dados ao iniciar a aplicação.
 *
 * Esta classe implementa a interface {@link CommandLineRunner}, permitindo que o método {@code run} seja executado quando
 * a aplicação for inicializada. O objetivo é verificar se já existe uma viagem entre as cidades de origem e destino, e se não,
 * criar e salvar uma nova viagem utilizando os dados configurados nas propriedades {@code city.origin} e {@code city.destiny}.
 */
@Component
public class TravelInitializer implements CommandLineRunner {

    private final TravelRepository travelRepository;
    private final TravelService travelService;


    @Value("${city.origin}")
    private String cityOrigin;

    @Value("${city.destiny}")
    private String cityDestiny;

    /**
     * Construtor para inicializar o {@link TravelInitializer}.
     *
     * @param travelRepository Repositório de viagens para consultar e salvar viagens no banco de dados.
     * @param travelService Serviço de viagens para criar e gerenciar viagens.
     */
    public TravelInitializer(TravelRepository travelRepository, TravelService travelService) {
        this.travelRepository = travelRepository;
        this.travelService = travelService;
    }

    /**
     * Método executado ao iniciar a aplicação. Verifica se já existe uma viagem entre as cidades configuradas.
     * Caso não exista, cria uma nova viagem e a salva no banco de dados.
     *
     * @param args Argumentos da linha de comando (não utilizados).
     */
    @Override
    @Transactional
    public void run(String... args) {
        if (!travelRepository.existsByCityOriginAndCityDestiny(cityOrigin, cityDestiny)) {
            TravelDTO travelDTO = new TravelDTO(cityOrigin, cityDestiny, "Viagem", 2, 12);
            travelService.createTravel(travelDTO);
            System.out.println("Viagem de " + cityOrigin + " para " + cityDestiny + " salva no banco de dados.");
        } else {
            System.out.println("A viagem de " + cityOrigin + " para " + cityDestiny + " já existe no banco de dados.");
        }
    }
}

