package com.passcom.PassCom.controllers;

import com.passcom.PassCom.domain.user.User;
import com.passcom.PassCom.dto.UserDTO;
import com.passcom.PassCom.exceptions.UserNotFoundException;
import com.passcom.PassCom.repostories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> responseListUsers = users.stream()
                .map(user -> new UserDTO(user.getName(), user.getEmail(),user.getId()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseListUsers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        return ResponseEntity.ok(new UserDTO(user.getName(), user.getEmail(), user.getId()));
    }

}
