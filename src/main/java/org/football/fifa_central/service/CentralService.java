package org.football.fifa_central.service;

import lombok.RequiredArgsConstructor;
import org.football.fifa_central.endpoint.rest.URL;
import org.football.fifa_central.model.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CentralService {
    private final PlayerService playerService;
    private List<URL> urls = List.of(
            new URL(8081)
    );

    public ResponseEntity<Object> getAllPlayersFromChampionships() {
        List<Player> players = new ArrayList<>();

        this.urls.forEach(url -> {
            List<Player> playersFromChampionship = playerService.getAllFromExternalAPI(url);
            players.addAll(playersFromChampionship);
        });

        return ResponseEntity.ok(players);
    }

}
