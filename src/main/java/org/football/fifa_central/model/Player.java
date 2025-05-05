package org.football.fifa_central.model;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
@EqualsAndHashCode
public class Player {
    private String name;
    private String id;
    private Positions position;
    private String country;
    private int age;
    private int preferredNumber;
    private Coach coach;
}
