package org.football.fifa_central.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Data
@EqualsAndHashCode
@ToString
public class PlayerStats {
    private String id;
    private int scoredGoals;
    private Instant syncDate;

    private Player player;
    private Season season;
    @JsonIgnore
    private PlayingTime playingTime;
}
