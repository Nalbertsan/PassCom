package com.passcom.PassCom.service.server;

import com.passcom.PassCom.domain.user.User;
import com.passcom.PassCom.dto.RebaseResponseDTO;
import com.passcom.PassCom.dto.ServerToServerConnectDTO;
import com.passcom.PassCom.repostories.UserRepository;
import com.passcom.PassCom.service.infra.security.TokenSecurity;
import com.passcom.PassCom.service.user.UserService;
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

//    @Autowired
//    private ServerRepository serverRepository;
//    @Autowired
//    private final RestTemplate restTemplate;
//    @Autowired
//    private final TokenSecurity tokenSecurity;
//    @Autowired
//    private ServerSynchronizeService serverSynchronizeService;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private UserService userService;
//
//
//    public List<Server> getAllServers() {
//        return serverRepository.findAll();
//    }
//
//    public Optional<Server> getServerById(String id) {
//        return serverRepository.findById(id);
//    }
//
//    public Server updateServer(String id, Server updatedServer) {
//        return serverRepository.findById(id)
//                .map(server -> {
//                    server.setPath(updatedServer.getPath());
//                    return serverRepository.save(server);
//                })
//                .orElseThrow(() -> new RuntimeException("Server not found with id: " + id));
//    }
//
//    public void deleteServer(String id) {
//        serverRepository.deleteById(id);
//    }
//
//    public ResponseEntity<List<Server>> sendRequestsToServers(ServerToServerConnectDTO serverToServerConnectDTO) {
//        List<Server> servers = serverRepository.findAllByPathNot(serverToServerConnectDTO.path());
//        Server savedServer = serverConnect(serverToServerConnectDTO);
//        if (servers.isEmpty()) {
//            serverConnect(serverToServerConnectDTO);
//            return ResponseEntity.ok(servers);
//        }
//        List<Server> successfulResponses = new ArrayList<>();
//
//        User user = new User();
//        user.setEmail("email@gmail.com");
//        String token = tokenSecurity.generateToken(user);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/json");
//        headers.set("Authorization", "Bearer " + token);
//
//
//        for (Server server : servers) {
//            try {
//                HttpEntity<ServerToServerConnectDTO> request = new HttpEntity<>(serverToServerConnectDTO, headers);
//                String link = server.getPath()+"/server/connect";
//                ResponseEntity<Server> response = restTemplate.exchange(
//                        link,
//                        HttpMethod.POST,
//                        request,
//                        Server.class
//                );
//
//                System.out.println(response.getStatusCode() == HttpStatus.OK);
//                if (response.getStatusCode() == HttpStatus.OK) {
//                    ServerSynchronize serverSynchronize = new ServerSynchronize();
//                    serverSynchronize.setServerPath(savedServer);
//                    serverSynchronize.setServerSync(server);
//                    serverSynchronizeService.saveServerSynchronize(serverSynchronize);
//                    successfulResponses.add(response.getBody());
//                } else {
//                    System.out.println("Falha na requisição para o servidor: " + server.getPath() +
//                            " - Status: " + response.getStatusCode());
//                }
//            } catch (Exception e) {
//                System.out.println("Erro ao enviar requisição para o servidor: " + server.getPath());
//                e.printStackTrace();
//            }
//        }
//
//        return ResponseEntity.ok(servers);
//    }
//
//    public void saveRebase(RebaseResponseDTO rebaseResponseDTO){
//        saveServers(rebaseResponseDTO.servers());
//        userService.saveUsers(rebaseResponseDTO.users());
//    }
//
//    public Server saveServer(Server server) {
//        Optional<Server> existingServer = serverRepository.findByPath(server.getPath());
//
//        if (existingServer.isPresent()) {
//            throw new IllegalArgumentException("Servidor com o caminho " + server.getPath() + " já está cadastrado.");
//        }
//
//        return serverRepository.save(server);
//    }
//
//    public List<Server> saveServers(List<Server> servers) {
//        List<Server> savedServers = new ArrayList<>();
//
//        for (Server server : servers) {
//            try {
//                Server savedServer = saveServer(server);
//                savedServers.add(savedServer);
//            } catch (IllegalArgumentException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//
//        return savedServers;
//    }
//
//
//    public Server serverConnect(ServerToServerConnectDTO serverToServerConnectDTO) {
//        Optional<Server> serverFind = serverRepository.findByPath(serverToServerConnectDTO.path());
//        if (serverFind.isPresent()) {
//            return serverFind.get();
//        }
//        Server server = new Server();
//        server.setPath(serverToServerConnectDTO.path());
//        return serverRepository.save(server);
//    }
}
