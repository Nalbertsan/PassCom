package com.passcom.PassCom.repostories;

import com.passcom.PassCom.domain.user.User;
import com.passcom.PassCom.domain.user.UserSynchronize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserSynchronizeRepository extends JpaRepository<UserSynchronize, String> {
    @Query("SELECT u FROM User u WHERE u.id NOT IN (SELECT us.user.id FROM UserSynchronize us WHERE us.server.id = :serverId)")
    List<User> findUsersNotSynchronizedWithServer(@Param("serverId") String serverId);
}
