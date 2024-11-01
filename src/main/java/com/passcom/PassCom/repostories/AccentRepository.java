package com.passcom.PassCom.repostories;


import com.passcom.PassCom.domain.accent.Accent;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccentRepository extends JpaRepository<Accent, String> {
    Optional<Accent> findByUserId(String userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Accent a WHERE a.id = :accentId")
    Optional<Accent> findByIdAndLock(@Param("accentId") String accentId);
}
