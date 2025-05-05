package org.football.fifa_central.service;

import lombok.RequiredArgsConstructor;
import org.football.fifa_central.dao.operations.ChampionshipCrudOperations;
import org.football.fifa_central.endpoint.rest.URL;
import org.football.fifa_central.model.*;
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
    private final ClubStatsService clubStatsService;
    private final SeasonService seasonService;
    private final PlayerStatisticsService playerStatisticsService;
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
        List<ClubStats> synchronizedClubStats = new ArrayList<>();
        List<Season> synchronizedSeasons = new ArrayList<>();
        for (Championship championship : championships) {
             synchronizedClubs.addAll(clubService.sync(championship));
             synchronizedSeasons.addAll(seasonService.sync(championship));
             synchronizedSeasons.forEach(season -> {
            synchronizedClubStats.addAll(clubStatsService.sync(championship,season));

             });
        }
        if (synchronizedClubs != null && synchronizedSeasons != null && synchronizedClubStats !=null) {
            return ResponseEntity.ok(List.of(synchronizedClubs, synchronizedClubStats, synchronizedSeasons));
        }
        return ResponseEntity.internalServerError().body("internal server error");
    }

}
