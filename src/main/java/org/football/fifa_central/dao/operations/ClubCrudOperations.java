package org.football.fifa_central.dao.operations;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.football.fifa_central.dao.DataSource;
import org.football.fifa_central.dao.mapper.ClubMapper;
import org.football.fifa_central.endpoint.rest.URL;
import org.football.fifa_central.model.Club;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository@RequiredArgsConstructor
public class ClubCrudOperations {
    private final DataSource dataSource;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ClubMapper clubMapper;
    private final CoachCrudOperation coachCrudOperation;

    public List<Club> getAllFromApi(String url) {
        ResponseEntity<List<Club>> response = restTemplate.exchange(url+"clubs", HttpMethod.GET,null,new ParameterizedTypeReference<List<Club>>() {});
        return response.getBody();
    }
    @SneakyThrows
    public List<Club> saveAll(List<Club> clubs) {
      //  System.out.println("clubs "+clubs);
        //List<Club> savedClubs = new ArrayList<>();
        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO club (id, name, year_creation, acronym, stadium, championship_id, coach_name, sync_date) values (?,?,?,?,?,?,?,?) " +
                        "ON CONFLICT (id) DO UPDATE set name=excluded.name , year_creation=excluded.year_creation,acronym=excluded.acronym,stadium=excluded.stadium,championship_id=excluded.championship_id,coach_name=excluded.coach_name ,sync_date=excluded.sync_date ")
                ){
            for(Club club : clubs){
                coachCrudOperation.save(club.getCoach());
                preparedStatement.setString(1, club.getId());
                preparedStatement.setString(2, club.getName());
                preparedStatement.setString(3,club.getYearCreation().toString());
                preparedStatement.setString(4, club.getAcronym());
                preparedStatement.setString(5, club.getStadium());
                preparedStatement.setString(6,club.getChampionship().getId());
                preparedStatement.setString(7,club.getCoach().getName());
                preparedStatement.setTimestamp(8, Timestamp.from(club.getSync_date()));
                preparedStatement.addBatch();
            }
            int[] rs = preparedStatement.executeBatch(); // Batch 💥
            if (Arrays.stream(rs).filter(value -> value != 1).toArray().length > 0) {
                System.out.println("One of entries failed");
                for (int r : rs) {
                    System.out.println(r);
                }
                return null;
            }
            return clubs;
        }
    }

    @SneakyThrows
    public List<Club> getAll(){
        List<Club> clubs = new ArrayList<>();
        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT id, name, year_creation, acronym, stadium, championship_id, coach_name, sync_date FROM club");
                ){
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()) {
                    Club club = clubMapper.apply(resultSet);
                    clubs.add(club);
                }
            }
        }
        return clubs;
    }

}
