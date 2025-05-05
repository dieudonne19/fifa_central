package org.football.fifa_central.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Data
@EqualsAndHashCode
@ToString
public class PlayingTime {
    private String id;
    private int value;
    private DurationUnit unit;
}
