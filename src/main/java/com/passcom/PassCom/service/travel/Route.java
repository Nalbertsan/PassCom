package com.passcom.PassCom.service.travel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Route {
    private String origin;
    private String destination;
    private boolean ability;
    private List<Segment> segments;
}
