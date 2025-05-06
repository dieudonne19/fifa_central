package org.football.fifa_central.service;

import lombok.RequiredArgsConstructor;
import org.football.fifa_central.dao.operations.PlayerStatisticsCrudOperations;
import org.football.fifa_central.dao.operations.SeasonCrudOperations;
import org.football.fifa_central.model.Championship;
import org.football.fifa_central.model.Player;
import org.football.fifa_central.model.PlayerStats;
import org.football.fifa_central.dao.operations.PlayingTimeCrudOperations;
import org.football.fifa_central.model.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlayerStatisticsService {
    private final PlayerStatisticsCrudOperations playerStatisticsCrudOperations;
    private final SeasonCrudOperations seasonCrudOperations;
    private final PlayingTimeCrudOperations playingTimeCrudOperations;

    public PlayerStats getFromExternalAPI(Championship championship, String playerId, Year seasonYear) {
        PlayerStats externalPlayerStats = playerStatisticsCrudOperations.getFromExternalAPI(championship, playerId, seasonYear);
        externalPlayerStats.setSyncDate(Instant.now());

        return externalPlayerStats;
    }

    public List<PlayerStats> getFromDb() {
        List<PlayerStats> playerStatsFromDb = playerStatisticsCrudOperations.getAllFromDB();

        return playerStatsFromDb;
    }

    public List<PlayerStats> saveAll(List<PlayerStats> entities) {
        List<PlayerStats> playerStats = playerStatisticsCrudOperations.saveAll(entities);
        return playerStats;
    }

    public List<PlayerStats> synchronize(Championship championship, List<Player> players, Season season) {
        List<PlayerStats> playerStats = new ArrayList<>();
        players.forEach(player -> {

            PlayerStats pls = this.getFromExternalAPI(championship, player.getId(), season.getYear());

            PlayingTime playingTime = pls.getPlayingTime();
            playingTime.setId("PT-" + player.getId());

            playingTimeCrudOperations.saveAll(List.of(playingTime));

            pls.setSeason(season);
            pls.setPlayingTime(playingTime);
            pls.setPlayer(player);
            playerStats.add(pls);
        });

        List<PlayerStats> savedPlayerStats = this.saveAll(playerStats);
        return savedPlayerStats;
    }

}
