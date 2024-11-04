package com.passcom.PassCom.controllers;


import com.passcom.PassCom.domain.ticket.Ticket;
import com.passcom.PassCom.domain.travel.Travel;
import com.passcom.PassCom.dto.TravelAndServerDTO;
import com.passcom.PassCom.dto.TravelDTO;
import com.passcom.PassCom.service.travel.RouterService;
import com.passcom.PassCom.service.travel.TravelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/travels")
@RequiredArgsConstructor
public class TravelController {

    private final TravelService travelService;
    private final RouterService routerService;

    @GetMapping
    public ResponseEntity<List<Travel>> getAllTravels() {
        List<Travel> allTravels = travelService.getAllTravels();
        return ResponseEntity.ok(allTravels);
    }

    @GetMapping("/servers")
    public ResponseEntity<List<RouterService.Route>> getAllServersTravels() {
        List<TravelAndServerDTO> allServersTravels = travelService.getAllServersTravels();
        List<RouterService.Route> routes = routerService.buildTravelGraph(allServersTravels);
        return ResponseEntity.ok(routes);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Travel> getUserById(@PathVariable String id) {
        Travel travel = travelService.getTravelById(id);
        return ResponseEntity.ok(travel);
    }

    @GetMapping("/tickets/{email}")
    public ResponseEntity<List<Ticket>> getTickets(@PathVariable String email) {
        List<Ticket> tickets = travelService.getAllTickets(email);
        return ResponseEntity.ok(tickets);
    }

    @PostMapping
    public ResponseEntity<Travel> createTravel(@Valid @RequestBody TravelDTO travelDTO) {
        Travel travel = travelService.createTravel(travelDTO);
        return ResponseEntity.ok(travel);
    }
}
