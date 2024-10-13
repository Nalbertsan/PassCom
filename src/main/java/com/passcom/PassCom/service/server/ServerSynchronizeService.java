package com.passcom.PassCom.service.server;

import com.passcom.PassCom.domain.server.Server;
import com.passcom.PassCom.domain.server.ServerSynchronize;
import com.passcom.PassCom.repostories.ServerSynchronizeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServerSynchronizeService {
    private ServerSynchronizeRepository serverSynchronizeRepository;

    public List<Server> getServersNotSynchronizedWithServer(String path) {
        return serverSynchronizeRepository.findServersNotSynchronizedWithServer(path);
    }

    public ServerSynchronize saveServerSynchronize(ServerSynchronize serverSynchronize) {
        return serverSynchronizeRepository.save(serverSynchronize);
    }

    public List<ServerSynchronize> getAllServerSynchronizes() {
        return serverSynchronizeRepository.findAll();
    }
}
