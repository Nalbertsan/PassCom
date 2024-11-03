package com.passcom.PassCom.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

import java.util.Objects;

public final class UserAccentDTO {

    @NotBlank(message = "Email is required")
    private final String email;


    @NotNull(message = "Accent number is required")
    @Min(value = 1, message = "Accent number must be greater than 0")
    private final int accentNumber;

    public UserAccentDTO(
            @JsonProperty("email") String email,
            @JsonProperty("accentNumber") int accentNumber) {
        this.email = email;
        this.accentNumber = accentNumber;
    }

    public String getEmail() {
        return email;
    }


    public int getAccentNumber() {
        return accentNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UserAccentDTO) obj;
        return Objects.equals(this.email, that.email) &&
                this.accentNumber == that.accentNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, accentNumber);
    }

    @Override
    public String toString() {
        return "UserAccentDTO[" +
                "email=" + email +
                ", accentNumber=" + accentNumber +
                ']';
    }
}
