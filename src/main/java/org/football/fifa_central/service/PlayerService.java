package org.football.fifa_central.service;

import lombok.RequiredArgsConstructor;
import org.football.fifa_central.dao.operations.PlayerCrudOperations;
import org.football.fifa_central.endpoint.rest.URL;
import org.football.fifa_central.model.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerCrudOperations playerCrudOperations;

    public List<Player> getAll(URL url) {
        List<Player> players = playerCrudOperations.getAll(url);

        return players;
    }
}
