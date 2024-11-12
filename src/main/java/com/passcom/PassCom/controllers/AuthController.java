package com.passcom.PassCom.controllers;

import com.passcom.PassCom.domain.user.User;
import com.passcom.PassCom.dto.*;
import com.passcom.PassCom.exceptions.UserAlreadyExistsException;
import com.passcom.PassCom.exceptions.UserNotFoundException;
import com.passcom.PassCom.repostories.UserRepository;
import com.passcom.PassCom.service.auth.AuthService;
import com.passcom.PassCom.service.infra.security.TokenSecurity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final AuthService authService;

    /**
     * Endpoint para autenticação de login do usuário.
     *
     * @param body Objeto que contém as credenciais de login do usuário.
     * @return ResponseEntity contendo a resposta de autenticação com as informações do token ou erro.
     */
    @PostMapping("/login")
    public ResponseEntity<ResponseAuthDTO> login(@RequestBody LoginRequestDTO body) {
        ResponseAuthDTO response = authService.login(body);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para registro de um novo usuário.
     *
     * @param body Objeto que contém os dados de registro do usuário, validados antes da criação.
     * @return ResponseEntity contendo a resposta de autenticação com as informações do token do novo usuário ou erro.
     */
    @PostMapping("/register")
    public ResponseEntity<ResponseAuthDTO> register(@Valid @RequestBody RegisterRequestDTO body) {
        ResponseAuthDTO response = authService.register(body);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para registrar um novo usuário em múltiplos servidores de viagem.
     *
     * @param body Objeto que contém os dados do servidor a ser registrado.
     * @return ResponseEntity contendo uma mensagem indicando o status do registro do servidor.
     */
    @PostMapping("/register/server")
    public ResponseEntity<MessageDTO> registerServers(@RequestBody RegisterServerRequestDTO body) {
        MessageDTO response = authService.saveUserServer(body);
        return ResponseEntity.ok(response);
    }
}
