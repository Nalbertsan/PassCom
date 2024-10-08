package com.passcom.PassCom.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public final class TravelDTO {

    @NotBlank
    private final String cityOrigin;
    @NotBlank
    private final String cityDestiny;
    @NotBlank
    private final String description;
    @NotNull
    private final double price;
    @NotNull
    private final int numberOfAccents;

    public TravelDTO(String cityOrigin, String cityDestiny, String description, double price, int numberOfAccents) {
        this.cityOrigin = cityOrigin;
        this.cityDestiny = cityDestiny;
        this.description = description;
        this.price = price;
        this.numberOfAccents = numberOfAccents;
    }

    public String cityOrigin() {
        return cityOrigin;
    }

    public String cityDestiny() {
        return cityDestiny;
    }

    public String description() {
        return description;
    }

    public double price() {
        return price;
    }

    public int numberOfAccents() {
        return numberOfAccents;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (TravelDTO) obj;
        return Objects.equals(this.cityOrigin, that.cityOrigin) &&
                Objects.equals(this.cityDestiny, that.cityDestiny) &&
                Objects.equals(this.description, that.description) &&
                Double.doubleToLongBits(this.price) == Double.doubleToLongBits(that.price) &&
                this.numberOfAccents == that.numberOfAccents;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityOrigin, cityDestiny, description, price, numberOfAccents);
    }

    @Override
    public String toString() {
        return "TravelDTO[" +
                "cityOrigin=" + cityOrigin + ", " +
                "cityDestiny=" + cityDestiny + ", " +
                "description=" + description + ", " +
                "price=" + price +
                ", numberOfAccents=" + numberOfAccents +
                + ']';
    }
}
