package com.passcom.PassCom.domain.accent;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.passcom.PassCom.domain.travel.Travel;
import com.passcom.PassCom.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "accent")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Accent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private int number;

    @Enumerated(EnumType.STRING)
    private Status statusConfirmation;

    private LocalDateTime expire;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "travel_id")
    @JsonBackReference
    private Travel travel;

    public enum Status {
        AVAILABLE, SOLD, RESERVED
    }
}
