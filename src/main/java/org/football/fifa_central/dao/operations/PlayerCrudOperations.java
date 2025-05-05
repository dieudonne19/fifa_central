package org.football.fifa_central.dao.operations;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.football.fifa_central.dao.DataSource;
import org.football.fifa_central.dao.mapper.ChampionshipMapper;
import org.football.fifa_central.dao.mapper.PlayerMapper;
import org.football.fifa_central.endpoint.rest.URL;
import org.football.fifa_central.model.Championship;
import org.football.fifa_central.model.Player;
import org.football.fifa_central.model.PlayerStats;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PlayerCrudOperations {
    private final DataSource dataSource;
    private final PlayerMapper playerMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Player> getAllFromExternalAPI(URL url) {
        ResponseEntity<List<Player>> response = restTemplate.exchange(
                url.getUrl() + "/players",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        return response.getBody();
    }

    @SneakyThrows
    public List<Player> getAllFromDB() {
        List<Player> players = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select id, name, number, position, country, age, championship_id, club_id, sync_date from player")) {
            /*
            statement.setInt(1, pageSize);
            statement.setInt(2, pageSize * (page - 1));
             */
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Player player = playerMapper.apply(resultSet);

                    players.add(player);
                }
            }
            return players;
        }
    }

    @SneakyThrows
    public Player getById(String playerId) {
        Player player = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select id, name, number, position, country, age, championship_id, club_id, sync_date"
                     + " from player where id = ?;"
             )) {
            statement.setString(1, playerId);
            /*
            statement.setInt(2, pageSize * (page - 1));
             */
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    player = playerMapper.apply(resultSet);
                }
            }
            return player;
        }
    }

    @SneakyThrows
    public List<Player> getManyByClubId(String clubId) {
        List<Player> players = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select id, name, number, position, country, age, championship_id, club_id, sync_date"
                     + " from player where club_id = ?;"
             )) {
            statement.setString(1, clubId);
            /*
            statement.setInt(2, pageSize * (page - 1));
             */
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Player player = playerMapper.apply(resultSet);

                    players.add(player);
                }
            }
            return players;
        }
    }

    @SneakyThrows
    public List<Player> getManyByChampionshipId(String championshipId) {
        List<Player> players = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select id, name, number, position, country, age, championship_id, club_id, sync_date"
                     + " from player where championship_id = ?;"
             )) {
            statement.setString(1, championshipId);
            /*
            statement.setInt(2, pageSize * (page - 1));
             */
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Player player = playerMapper.apply(resultSet);

                    players.add(player);
                }
            }
            return players;
        }
    }

    @SneakyThrows
    public List<Player> saveAll(List<Player> entities) {
        List<Player> savedPlayers = new ArrayList<>();

        String sql = "INSERT INTO player(id, name, number, position, country, age, championship_id, club_id, sync_date)"
                + " VALUES (?, ?, ?, cast(? as positions), ?, ?, ?, ?, ?)"
                + " ON CONFLICT (id) DO UPDATE SET name = excluded.name, position = excluded.position, "
                + " country = excluded.country, age = excluded.age, number = excluded.number, championship_id=excluded.championship_id,"
                + " club_id=excluded.club_id, sync_date=excluded.sync_date";
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)
        ) {
            for (Player p : entities) {
                statement.setString(1, p.getId());
                statement.setString(2, p.getName());
                statement.setInt(3, p.getNumber());
                statement.setString(4, p.getPosition().toString());
                statement.setString(5, p.getCountry());
                statement.setInt(6, p.getAge());
                statement.setString(7, p.getChampionship().getId());
                statement.setString(8, p.getClub().getId());
                statement.setTimestamp(9, Timestamp.from(p.getSyncDate()));

                statement.addBatch();
            }

            int[] rs = statement.executeBatch(); // Batch 💥
            if (!Arrays.stream(rs).allMatch(value -> value == 1)) {
                System.out.println("One of entries failed");
                return null;
            }
            // Pas besoin de re-fetch les players si on fait juste du save
            return entities;
        }
    }
}
