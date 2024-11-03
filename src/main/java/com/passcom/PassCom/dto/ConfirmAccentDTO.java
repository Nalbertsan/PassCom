package com.passcom.PassCom.dto;

import com.passcom.PassCom.domain.ticket.Ticket;

public record ConfirmAccentDTO(int accentNumber , boolean confirm, Ticket ticket) {
}
