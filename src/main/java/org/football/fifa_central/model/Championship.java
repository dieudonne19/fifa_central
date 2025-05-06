package org.football.fifa_central.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Data
@EqualsAndHashCode
@ToString
public class Championship {
    private String id;
    private ChampionshipName name;
    private String country;
    private String apiUrl;
    private String apiKey;
    private List<Club> clubs;
    private List<Player> players;
    private List<Season> seasons;
}
