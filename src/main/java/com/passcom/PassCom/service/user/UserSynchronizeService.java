package com.passcom.PassCom.service.user;

import com.passcom.PassCom.domain.user.User;
import com.passcom.PassCom.repostories.UserSynchronizeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSynchronizeService {

    private final UserSynchronizeRepository userSynchronizeRepository;

    public UserSynchronizeService(UserSynchronizeRepository userSynchronizeRepository) {
        this.userSynchronizeRepository = userSynchronizeRepository;
    }

    /**
     * Retorna a lista de usuários que ainda não estão sincronizados com o servidor especificado.
     * @param serverId ID do servidor.
     * @return Lista de usuários não sincronizados.
     */
    public List<User> getUsersNotSynchronizedWithServer(String serverId) {
        return userSynchronizeRepository.findUsersNotSynchronizedWithServer(serverId);
    }
}
