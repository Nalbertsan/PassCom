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

    /**
     * Recupera todos os usuários do banco de dados e os retorna como uma lista de UserDTO.
     *
     * @return Lista de UserDTO representando todos os usuários.
     */
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserDTO(user.getName(), user.getEmail(), user.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Recupera um usuário pelo seu ID e o retorna como UserDTO.
     *
     * @param id O ID do usuário a ser recuperado.
     * @return UserDTO representando o usuário.
     * @throws UserNotFoundException Se o usuário não for encontrado.
     */
    public UserDTO getUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        return new UserDTO(user.getName(), user.getEmail(), user.getId());
    }

    /**
     * Salva um novo usuário no banco de dados, garantindo que o e-mail seja único.
     *
     * @param user O objeto User a ser salvo.
     * @return O objeto User salvo.
     * @throws UserAlreadyExistsException Se o e-mail do usuário já estiver cadastrado.
     */
    public User saveUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("Usuário com email " + user.getEmail() + " já está cadastrado.");
        }
        return userRepository.save(user);
    }

    /**
     * Salva uma lista de usuários no banco de dados, ignorando aqueles que já existem.
     * Para cada usuário existente, uma mensagem é exibida no console.
     *
     * @param users A lista de usuários a ser salva.
     * @return Lista de usuários que foram salvos com sucesso.
     */
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
