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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ResponseAuthDTO> login(@RequestBody LoginRequestDTO body) {
        ResponseAuthDTO response = authService.login(body);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseAuthDTO> register(@Valid @RequestBody RegisterRequestDTO body) {
        ResponseAuthDTO response = authService.register(body);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register/server")
    public ResponseEntity<MessageDTO> registerServers(@RequestBody RegisterServerRequestDTO body) {
        MessageDTO response = authService.saveUserServer(body);
        return ResponseEntity.ok(response);
    }
}
