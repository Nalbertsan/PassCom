package com.passcom.PassCom.repostories;


import com.passcom.PassCom.domain.accent.Accent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccentRepository extends JpaRepository<Accent, String> {
    Optional<Accent> findByUserId(String userId);
}
