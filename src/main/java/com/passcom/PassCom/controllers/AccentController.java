package com.passcom.PassCom.controllers;

import com.passcom.PassCom.domain.accent.Accent;
import com.passcom.PassCom.dto.ConfirmAccentDTO;
import com.passcom.PassCom.dto.SellAccentServerDTO;
import com.passcom.PassCom.dto.UserAccentDTO;
import com.passcom.PassCom.service.accent.AccentService;
import com.passcom.PassCom.service.accent.SellAccentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/accent")
@RequiredArgsConstructor
public class AccentController {
    @Autowired
    private final AccentService accentService;
    private final SellAccentService sellAccentService;

    @PostMapping("/sell/servers")
    public ResponseEntity<Accent> sellAccentServers(@RequestBody SellAccentServerDTO sellAccentServerDTO) {
        Accent accent = sellAccentService.scheduleAccent(sellAccentServerDTO.serversTravels()
                ,sellAccentServerDTO.email(),sellAccentServerDTO.accentNumber(), sellAccentServerDTO.origin(), sellAccentServerDTO.destination());
        return ResponseEntity.ok(accent);
    }

    @PatchMapping("/sell/{travelId}")
    public ResponseEntity<Accent> sellAccent(@PathVariable("travelId") String travelId, @RequestBody UserAccentDTO userAccentDTO) {
        System.out.println(userAccentDTO);
        Accent accent = accentService.sellAccent(userAccentDTO.getEmail(), userAccentDTO.getAccentNumber() ,travelId);
        return ResponseEntity.ok(accent);
    }

    @PatchMapping("/confirm/{travelId}")
    public ResponseEntity<Accent> confirmAccent(@PathVariable("travelId") String travelId, @RequestBody ConfirmAccentDTO confirmAccentDTO) {
        Accent accent = accentService.confirmAccent(confirmAccentDTO.accentNumber(), travelId, confirmAccentDTO.confirm(), confirmAccentDTO.ticket());
        return ResponseEntity.ok(accent);
    }
}
