package org.football.fifa_central.model;


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
    private long scoredGoals;
    private Instant syncDate;

    private Player player;
    private Season season;
    private PlayingTime playingTime;
}
