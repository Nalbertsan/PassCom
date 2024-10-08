package com.passcom.PassCom.controllers;

import com.passcom.PassCom.domain.accent.Accent;
import com.passcom.PassCom.dto.UserAccentDTO;
import com.passcom.PassCom.service.accent.AccentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accent")
@RequiredArgsConstructor
public class AccentController {
    @Autowired
    private final AccentService accentService;

    @PatchMapping("/sell/{accentId}")
    public ResponseEntity<Accent> sellAccent(@PathVariable("accentId") String accentId, @RequestBody @Valid UserAccentDTO userAccentDTO) {
        Accent accent = accentService.sellAccent(userAccentDTO.userId(), accentId);
        return ResponseEntity.ok(accent);
    }
}
