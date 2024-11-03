package com.passcom.PassCom.controllers;


import com.passcom.PassCom.domain.travel.Travel;
import com.passcom.PassCom.dto.TravelAndServerDTO;
import com.passcom.PassCom.dto.TravelDTO;
import com.passcom.PassCom.exceptions.TravelNotFoundException;
import com.passcom.PassCom.repostories.TravelRepository;
import com.passcom.PassCom.service.travel.Route;
import com.passcom.PassCom.service.travel.RouteService;
import com.passcom.PassCom.service.travel.TravelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/travels")
@RequiredArgsConstructor
public class TravelController {

    private final TravelService travelService;
    private final RouteService routeService;

    @GetMapping
    public ResponseEntity<List<Travel>> getAllTravels() {
        List<Travel> allTravels = travelService.getAllTravels();
        return ResponseEntity.ok(allTravels);
    }

    @GetMapping("/servers")
    public ResponseEntity<List<Route>> getAllServersTravels() {
        List<TravelAndServerDTO> allServersTravels = travelService.getAllServersTravels();
        List<Route> routes = routeService.findPossibleRoutes(allServersTravels);
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Travel> getUserById(@PathVariable String id) {
        Travel travel = travelService.getTravelById(id);
        return ResponseEntity.ok(travel);
    }

    @PostMapping
    public ResponseEntity<Travel> createTravel(@Valid @RequestBody TravelDTO travelDTO) {
        Travel travel = travelService.createTravel(travelDTO);
        return ResponseEntity.ok(travel);
    }
}
