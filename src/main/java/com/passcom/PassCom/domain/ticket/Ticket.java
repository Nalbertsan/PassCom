package com.passcom.PassCom.domain.ticket;


import com.passcom.PassCom.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String origin;
    private String destination;
    private int accentNumber;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "server_one", nullable = false, columnDefinition = "boolean default false")
    private boolean serverOne;
    @Column(name = "server_two", nullable = false, columnDefinition = "boolean default false")
    private boolean serverTwo;
}
