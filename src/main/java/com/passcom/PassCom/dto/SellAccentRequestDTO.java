package com.passcom.PassCom.dto;

import com.passcom.PassCom.domain.accent.Accent;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Getter
@Setter
public final class SellAccentRequestDTO {
    private final String userId;
    private final String accentId;
    private CompletableFuture<Accent> future;

    public SellAccentRequestDTO(String userId, String accentId) {
        this.userId = userId;
        this.accentId = accentId;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SellAccentRequestDTO) obj;
        return Objects.equals(this.userId, that.userId) &&
                Objects.equals(this.accentId, that.accentId) &&
                Objects.equals(this.future, that.future);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, accentId, future);
    }

    @Override
    public String toString() {
        return "SellAccentRequestDTO[" +
                "userId=" + userId + ", " +
                "accentId=" + accentId + ", " +
                "future=" + future + ']';
    }

}
