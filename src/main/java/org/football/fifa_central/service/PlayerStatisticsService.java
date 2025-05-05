package org.football.fifa_central.service;

import lombok.RequiredArgsConstructor;
import org.football.fifa_central.dao.operations.PlayerStatisticsCrudOperations;
import org.football.fifa_central.model.Championship;
import org.football.fifa_central.model.Player;
import org.football.fifa_central.model.PlayerStats;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerStatisticsService {
    private final PlayerStatisticsCrudOperations playerStatisticsCrudOperations;

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

    public List<PlayerStats> synchronize(Championship championship, List<Player> players, Year seasonYear) {
        List<PlayerStats> playerStats = new ArrayList<>();
        players.forEach(player -> {
            PlayerStats pls = this.getFromExternalAPI(championship, player.getId(), seasonYear);
            // season mila settena
            playerStats.add(pls);
        });

        List<PlayerStats> savedPlayerStats = this.saveAll(playerStats);
        return savedPlayerStats;
    }

}
