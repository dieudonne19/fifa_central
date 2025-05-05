package org.football.fifa_central.service;

import lombok.RequiredArgsConstructor;
import org.football.fifa_central.dao.operations.ChampionshipCrudOperations;
import org.football.fifa_central.endpoint.rest.URL;
import org.football.fifa_central.model.Championship;
import org.football.fifa_central.model.Club;
import org.football.fifa_central.model.Player;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SynchronizationService {
    private final PlayerService playerService;
    private final ChampionshipCrudOperations championshipCrudOperations;
    private final ClubService clubService;
    private List<URL> urls = List.of(
            new URL(8081)
    );

   /* public ResponseEntity<Object> getAllPlayersFromChampionships() {
        List<Player> players = new ArrayList<>();

        this.urls.forEach(url -> {
            List<Player> playersFromChampionship = playerService.getAllFromExternalAPI(url);
            players.addAll(playersFromChampionship);
        });

        return ResponseEntity.ok(players);
    }*/

    public ResponseEntity<Object> synchronize() {
        List<Championship> championships = championshipCrudOperations.getAll();
        List<Club> synchronizedClubs = new ArrayList<>();
        for (Championship championship : championships) {
           synchronizedClubs.addAll(clubService.sync(championship.getApiUrl()));
        }
        if (synchronizedClubs.size() > 0) {
            return ResponseEntity.ok(synchronizedClubs);
        }
        return ResponseEntity.internalServerError().body("internal server error");
    }

}
