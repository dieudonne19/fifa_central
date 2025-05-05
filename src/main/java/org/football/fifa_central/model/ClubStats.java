package org.football.fifa_central.model;

import lombok.*;

import java.time.Instant;

@AllArgsConstructor@NoArgsConstructor@Setter@Getter@EqualsAndHashCode@ToString
public class ClubStats {
    private Club club;
    private int rankingPoints;
    private int scoredGoals;
    private int concededGoals;
    private int differenceGoals;
    private int cleanSheetNumber;
    private Season season;
    private Instant syncDate;
}
