package com.passcom.PassCom.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public final class UserAccentDTO {

    @NotBlank(message = "Id is required")
    private final String userid;

    public UserAccentDTO(@JsonProperty("userid") String userId) {
        this.userid = userId;
    }

    public String userId() {
        return userid;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UserAccentDTO) obj;
        return Objects.equals(this.userid, that.userid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userid);
    }

    @Override
    public String toString() {
        return "UserAccentDTO[" +
                "userId=" + userid + ']';
    }

}
