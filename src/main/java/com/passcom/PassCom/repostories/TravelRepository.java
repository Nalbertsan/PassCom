package com.passcom.PassCom.repostories;

import com.passcom.PassCom.domain.travel.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TravelRepository extends JpaRepository<Travel, String> {
    Optional<Travel> findById(String id);

    @Query("SELECT t FROM Travel t LEFT JOIN FETCH t.accents")
    List<Travel> findAllWithAccents();

    @Query("SELECT t FROM Travel t LEFT JOIN FETCH t.accents WHERE t.id = :id")
    Optional<Travel> findWithAccent(@Param("id") String id);
}
