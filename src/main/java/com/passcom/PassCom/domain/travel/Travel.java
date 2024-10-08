package com.passcom.PassCom.domain.travel;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.passcom.PassCom.domain.accent.Accent;
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
@Table(name = "travel")
public class Travel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String cityOrigin;
    private String cityDestiny;
    private String description;
    private double price;

    @OneToMany(mappedBy = "travel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Accent> accents;
}
