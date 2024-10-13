package com.passcom.PassCom.repostories;

import com.passcom.PassCom.domain.server.Server;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServerRepository extends JpaRepository<Server, String> {
    Optional<Server> findById(String id);
    Optional<Server> findByPath(String path);
}
