package com.passcom.PassCom.repostories;

import com.passcom.PassCom.domain.server.Server;
import com.passcom.PassCom.domain.server.ServerSynchronize;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServerSynchronizeRepository extends JpaRepository<ServerSynchronize, String> {

    @Query("SELECT s FROM Server s WHERE s.id NOT IN " +
            "(SELECT us.server_path.id FROM ServerSynchronize us WHERE us.server_sync.id = :serverId)")
    List<Server> findServersNotSynchronizedWithServer(@Param("serverId") String serverId);
}
