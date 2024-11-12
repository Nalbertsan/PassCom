package com.passcom.PassCom.controllers;

import com.passcom.PassCom.dto.UserDTO;
import com.passcom.PassCom.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    /**
     * Endpoint para obter todos os usuários.
     *
     * @return ResponseEntity contendo a lista de todos os usuários.
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> responseListUsers = userService.getAllUsers();
        return ResponseEntity.ok(responseListUsers);
    }

    /**
     * Endpoint para obter um usuário específico pelo ID.
     *
     * @param id Identificador do usuário.
     * @return ResponseEntity contendo os dados do usuário encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }


}
