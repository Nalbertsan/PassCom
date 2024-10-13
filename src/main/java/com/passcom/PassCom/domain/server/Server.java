package com.passcom.PassCom.domain.server;

import com.passcom.PassCom.domain.user.UserSynchronize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "server")
public class Server {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String path;
    @OneToMany(mappedBy = "server", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSynchronize> listUserSynchronize;
}
