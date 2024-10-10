package com.passcom.PassCom.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_synchronize")
public class userSynchronize {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private boolean synchronize;


}
