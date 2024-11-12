package com.passcom.PassCom.controllers;

import com.passcom.PassCom.domain.accent.Accent;
import com.passcom.PassCom.dto.ConfirmAccentDTO;
import com.passcom.PassCom.dto.SellAccentServerDTO;
import com.passcom.PassCom.dto.UserAccentDTO;
import com.passcom.PassCom.service.accent.AccentService;
import com.passcom.PassCom.service.accent.SellAccentService;
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

    /**
     * Endpoint para vender um assento em múltiplos servidores de viagem.
     *
     * @param sellAccentServerDTO Objeto que contém os dados da venda de assento em servidores,
     *                            incluindo a lista de viagens, email do usuário, número do assento,
     *                            origem e destino.
     * @return ResponseEntity contendo o assento vendido.
     */
    @PostMapping("/sell/servers")
    public ResponseEntity<Accent> sellAccentServers(@RequestBody SellAccentServerDTO sellAccentServerDTO) {
        Accent accent = sellAccentService.scheduleAccent(sellAccentServerDTO.serversTravels()
                ,sellAccentServerDTO.email(),sellAccentServerDTO.accentNumber(), sellAccentServerDTO.origin(), sellAccentServerDTO.destination());
        return ResponseEntity.ok(accent);
    }

    /**
     * Endpoint para vender um assento em uma viagem específica.
     *
     * @param travelId Identificador da viagem.
     * @param userAccentDTO Objeto que contém os dados do usuário e do assento a ser vendido,
     *                      incluindo o email do usuário e o número do assento.
     * @return ResponseEntity contendo o assento vendido.
     */
    @PatchMapping("/sell/{travelId}")
    public ResponseEntity<Accent> sellAccent(@PathVariable("travelId") String travelId, @RequestBody UserAccentDTO userAccentDTO) {
        System.out.println(userAccentDTO);
        Accent accent = accentService.sellAccent(userAccentDTO.getEmail(), userAccentDTO.getAccentNumber() ,travelId);
        return ResponseEntity.ok(accent);
    }

    /**
     * Endpoint para confirmar a venda de um assento em uma viagem específica.
     *
     * @param travelId Identificador da viagem.
     * @param confirmAccentDTO Objeto que contém os dados de confirmação do assento,
     *                         incluindo o número do assento, confirmação e ticket.
     * @return ResponseEntity contendo o assento confirmado.
     */
    @PatchMapping("/confirm/{travelId}")
    public ResponseEntity<Accent> confirmAccent(@PathVariable("travelId") String travelId, @RequestBody ConfirmAccentDTO confirmAccentDTO) {
        Accent accent = accentService.confirmAccent(confirmAccentDTO.accentNumber(), travelId, confirmAccentDTO.confirm(), confirmAccentDTO.ticket());
        return ResponseEntity.ok(accent);
    }
}
