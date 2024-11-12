package com.passcom.PassCom.service.auth;

import com.passcom.PassCom.domain.user.User;
import com.passcom.PassCom.dto.*;
import com.passcom.PassCom.exceptions.InvalidCredentialsException;
import com.passcom.PassCom.exceptions.UserAlreadyExistsException;
import com.passcom.PassCom.exceptions.UserNotFoundException;
import com.passcom.PassCom.repostories.UserRepository;
import com.passcom.PassCom.service.infra.security.TokenSecurity;
import com.passcom.PassCom.service.user.UserService;
import com.passcom.PassCom.service.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {


    @Value("${external.service.url}")
    private String urlServerOne;

    @Value("${secondary.external.service.url}")
    private String urlServerTwo;

    @Autowired
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenSecurity tokenSecurity;
    private final Utils utils;

    /**
     * Construtor do serviço de autenticação.
     *
     * @param userService Serviço de gerenciamento de usuários.
     * @param userRepository Repositório de usuários para operações de banco de dados.
     * @param passwordEncoder Codificador de senhas para criptografia.
     * @param tokenSecurity Serviço de geração de tokens de segurança.
     * @param utils Utilitário para operações auxiliares.
     */
    public AuthService(UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder, TokenSecurity tokenSecurity, Utils utils) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenSecurity = tokenSecurity;
        this.utils = utils;
    }

    /**
     * Realiza o login de um usuário autenticando suas credenciais e gerando um token de acesso.
     *
     * @param body Objeto contendo o email e a senha do usuário.
     * @return Um DTO contendo informações do usuário autenticado e o token de acesso.
     * @throws UserNotFoundException se o usuário não for encontrado.
     * @throws InvalidCredentialsException se o email ou a senha forem inválidos.
     */
    public ResponseAuthDTO login(LoginRequestDTO body) {
        User user = userRepository.findByEmail(body.email())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (passwordEncoder.matches(body.password(), user.getPassword())) {
            String token = tokenSecurity.generateToken(user);
            return new ResponseAuthDTO(user.getName(), user.getEmail(), user.getId(), token);
        } else {
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }

    /**
     * Registra um novo usuário, enviando suas informações para servidores externos e gerando um token de autenticação.
     *
     * @param body DTO contendo as informações de registro do usuário.
     * @return Um DTO com as informações do usuário registrado e o token de acesso.
     * @throws UserAlreadyExistsException se o email já estiver registrado.
     */
    public ResponseAuthDTO register(RegisterRequestDTO body) {
        Optional<User> user = userRepository.findByEmail(body.email());
        if (user.isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }

        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(body.password()));
        newUser.setEmail(body.email());
        newUser.setName(body.name());

        // Gera o token para a comunicação com os outros servidores
        String tokenServer = utils.generateTokenForRequest();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + tokenServer);

        // Cria o DTO para enviar aos outros servidores
        RegisterServerRequestDTO registerServerRequestDTO = new RegisterServerRequestDTO(newUser);

        // URLs dos servidores externos
        List<String> externalServerUrls = List.of(
                urlServerOne + "/auth/register/server",
                urlServerTwo + "/auth/register/server"
        );

        RestTemplate restTemplate = new RestTemplate();

        // Para cada URL de servidor externo, tenta fazer a requisição de registro
        for (String url : externalServerUrls) {
            try {
                // Inclui o DTO na requisição
                HttpEntity<RegisterServerRequestDTO> request = new HttpEntity<>(registerServerRequestDTO, headers);
                ResponseEntity<MessageDTO> response = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        request,
                        new ParameterizedTypeReference<>() {}
                );

                // Se o registro no servidor for bem-sucedido, marca o servidor no novo usuário
                if (response.getStatusCode().is2xxSuccessful()) {
                    if (url.equals(urlServerOne + "/auth/register/server")) {
                        newUser.setServerOne(true);
                    } else if (url.equals(urlServerTwo + "/auth/register/server")) {
                        newUser.setServerTwo(true);
                    }
                }
            } catch (Exception e) {
                System.err.println("Erro ao conectar com o servidor: " + url);

            }
        }

        // Salva o novo usuário no repositório
        userRepository.save(newUser);

        // Gera o token de autenticação para o novo usuário
        String token = tokenSecurity.generateToken(newUser);
        return new ResponseAuthDTO(newUser.getName(), newUser.getEmail(), newUser.getId(), token);
    }



    /**
     * Salva um usuário em servidores externos, definindo-o como registrado em todos os servidores necessários.
     *
     * @param body DTO com as informações do usuário a serem salvas.
     * @return Um DTO de mensagem indicando o sucesso do registro.
     */
    public MessageDTO saveUserServer(RegisterServerRequestDTO body) {
        User user = new User();
        user.setEmail(body.user().getEmail());
        user.setPassword(body.user().getPassword());
        user.setName(body.user().getName());
        user.setServerOne(true);
        user.setServerTwo(true);
        userService.saveUser(user);
        return new MessageDTO("Successfully registered user");
    }
}

