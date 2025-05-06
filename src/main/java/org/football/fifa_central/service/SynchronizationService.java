package org.football.fifa_central.service;

import lombok.RequiredArgsConstructor;
import org.football.fifa_central.dao.operations.ChampionshipCrudOperations;
import org.football.fifa_central.model.*;
import org.football.fifa_central.model.Championship;
import org.football.fifa_central.model.Club;
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


    public ResponseEntity<Object> synchronize() {
        List<Championship> championships = championshipCrudOperations.getAll();

        List<Club> synchronizedClubs = new ArrayList<>();
        List<ClubStats> synchronizedClubStats = new ArrayList<>();
        List<Season> synchronizedSeasons = new ArrayList<>();
        List<Player> synchronizedPlayers = new ArrayList<>();
        List<PlayerStats> synchronizedPlayersStats = new ArrayList<>();


        for (Championship championship : championships) {
            synchronizedClubs.addAll(clubService.sync(championship));
            synchronizedSeasons.addAll(seasonService.sync(championship));
            synchronizedPlayers.addAll(playerService.synchronize(championship));


            synchronizedSeasons.forEach(season -> {
                synchronizedClubStats.addAll(clubStatsService.sync(championship, season));
                synchronizedPlayersStats.addAll(playerStatisticsService.synchronize(championship, synchronizedPlayers, season));
            });
        }
        if (synchronizedClubs != null && synchronizedSeasons != null && synchronizedClubStats != null) {
            return ResponseEntity.ok(List.of(
                    synchronizedClubs,
                    synchronizedClubStats,
                    synchronizedSeasons,
                    synchronizedPlayers,
                    synchronizedPlayersStats));
        }
        return ResponseEntity.internalServerError().body("internal server error");
    }

}
