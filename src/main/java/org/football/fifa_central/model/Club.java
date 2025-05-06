package org.football.fifa_central.model;

import lombok.*;

import java.time.Instant;
import java.time.Year;
@AllArgsConstructor@NoArgsConstructor@Setter@Getter@EqualsAndHashCode@ToString
public class Club {
    private String id;
    private String name;
    private Year yearCreation;
    private String acronym;
    private String stadium;
    private Championship championship;
    private Coach coach;
    private Instant sync_date;
}