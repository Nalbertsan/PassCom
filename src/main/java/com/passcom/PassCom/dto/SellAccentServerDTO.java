package com.passcom.PassCom.dto;

import java.util.List;

public record SellAccentServerDTO(List<ServersTravelsDTO> serversTravels,
                                  String email, int accentNumber, String origin, String destination) {
}
