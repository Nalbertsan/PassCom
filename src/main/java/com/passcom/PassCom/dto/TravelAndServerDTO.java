package com.passcom.PassCom.dto;

import com.passcom.PassCom.domain.travel.Travel;

import java.util.List;

public record TravelAndServerDTO(String server, List<Travel> travel) {}
