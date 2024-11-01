package com.passcom.PassCom.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_aplication")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String email;
    private String name;
    private String password;
    @Column(name = "server_one", nullable = false, columnDefinition = "boolean default false")
    private boolean serverOne;
    @Column(name = "server_two", nullable = false, columnDefinition = "boolean default false")
    private boolean serverTwo;
}
