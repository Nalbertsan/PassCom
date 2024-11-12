package com.passcom.PassCom.service.infra.security;

import com.passcom.PassCom.domain.user.User;
import com.passcom.PassCom.repostories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Serviço personalizado para carregar detalhes do usuário com base no nome de usuário.
 * Implementa a interface {@link UserDetailsService} para uso com Spring Security.
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Carrega um usuário pelo seu nome de usuário (e-mail neste caso).
     *
     * @param username o e-mail do usuário a ser carregado.
     * @return um objeto {@link UserDetails} contendo os detalhes do usuário.
     * @throws UsernameNotFoundException se o usuário não for encontrado.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}
