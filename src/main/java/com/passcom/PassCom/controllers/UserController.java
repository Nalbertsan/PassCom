package com.passcom.PassCom.controllers;

import com.passcom.PassCom.domain.user.User;
import com.passcom.PassCom.dto.UserDTO;
import com.passcom.PassCom.exceptions.UserNotFoundException;
import com.passcom.PassCom.repostories.UserRepository;
import com.passcom.PassCom.service.user.UserService;
import com.passcom.PassCom.service.user.UserSynchronizeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private final UserService userService;
    private final UserSynchronizeService userSynchronizeService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> responseListUsers = userService.getAllUsers();
        return ResponseEntity.ok(responseListUsers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/not-synchronized/{serverId}")
    public ResponseEntity<List<User>> getUsersNotSynchronizedWithServer(@PathVariable String serverId) {
        List<User> users = userSynchronizeService.getUsersNotSynchronizedWithServer(serverId);
        return ResponseEntity.ok(users);
    }

}
