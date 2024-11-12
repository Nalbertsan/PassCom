package com.passcom.PassCom.service.infra.cors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração de CORS para a aplicação, permitindo requisições de origens específicas e métodos HTTP.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    /**
     * Configura os mapeamentos de CORS para a aplicação, permitindo acesso de um front-end específico.
     *
     * @param registry O registro de mapeamento de CORS.
     */
    @Override
    public void addCorsMappings (CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

