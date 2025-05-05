package org.football.fifa_central.endpoint.rest;

import lombok.*;

@NoArgsConstructor
@Data
@Setter
@ToString
@EqualsAndHashCode
public class URL {

    private int port;
    private String host;

    public URL(int port) {
        this.port = port;
        this.host = "localhost";
    }

    public String getUrl() {
        return "http://" + this.host + ":" + this.port;
    }
}
