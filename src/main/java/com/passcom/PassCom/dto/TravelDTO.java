package com.passcom.PassCom.dto;


import java.util.List;

public record TravelDTO(String cityOrigin, String cityDestiny, String description, double price, List<AccentDTO> accents) {}
