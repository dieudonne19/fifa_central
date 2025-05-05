package org.football.fifa_central.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Data
@EqualsAndHashCode
@ToString
public class Coach {
    @JsonIgnore
    private String id;
    private String name;
    private String country;
}
