package com.passcom.PassCom.controllers;


import com.passcom.PassCom.domain.user.User;
import com.passcom.PassCom.dto.*;
import com.passcom.PassCom.service.infra.security.TokenSecurity;
import com.passcom.PassCom.service.server.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
public class ServerController {

    private final RestTemplate restTemplate;
    private final ServerService serverService;
    private final TokenSecurity tokenSecurity;

    @Value("${external.service.url}")
    private String urlServerOne;

    @Value("secondary.external.service.url")
    private String urlServerTwo;




//    @GetMapping("/{id}")
//    public ResponseEntity<Server> getServerById(@PathVariable String id) {
//        Optional<Server> server = serverService.getServerById(id);
//        return server.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    @PostMapping
//    public ResponseEntity<Server> createServer(@RequestBody ServerCreateDTO serverDTO) {
//        Server server = new Server();
//        server.setPath(serverDTO.path());
//        Server createdServer = serverService.saveServer(server);
//        return ResponseEntity.ok(createdServer);
//    }
//
//    @PostMapping("/connect")
//    public ResponseEntity<MessageDTO> connectServer(@RequestBody RebaseResponseDTO rebaseResponseDTO) {
//        serverService.saveRebase(rebaseResponseDTO);
//        MessageDTO messageDTO = new MessageDTO("Ok");
//        return ResponseEntity.ok(messageDTO);
//    }
//
//    @PostMapping("/connect-servers")
//    public ResponseEntity<List<Server>> connectServers(@RequestBody ServerToServerConnectDTO serverToServerConnectDTO) {
//        return serverService.sendRequestsToServers(serverToServerConnectDTO);
//    }



//    @PostMapping("/connect-start")
//    public ResponseEntity <List<Server>> connectStart(@RequestBody ServerToServerCreateDTO serverToServerCreateDTO) {
//
//        ServerToServerConnectDTO serverExternalDTO = new ServerToServerConnectDTO(serverToServerCreateDTO.pathExternal());
//        serverService.serverConnect(serverExternalDTO);
//
//        String externalApiUrl = serverToServerCreateDTO.pathExternal()+"/server/connect-servers";
//        User user = new User();
//        user.setEmail("email@gmail.com");
//        String token = tokenSecurity.generateToken(user);
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/json");
//        headers.set("Authorization", "Bearer " + token);
//        ServerToServerConnectDTO serverToServerConnectDTO = new ServerToServerConnectDTO(serverToServerCreateDTO.pathInternal());
//        HttpEntity<ServerToServerConnectDTO> request = new HttpEntity<>(serverToServerConnectDTO, headers);
//
//        ResponseEntity<List<Server>> response = restTemplate.exchange(
//                externalApiUrl,
//                HttpMethod.POST,
//                request,
//                new ParameterizedTypeReference<List<Server>>() {}
//        );
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//            List<Server> servers = response.getBody();
//            if (servers != null && !servers.isEmpty()) {
//
//                List<Server> serversToSave = servers.stream()
//                        .filter(server -> !serverRepository.existsByPath(server.getPath()))
//                        .collect(Collectors.toList());
//
//                if (!serversToSave.isEmpty()) {
//                    serverRepository.saveAll(serversToSave);
//                }
//            }
//            return ResponseEntity.ok(response.getBody());
//        } else {
//            return ResponseEntity.status(response.getStatusCode()).build();
//        }
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Server> updateServer(@PathVariable String id, @RequestBody Server updatedServer) {
//        Server server = serverService.updateServer(id, updatedServer);
//        return ResponseEntity.ok(server);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteServer(@PathVariable String id) {
//        serverService.deleteServer(id);
//        return ResponseEntity.noContent().build();
//    }
}
