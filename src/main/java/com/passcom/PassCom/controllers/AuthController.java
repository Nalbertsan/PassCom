package com.passcom.PassCom.controllers;

import com.passcom.PassCom.domain.user.User;
import com.passcom.PassCom.dto.LoginRequestDTO;
import com.passcom.PassCom.dto.RegisterRequestDTO;
import com.passcom.PassCom.dto.ResponseAuthDTO;
import com.passcom.PassCom.exceptions.UserAlreadyExistsException;
import com.passcom.PassCom.exceptions.UserNotFoundException;
import com.passcom.PassCom.repostories.UserRepository;
import com.passcom.PassCom.service.infra.security.TokenSecurity;
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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenSecurity tokenSecurity;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body){
        User user = this.userRepository.findByEmail(body.email()).orElseThrow(() -> new UserNotFoundException("User not found"));
        if(passwordEncoder.matches(body.password(), user.getPassword())) {
            String token = this.tokenSecurity.generateToken(user);
            return ResponseEntity.ok(new ResponseAuthDTO(user.getName(), user.getEmail(), user.getId() , token));
        }
        return ResponseEntity.badRequest().build();
    }


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body){
        Optional<User> user = this.userRepository.findByEmail(body.email());
        if (user.isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(body.password()));
        newUser.setEmail(body.email());
        newUser.setName(body.name());
        this.userRepository.save(newUser);

        String token = this.tokenSecurity.generateToken(newUser);
        return ResponseEntity.ok(new ResponseAuthDTO(newUser.getName(), newUser.getEmail(), newUser.getId() , token));

    }
}
