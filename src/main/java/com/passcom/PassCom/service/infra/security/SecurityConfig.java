package com.passcom.PassCom.service.infra.security;

import com.passcom.PassCom.service.infra.security.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    SecurityFilter securityFilter;

    /**
     * Configuração de segurança do Spring, incluindo regras de autorização, CORS e gerenciamento de sessão.
     * Esta configuração é usada para garantir a segurança da aplicação, desabilitando CSRF,
     * configurando o gerenciamento de sessão como sem estado (stateless), permitindo acesso
     * público às rotas de login e registro, e exigindo autenticação para outras rotas.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(Customizer.withDefaults());
        ;

        return http.build();
    }

    /**
     * Bean para o codificador de senha {@link PasswordEncoder}, utilizado para criptografar senhas.
     * Neste caso, é utilizado o {@link BCryptPasswordEncoder} para garantir a segurança das senhas.
     *
     * @return o codificador de senha configurado.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean para o {@link AuthenticationManager} necessário para autenticação de usuários.
     * Este gerenciador é usado para validar credenciais e fornecer um contexto de segurança para o Spring Security.
     *
     * @param authenticationConfiguration a configuração de autenticação que fornece o gerenciador de autenticação.
     * @return o {@link AuthenticationManager} configurado.
     * @throws Exception se ocorrer um erro durante a configuração do gerenciador de autenticação.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configuração CORS para permitir que a aplicação frontend interaja com o backend.
     * Permite que o frontend da aplicação em {@code http://localhost:5173} faça requisições ao backend.
     * As configurações incluem os métodos HTTP permitidos e os cabeçalhos.
     *
     * @return a configuração de CORS para a aplicação.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
