package com.passcom.PassCom.service.utils;

import com.passcom.PassCom.domain.user.User;
import com.passcom.PassCom.service.infra.security.TokenSecurity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class Utils {

    private final TokenSecurity tokenSecurity;

    @Autowired
    public Utils(TokenSecurity tokenSecurity) {
        this.tokenSecurity = tokenSecurity;
    }

    public String generateTokenForRequest() {
        User user = new User();
        user.setEmail("email@gmail.com");
        return tokenSecurity.generateToken(user);
    }
}
