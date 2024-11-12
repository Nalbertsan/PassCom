package com.passcom.PassCom.service.infra.resttemplate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuração para instanciar e disponibilizar um bean do RestTemplate, permitindo injeção de dependência.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Cria e configura uma instância de {@link RestTemplate} para realizar requisições HTTP externas.
     *
     * @return uma instância configurada de {@link RestTemplate}.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
