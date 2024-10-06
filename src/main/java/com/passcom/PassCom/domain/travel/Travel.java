package com.passcom.PassCom.domain.travel;

import com.passcom.PassCom.domain.city.City;
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
@Table(name = "travel")
public class Travel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne
    @JoinColumn(name = "city_origin_id")
    private City cityOrigin;
    @ManyToOne
    @JoinColumn(name = "city_destiny_id")
    private City cityDestiny;
    private String description;
    private double price;
}
