package org.football.fifa_central.dao.operations;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.football.fifa_central.dao.DataSource;
import org.football.fifa_central.dao.mapper.ChampionshipMapper;
import org.football.fifa_central.model.Championship;
import org.football.fifa_central.model.ChampionshipName;
import org.football.fifa_central.model.Player;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChampionshipCrudOperations {
    private final DataSource dataSource;
    private final ChampionshipMapper championshipMapper;

    @SneakyThrows
    public List<Championship> getAll() {
        List<Championship> championships = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select id, name, country, api_url,api_key from championship")) {
            /*
            statement.setInt(1, pageSize);
            statement.setInt(2, pageSize * (page - 1));
             */
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Championship championship = championshipMapper.apply(resultSet);
                    championships.add(championship);
                }
            }
            return championships;
        }
    }

    @SneakyThrows
    public Championship getByName(ChampionshipName championshipName) {
        Championship championship = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select id, name, country, api_url"
                     + " from championship where name = cast(? as championship_name);"
             )) {
            statement.setString(1, championshipName.name());
            /*
            statement.setInt(2, pageSize * (page - 1));
             */
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    championship = championshipMapper.apply(resultSet);
                }
            }
            return championship;
        }
    }

    @SneakyThrows
    public Championship getById(String id) {
        Championship championship = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select id, name, country, api_url"
                     + " from championship where id = ?;"
             )) {
            statement.setString(1, id);
            /*
            statement.setInt(2, pageSize * (page - 1));
             */
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    championship = championshipMapper.apply(resultSet);
                }
            }
            return championship;
        }
    }

    @SneakyThrows
    public Championship getByApiURL(String apiURL) {
        Championship championship = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select id, name, country, api_url"
                     + " from championship where api_url = ?;"
             )) {
            statement.setString(1, apiURL);
            /*
            statement.setInt(2, pageSize * (page - 1));
             */
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    championship = championshipMapper.apply(resultSet);
                }
            }
            return championship;
        }
    }

    @SneakyThrows
    public List<Championship> saveAll(List<Championship> entities) {
        List<Championship> savedChampionships = new ArrayList<>();

        String sql = "INSERT INTO championship(id, name, country, api_url)"
                + " VALUES (?, cast(? as championship_name), ?, ?)"
                + " ON CONFLICT (id) DO UPDATE SET name=excluded.name, country=excluded.country, api_url=excluded.api_url";
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)
        ) {
            for (Championship championship : entities) {
                statement.setString(1, championship.getId());
                statement.setString(2, championship.getName().toString());
                // statement.setString(3, championship.getCountry());
                statement.setString(4, championship.getApiUrl());

                statement.addBatch();
            }

            int[] rs = statement.executeBatch(); // Batch 💥
            if (!Arrays.stream(rs).allMatch(value -> value == 1)) {
                System.out.println("One of entries failed");
                return null;
            }
            // Pas besoin de re-fetch les championships si on fait juste du save
            return entities;
        }
    }
}
