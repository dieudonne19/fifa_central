package org.football.fifa_central.dao.operations;

import lombok.RequiredArgsConstructor;
import org.football.fifa_central.dao.DataSource;
import org.football.fifa_central.endpoint.rest.URL;
import org.football.fifa_central.model.Player;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PlayerCrudOperations {
    private final DataSource dataSource;
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Player> getAll(URL url) {
        ResponseEntity<List<Player>> response = restTemplate.exchange(
                url.getUrl() + "/players",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        return response.getBody();
    }


}
