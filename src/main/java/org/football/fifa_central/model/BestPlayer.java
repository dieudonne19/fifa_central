package org.football.fifa_central.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
@EqualsAndHashCode
public class BestPlayer {
    private String id;
    private String name;
    private String country;
    private int age;
    private int number;
    private int scoredGoals;

    private PlayingTime playingTime;

    @JsonIgnore
    private Championship championship;

    private Positions position;

    @JsonProperty("championship")
    private ChampionshipName getChampionship() {
        return this.championship.getName();
    }
}
