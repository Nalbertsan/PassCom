package com.passcom.PassCom.service.server;

import com.passcom.PassCom.domain.server.Server;
import com.passcom.PassCom.domain.server.ServerSynchronize;
import com.passcom.PassCom.domain.user.User;
import com.passcom.PassCom.dto.ServerToServerConnectDTO;
import com.passcom.PassCom.repostories.ServerRepository;
import com.passcom.PassCom.service.infra.security.TokenSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServerService {

    @Autowired
    private ServerRepository serverRepository;
    private final RestTemplate restTemplate;
    private final TokenSecurity tokenSecurity;
    private ServerSynchronizeService serverSynchronizeService;

    public Server saveServer(Server server) {
        return serverRepository.save(server);
    }

    public List<Server> getAllServers() {
        return serverRepository.findAll();
    }

    public Optional<Server> getServerById(String id) {
        return serverRepository.findById(id);
    }

    public Server updateServer(String id, Server updatedServer) {
        return serverRepository.findById(id)
                .map(server -> {
                    server.setPath(updatedServer.getPath());
                    return serverRepository.save(server);
                })
                .orElseThrow(() -> new RuntimeException("Server not found with id: " + id));
    }

    public void deleteServer(String id) {
        serverRepository.deleteById(id);
    }

    public ResponseEntity<List<Server>> sendRequestsToServers(String path) {
        List<Server> servers = serverRepository.findAll();
        List<Server> successfulResponses = new ArrayList<>();

        User user = new User();
        user.setEmail("email@gmail.com");
        String token = tokenSecurity.generateToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);

        Server serverAux = new Server();
        serverAux.setPath(path);

        for (Server server : servers) {
            try {
                ServerToServerConnectDTO serverToServerConnectDTO = new ServerToServerConnectDTO(path);

                HttpEntity<ServerToServerConnectDTO> request = new HttpEntity<>(serverToServerConnectDTO, headers);
                String link = server.getPath()+"/server/connect";
                ResponseEntity<Server> response = restTemplate.exchange(
                        link,
                        HttpMethod.POST,
                        request,
                        Server.class
                );

                if (response.getStatusCode() == HttpStatus.OK) {
                    ServerSynchronize serverSynchronize = new ServerSynchronize();
                    serverSynchronize.setServer_path(serverAux);
                    serverSynchronize.setServer_sync(server);
                    serverSynchronizeService.saveServerSynchronize(serverSynchronize);
                    successfulResponses.add(response.getBody());
                } else {
                    System.out.println("Falha na requisição para o servidor: " + server.getPath() +
                            " - Status: " + response.getStatusCode());
                }
            } catch (Exception e) {
                // Trata exceções, como falhas na conexão ou outros erros
                System.out.println("Erro ao enviar requisição para o servidor: " + server.getPath());
                e.printStackTrace();
            }
        }

        // Retorna a lista de servidores que receberam respostas bem-sucedidas
        if (!successfulResponses.isEmpty()) {
            return ResponseEntity.ok(successfulResponses);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public Server serverConnect(ServerToServerConnectDTO serverToServerConnectDTO) {
        Optional<Server> serverFind = serverRepository.findByPath(serverToServerConnectDTO.path());
        if (serverFind.isPresent()) {
            return null;
        }
        Server server = new Server();
        server.setPath(serverToServerConnectDTO.path());
        return serverRepository.save(server);
    }
}
