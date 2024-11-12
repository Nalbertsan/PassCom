package com.passcom.PassCom.service.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.passcom.PassCom.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenSecurity {

    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * Gera um token JWT para o usuário autenticado.
     *
     * Este método utiliza o algoritmo HMAC com a chave secreta para gerar um token JWT. O token contém o e-mail do usuário como
     * o sujeito, um emissor fixo "passcom" e uma data de expiração definida para 2 horas após a criação. O token é assinado e retornado.
     *
     * @param user O usuário para o qual o token JWT será gerado. O e-mail do usuário será usado como o sujeito do token.
     * @return O token JWT gerado.
     * @throws RuntimeException Se ocorrer um erro na criação do token.
     */
    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("passcom")
                    .withSubject(user.getEmail())
                    .withExpiresAt(this.generateExpirationTime())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException(e + "Error generating token");
        }
    }

    /**
     * Valida um token JWT e retorna o e-mail do usuário contido no token.
     *
     * Este método valida o token JWT utilizando o algoritmo HMAC com a chave secreta e verifica o emissor do token. Se o token for válido,
     * o e-mail do usuário (sujeito) é retornado. Caso contrário, o método retorna null.
     *
     * @param token O token JWT a ser validado.
     * @return O e-mail do usuário, se o token for válido; caso contrário, retorna null.
     */
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("passcom")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch ( JWTVerificationException e ) {
            return null;
        }
    }

    /**
     * Gera a data e hora de expiração para o token JWT.
     *
     * Este método define que o token terá uma expiração de 2 horas após o momento atual. A data de expiração é convertida para um Instant,
     * utilizando o fuso horário de -3 horas (horário de Brasília, por exemplo).
     *
     * @return A data de expiração do token.
     */
    private Instant generateExpirationTime() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-3"));
    }
}
