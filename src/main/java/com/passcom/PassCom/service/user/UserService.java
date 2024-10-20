package com.passcom.PassCom.service.user;

import com.passcom.PassCom.domain.user.User;
import com.passcom.PassCom.dto.UserDTO;
import com.passcom.PassCom.exceptions.UserAlreadyExistsException;
import com.passcom.PassCom.exceptions.UserNotFoundException;
import com.passcom.PassCom.repostories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserDTO(user.getName(), user.getEmail(), user.getId()))
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        return new UserDTO(user.getName(), user.getEmail(), user.getId());
    }

    public User saveUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("Usuário com email " + user.getEmail() + " já está cadastrado.");
        }
        return userRepository.save(user);
    }

    public List<User> saveUsers(List<User> users) {
        List<User> savedUsers = new ArrayList<>();

        for (User user : users) {
            try {
                User savedUser = saveUser(user);
                savedUsers.add(savedUser);
            } catch (UserAlreadyExistsException e) {
                System.out.println(e.getMessage());
            }
        }

        return savedUsers;
    }

}
