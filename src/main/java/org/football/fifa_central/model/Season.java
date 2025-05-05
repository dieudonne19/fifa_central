package org.football.fifa_central.model;

import lombok.*;

import java.time.Year;

@AllArgsConstructor@NoArgsConstructor@Getter@Setter@EqualsAndHashCode@ToString
public class Season {
    private String id;
    private Year year;
    private String alias;
    private Championship championship;
}
