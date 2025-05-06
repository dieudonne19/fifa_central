package org.football.fifa_central.dao.operations;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.football.fifa_central.dao.DataSource;
import org.football.fifa_central.dao.mapper.PlayerStatsMapper;
import org.football.fifa_central.model.Championship;
import org.football.fifa_central.model.Player;
import org.football.fifa_central.model.PlayerStats;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PlayerStatisticsCrudOperations {
    private final DataSource dataSource;
    private final RestTemplate restTemplate = new RestTemplate();
    private final PlayerStatsMapper playerStatsMapper;

    public PlayerStats getFromExternalAPI(Championship championship, String playerId, Year seasonYear) {
        ResponseEntity<PlayerStats> response = restTemplate.exchange(
                championship.getApiUrl() + "/players/" + playerId + "/statistics/" + seasonYear,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        return response.getBody();
    }

    @SneakyThrows
    public List<PlayerStats> getAllFromDB() {
        List<PlayerStats> stats = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select id, player_id, season_id, playing_time_id, scored_goals, sync_date"
                     + " from player_stats")) {
            /*
            statement.setInt(1, pageSize);
            statement.setInt(2, pageSize * (page - 1));
             */
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    PlayerStats stat = playerStatsMapper.apply(resultSet);
                    stats.add(stat);
                }
            }
            return stats;
        }
    }

    @SneakyThrows
    public PlayerStats getId(String id) {
        PlayerStats stat = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select id, player_id, season_id, playing_time_id, scored_goals, sync_date"
                     + " from player_stats where id = ?")) {
            statement.setString(1, id);
            /*
            statement.setInt(2, pageSize * (page - 1));
             */
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    stat = playerStatsMapper.apply(resultSet);
                }
            }
            return stat;
        }
    }

    @SneakyThrows
    public PlayerStats getBySeasonId(String seasonId) {
        PlayerStats stat = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select id, player_id, season_id, playing_time_id, scored_goals, sync_date"
                     + " from player_stats where season_id = ?")) {
            statement.setString(1, seasonId);
            /*
            statement.setInt(2, pageSize * (page - 1));
             */
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    stat = playerStatsMapper.apply(resultSet);
                }
            }
            return stat;
        }
    }

    @SneakyThrows
    public List<PlayerStats> getManyByPlayerId(String playerId) {
        List<PlayerStats> stats = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select id, player_id, season_id, playing_time_id, scored_goals, sync_date"
                     + " from player_stats where player_id = ?")) {
            statement.setString(1, playerId);
            /*
            statement.setInt(2, pageSize * (page - 1));
             */
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    PlayerStats stat = playerStatsMapper.apply(resultSet);
                    stats.add(stat);
                }
            }
            return stats;
        }
    }

    @SneakyThrows
    public List<PlayerStats> saveAll(List<PlayerStats> entities) {
        List<PlayerStats> savedPlayerStats = new ArrayList<>();

        String sql = "INSERT INTO player_stats(id, player_id, season_id, playing_time_id, scored_goals, sync_date)"
                + " VALUES (?, ?, ?, ?, ?, ?)"
                + " ON CONFLICT (id) DO UPDATE SET season_id=excluded.season_id, playing_time_id=excluded.playing_time_id,"
                + "  scored_goals= excluded.scored_goals, sync_date=excluded.sync_date";
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)
        ) {
            for (PlayerStats p : entities) {
                String id = p.getId() != null ? p.getId() : "STATS-" + p.getPlayer().getId() ;

                statement.setString(1, id);
                statement.setString(2, p.getPlayer().getId());
                statement.setString(3, p.getSeason().getId());
                //statement.setString(3, UUID.randomUUID().toString());
                statement.setString(4, p.getPlayingTime().getId());
                statement.setLong(5, p.getScoredGoals());
                statement.setTimestamp(6, Timestamp.from(p.getSyncDate()));


                statement.addBatch();
            }

            int[] rs = statement.executeBatch(); // Batch 💥
            if (!Arrays.stream(rs).allMatch(value -> value == 1)) {
                System.out.println("One of entries failed");
                return null;
            }
            // Pas besoin de re-fetch les playersStats si on fait juste du save
            return entities;
        }
    }

}
