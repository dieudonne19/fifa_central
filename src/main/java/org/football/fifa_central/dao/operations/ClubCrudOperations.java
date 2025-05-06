package org.football.fifa_central.dao.operations;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.football.fifa_central.dao.DataSource;
import org.football.fifa_central.dao.mapper.ClubMapper;
import org.football.fifa_central.model.Championship;
import org.football.fifa_central.model.Club;
import org.football.fifa_central.model.Player;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ClubCrudOperations {
    private final DataSource dataSource;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ClubMapper clubMapper;
    private final CoachCrudOperation coachCrudOperation;

    public List<Club> getAllFromApi(Championship championship) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY",championship.getApiKey());
        headers.setAccept(List.of(new MediaType("application", "json")));

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<List<Club>> response = restTemplate.exchange(championship.getApiUrl() + "/clubs", HttpMethod.GET, httpEntity, new ParameterizedTypeReference<List<Club>>() {
        });

        return response.getBody();
    }

    @SneakyThrows
    public Club getById(String clubId) {
        Club club = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select id, name, year_creation, acronym, stadium, championship_id, coach_name, sync_date"
                     + " from club where id = ?;"
             )) {
            statement.setString(1, clubId);
            /*
            statement.setInt(2, pageSize * (page - 1));
             */
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    club = clubMapper.apply(resultSet);
                }
            }
            return club;
        }
    }

    @SneakyThrows
    public List<Club> saveAll(List<Club> clubs) {
        //  System.out.println("clubs "+clubs);
        //List<Club> savedClubs = new ArrayList<>();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO club (id, name, year_creation, acronym, stadium, championship_id, coach_name, sync_date) values (?,?,?,?,?,?,?,?) " +
                        "ON CONFLICT (id) DO UPDATE set name=excluded.name , year_creation=excluded.year_creation,acronym=excluded.acronym,stadium=excluded.stadium,championship_id=excluded.championship_id,coach_name=excluded.coach_name ,sync_date=excluded.sync_date ")
        ) {
            for (Club club : clubs) {
                coachCrudOperation.save(club.getCoach());
                preparedStatement.setString(1, club.getId());
                preparedStatement.setString(2, club.getName());
                preparedStatement.setString(3, club.getYearCreation().toString());
                preparedStatement.setString(4, club.getAcronym());
                preparedStatement.setString(5, club.getStadium());
                preparedStatement.setString(6, club.getChampionship().getId());
                preparedStatement.setString(7, club.getCoach().getName());
                preparedStatement.setTimestamp(8, Timestamp.from(club.getSync_date()));
                preparedStatement.addBatch();
            }
            int[] rs = preparedStatement.executeBatch(); // Batch 💥
            if (!Arrays.stream(rs).allMatch(value -> value == 1)) {
                System.out.println("One of entries failed");
                return null;
            }
            return clubs;
        }
    }

    @SneakyThrows
    public List<Club> getAll() {
        List<Club> clubs = new ArrayList<>();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT id, name, year_creation, acronym, stadium, championship_id, coach_name, sync_date FROM club");
        ) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Club club = clubMapper.apply(resultSet);
                    clubs.add(club);
                }
            }
        }
        return clubs;
    }

    @SneakyThrows
    public List<Club> getAllByChampionshipId(String championshipId) {
        List<Club> clubs = new ArrayList<>();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT id, name, year_creation, acronym, stadium, championship_id, coach_name, sync_date"
                        + " FROM club where championship_id = ?;");
        ) {
            statement.setString(1, championshipId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Club club = clubMapper.apply(resultSet);
                    clubs.add(club);
                }
            }
        }
        return clubs;
    }

}
