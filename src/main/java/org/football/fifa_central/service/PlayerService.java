package org.football.fifa_central.service;

import lombok.RequiredArgsConstructor;
import org.football.fifa_central.dao.mapper.BestPlayerMapper;
import org.football.fifa_central.dao.operations.ChampionshipCrudOperations;
import org.football.fifa_central.dao.operations.PlayerCrudOperations;
import org.football.fifa_central.dao.operations.PlayerStatisticsCrudOperations;
import org.football.fifa_central.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerCrudOperations playerCrudOperations;
    private final ChampionshipCrudOperations championshipCrudOperations;
    private final PlayerStatisticsCrudOperations playerStatisticsCrudOperations;
    private final BestPlayerMapper bestPlayerMapper;

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

    public ResponseEntity<Object> getBestPlayerFromAllChampionship(Integer top, DurationUnit playingTimeUnit) {
        if (!playingTimeUnit.equals(DurationUnit.MINUTE)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sorry, all playing time unit are mapped to MINUTE so try MINUTE INSTEAD OF " + playingTimeUnit);
        }

        List<PlayerStats> playerStats = playerStatisticsCrudOperations.getAllFromDB().stream()
                .sorted(Comparator.comparing(PlayerStats::getScoredGoals))
                .toList().reversed();
        List<PlayerStats> playerStatsHavingSameGoals = new ArrayList<>();
        List<PlayerStats> playerStatsScoredGoals = new ArrayList<>();
        List<BestPlayer> bestPlayers = new ArrayList<>();

        playerStats.forEach(pls -> {
            int baseGoals = 0;

            if (pls.getScoredGoals() == baseGoals) {
                playerStatsHavingSameGoals.add(pls);
            } else {
                baseGoals = pls.getScoredGoals();
                playerStatsScoredGoals.add(pls);
            }
        });
        playerStatsHavingSameGoals.sort(Comparator.comparing(pls -> pls.getPlayingTime().getValue()));

        List<PlayerStats> combinedPlayerStats = new ArrayList<>(playerStatsScoredGoals);
        combinedPlayerStats.addAll(playerStatsHavingSameGoals.reversed());

        combinedPlayerStats.forEach(pls -> {
            String playerId = Arrays.stream(pls.getId().split("-")).toList().getLast();
            Player player = playerCrudOperations.getById(playerId);

            pls.setPlayer(player);

            BestPlayer bestPlayer = bestPlayerMapper.apply(pls);

            bestPlayer.setChampionship(player.getChampionship());

            bestPlayers.add(bestPlayer);
        });

        List<BestPlayer> confirmedBestPlayers = bestPlayers.stream()
                .filter(bestPlayer -> bestPlayer.getPlayingTime().getUnit().equals(playingTimeUnit))
                .toList();

        if (confirmedBestPlayers.isEmpty()) {
            return ResponseEntity.ok(bestPlayers);
        }

        if (top == null) {
            return ResponseEntity.ok(confirmedBestPlayers.subList(0, 5));
        }
        if (top > bestPlayers.size()) {
            return ResponseEntity.ok(confirmedBestPlayers);
        }

        return ResponseEntity.ok(confirmedBestPlayers.subList(0, top));
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
