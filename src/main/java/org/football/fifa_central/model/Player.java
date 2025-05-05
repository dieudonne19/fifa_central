package org.football.fifa_central.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
@EqualsAndHashCode
public class Player {
    private String id;
    private String name;
    private String country;
    private int age;
    private int number;
    private Instant syncDate;

    private Positions position;
    private Club club;

    @JsonIgnore
    private List<PlayerStats> playerStats;

    @JsonIgnore
    private Championship championship;
}
