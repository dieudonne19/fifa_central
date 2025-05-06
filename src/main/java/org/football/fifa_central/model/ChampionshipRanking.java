package org.football.fifa_central.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Data
@EqualsAndHashCode
@ToString
public class ChampionshipRanking {
    @JsonIgnore
    private String id;
    private int rank;

    @JsonIgnore
    private Championship championship;
    private int differenceGoalMedian ;

    @JsonProperty("championship")
    private ChampionshipName getChampionshipName() {
        return this.championship.getName();
    }
}
