package com.passcom.PassCom.domain.accent;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.passcom.PassCom.domain.travel.Travel;
import com.passcom.PassCom.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "travel_id")
    @JsonBackReference
    private Travel travel;
}
