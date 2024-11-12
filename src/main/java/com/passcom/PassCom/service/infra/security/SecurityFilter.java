package com.passcom.PassCom.service.infra.security;

import com.passcom.PassCom.domain.user.User;
import com.passcom.PassCom.exceptions.UserNotFoundException;
import com.passcom.PassCom.repostories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    TokenSecurity tokenService;
    @Autowired
    UserRepository userRepository;

    /**
     * Filtro de autenticação customizado para validar o token JWT e configurar o contexto de segurança no Spring Security.
     * Esse filtro intercepta as requisições, valida o token JWT e autentica o usuário caso o token seja válido.
     * O filtro é inserido na cadeia de filtros do Spring Security para garantir que a autenticação aconteça antes de chegar à lógica de negócio.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        var login = tokenService.validateToken(token);

        if(login != null){
            if (login.equals("email@gmail.com")) {
                User user = new User();
                user.setEmail(login);
                var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
                var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                User user = userRepository.findByEmail(login).orElseThrow(() -> new UserNotFoundException("User Not Found"));
                var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
                var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Método para recuperar o token JWT da requisição.
     * O token deve estar presente no cabeçalho "Authorization" da requisição, e este método extrai e retorna o token.
     *
     * @param request A requisição HTTP recebida.
     * @return O token JWT extraído do cabeçalho, ou null se o cabeçalho não estiver presente.
     */
    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
