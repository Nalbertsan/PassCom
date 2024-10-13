package com.passcom.PassCom.domain.server;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "server_synchronize")
public class ServerSynchronize {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "server_path_id")
    private Server server_path;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "server_sync_id")
    private Server server_sync;
}
