package org.football.fifa_central.dao.operations;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.football.fifa_central.dao.DataSource;
import org.football.fifa_central.dao.mapper.SeasonMapper;
import org.football.fifa_central.model.Season;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SeasonCrudOperations {

    private final DataSource dataSource;
    private final RestTemplate restTemplate = new RestTemplate();
    private final SeasonMapper seasonMapper;

    public List<Season> getSeasonsFromApi(String apiUrl) {
        ResponseEntity<List<Season>> response = restTemplate.exchange(apiUrl + "seasons", HttpMethod.GET, null, new ParameterizedTypeReference<List<Season>>() {
        });
        // System.out.println("seasons "+response.getBody());

        return response.getBody();
    }

    @SneakyThrows
    public List<Season> saveAll(List<Season> seasons) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO season (id, year, championship_id, alias) VALUES (?,?,?,?) on conflict (id) do nothing ")
        ) {
            for (Season season : seasons) {
                statement.setString(1, season.getId());
                statement.setInt(2, season.getYear().getValue());
                statement.setString(3, season.getChampionship().getId());
                statement.setString(4, season.getAlias());
                statement.addBatch();
            }
            int[] rs = statement.executeBatch();
            if (Arrays.stream(rs).filter(value -> value != 0).toArray().length > 0) {
                System.out.println("One of entries failed " + rs.length);

                return null;
            }
            return seasons;
        }
    }

    @SneakyThrows
    public List<Season> getAll() {
        List<Season> seasons = new ArrayList<>();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT id, year, championship_id, alias FROM season")
        ) {

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Season season = seasonMapper.apply(rs);
                    seasons.add(season);
                }

            }

        }
        return seasons;
    }

    @SneakyThrows
    public Season getByYear(Year seasonYear) {

        Season season = null;
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT id, year, championship_id, alias FROM season where year=?")
        ) {
            statement.setInt(1, seasonYear.getValue());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    season = seasonMapper.apply(rs);
                }
            }
        }
        return season;
    }
}
