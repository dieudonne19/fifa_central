package org.football.fifa_central.dao.operations.DTO;


import lombok.*;
import org.football.fifa_central.model.Coach;

import java.time.Year;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ClubStatsDto {
    private int rankingPoints;
    private int scoredGoals;
    private int concededGoals;
    private int differenceGoals;
    private int cleanSheetNumber;
    private Year yearCreation;
    private String stadium;
    private Coach coach;
    private String id;
    private String name;
    private String acronym;
}
