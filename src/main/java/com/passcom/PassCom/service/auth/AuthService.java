package com.passcom.PassCom.service.auth;

import com.passcom.PassCom.domain.user.User;
import com.passcom.PassCom.dto.LoginRequestDTO;
import com.passcom.PassCom.dto.RegisterRequestDTO;
import com.passcom.PassCom.dto.ResponseAuthDTO;
import com.passcom.PassCom.exceptions.InvalidCredentialsException;
import com.passcom.PassCom.exceptions.UserAlreadyExistsException;
import com.passcom.PassCom.exceptions.UserNotFoundException;
import com.passcom.PassCom.repostories.UserRepository;
import com.passcom.PassCom.service.infra.security.TokenSecurity;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenSecurity tokenSecurity;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenSecurity tokenSecurity) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenSecurity = tokenSecurity;
    }

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

    public ResponseAuthDTO register(RegisterRequestDTO body) {
        Optional<User> user = userRepository.findByEmail(body.email());
        if (user.isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }

        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(body.password()));
        newUser.setEmail(body.email());
        newUser.setName(body.name());
        userRepository.save(newUser);

        String token = tokenSecurity.generateToken(newUser);
        return new ResponseAuthDTO(newUser.getName(), newUser.getEmail(), newUser.getId(), token);
    }
}

