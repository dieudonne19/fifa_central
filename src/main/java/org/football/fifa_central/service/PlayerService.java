package org.football.fifa_central.service;

import lombok.RequiredArgsConstructor;
import org.football.fifa_central.dao.operations.ChampionshipCrudOperations;
import org.football.fifa_central.dao.operations.PlayerCrudOperations;
import org.football.fifa_central.dao.operations.PlayerStatisticsCrudOperations;
import org.football.fifa_central.model.Championship;
import org.football.fifa_central.model.Player;
import org.football.fifa_central.model.PlayerStats;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerCrudOperations playerCrudOperations;
    private final ChampionshipCrudOperations championshipCrudOperations;
    private final PlayerStatisticsCrudOperations playerStatisticsCrudOperations;

    public List<Player> getAllFromExternalAPI(Championship championship) {
        List<Player> externalPlayers = playerCrudOperations.getAllFromExternalAPI(championship);
        externalPlayers.forEach(player -> {
            //Championship championship = championshipCrudOperations.getByApiURL(url.getUrl());

            List<PlayerStats> playerStats = playerStatisticsCrudOperations.getManyByPlayerId(player.getId());

            player.setSyncDate(Instant.now());
            player.setPlayerStats(playerStats);
            player.setChampionship(championship);
            // club a setter
        });
        return externalPlayers;
    }

    public List<Player> getAllFromDb() {
        List<Player> internalPlayers = playerCrudOperations.getAllFromDB();

        this.setChampionshipAndPlayerStats(internalPlayers);

        return internalPlayers;
    }

    public List<Player> saveAll(List<Player> entities) {
        List<Player> players = playerCrudOperations.saveAll(entities);
        return players;
    }

    public List<Player> synchronize(Championship championship) {
        List<Player> externalPlayers = this.getAllFromExternalAPI(championship);
        List<Player> playersSavedToCentral = playerCrudOperations.saveAll(externalPlayers);

        return playersSavedToCentral;
    }


    public Player getById(String playerId) {
        Player player = playerCrudOperations.getById(playerId);

        this.setChampionshipAndPlayerStats(List.of(player));

        return player;
    }

    public List<Player> getManyByClubId(String clubId) {
        List<Player> internalPlayers = playerCrudOperations.getManyByClubId(clubId);

        this.setChampionshipAndPlayerStats(internalPlayers);

        return internalPlayers;
    }

    public List<Player> getManyByChampionshipId(String championshipId) {
        List<Player> internalPlayers = playerCrudOperations.getManyByChampionshipId(championshipId);

        this.setChampionshipAndPlayerStats(internalPlayers);

        return internalPlayers;
    }

    private void setChampionshipAndPlayerStats(List<Player> internalPlayers) {
        internalPlayers.forEach(player -> {
            Championship championship = championshipCrudOperations.getByApiURL(player.getChampionship().getApiUrl());
            List<PlayerStats> playerStats = playerStatisticsCrudOperations.getManyByPlayerId(player.getId());

            player.setChampionship(championship);
            player.setPlayerStats(playerStats);
            // club a setter
        });
    }
}
