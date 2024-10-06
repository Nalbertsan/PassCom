package com.passcom.PassCom.repostories;

import com.passcom.PassCom.domain.travel.Travel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TravelRepository extends JpaRepository<Travel, String> {
    Optional<Travel> findById(String email);
}
