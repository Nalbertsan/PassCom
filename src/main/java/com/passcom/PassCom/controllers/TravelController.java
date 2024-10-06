package com.passcom.PassCom.controllers;


import com.passcom.PassCom.domain.travel.Travel;
import com.passcom.PassCom.exceptions.TravelNotFoundException;
import com.passcom.PassCom.repostories.TravelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/travels")
@RequiredArgsConstructor
public class TravelController {

    private final TravelRepository travelRepository;

    @GetMapping
    public ResponseEntity<List<Travel>> getAllTravels() {
        List<Travel> allTravels = travelRepository.findAll();
        return ResponseEntity.ok(allTravels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Travel> getUserById(@PathVariable String id) {
        Travel travel = travelRepository.findById(id).orElseThrow(() -> new TravelNotFoundException("Travel not found"));
//        return ResponseEntity.ok(new UserDTO(user.getName(), user.getEmail(), user.getId()));
        return ResponseEntity.ok(travel);
    }
}
