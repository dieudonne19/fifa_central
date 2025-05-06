package org.football.fifa_central.dao.operations;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.football.fifa_central.dao.DataSource;
import org.football.fifa_central.dao.mapper.ClubStatsDtoMapper;
import org.football.fifa_central.dao.mapper.ClubStatsMapper;
import org.football.fifa_central.dao.operations.DTO.ClubStatsDto;
import org.football.fifa_central.model.ClubStats;
import org.football.fifa_central.model.Season;
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

@Repository
@RequiredArgsConstructor
public class ClubStatsCrudOperations {

    private final DataSource dataSource;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ClubStatsDtoMapper clubStatsDtoMapper;
    private final ClubStatsMapper clubStatsMapper;

    public List<ClubStats> getClubStatsFromApi(String apiUrl, Season season) {
        ResponseEntity<List<ClubStatsDto>> response = restTemplate.exchange(apiUrl + "/clubs/statistics/" + season.getYear(), HttpMethod.GET, null, new ParameterizedTypeReference<List<ClubStatsDto>>() {
        });
        List<ClubStats> clubStats = response.getBody().stream().map(clubStatsDto -> clubStatsDtoMapper.toModel(clubStatsDto)).toList();
        return clubStats;
    }

    @SneakyThrows
    public List<ClubStats> saveAll(List<ClubStats> clubStatsToSave) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement statement = con.prepareStatement("INSERT INTO club_stats (club_id, season_id, points, scored_goals, conceded_goals, clean_sheets, difference_goals, sync_date) VALUES (?,?,?,?,?,?,?,?) " +
                     "ON CONFLICT (club_id,season_id) DO UPDATE SET points=excluded.points,scored_goals=excluded.scored_goals,conceded_goals=excluded.conceded_goals,clean_sheets=excluded.clean_sheets,difference_goals=excluded.difference_goals,sync_date=excluded.sync_date "
             )
        ) {
            for (ClubStats clubStats : clubStatsToSave) {
                statement.setString(1, clubStats.getClub().getId());
                statement.setString(2, clubStats.getSeason().getId());
                statement.setInt(3, clubStats.getRankingPoints());
                statement.setInt(4, clubStats.getScoredGoals());
                statement.setInt(5, clubStats.getConcededGoals());
                statement.setInt(6, clubStats.getCleanSheetNumber());
                statement.setInt(7, clubStats.getDifferenceGoals());
                statement.setTimestamp(8, Timestamp.from(clubStats.getSyncDate()));
                statement.addBatch();
            }
            int[] rs = statement.executeBatch();
            if (!Arrays.stream(rs).allMatch(value -> value == 1)) {
                System.out.println("One of entries failed in club stats");
                return null;
            }
            return clubStatsToSave;

        }
    }

    @SneakyThrows
    public List<ClubStats> getAll() {
        List<ClubStats> clubStats = new ArrayList<>();
        try(
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("select club_id, season_id, points, scored_goals, conceded_goals, clean_sheets, difference_goals, sync_date from club_stats")
                ){
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()){

                ClubStats clubStat = clubStatsMapper.apply(rs);
                clubStats.add(clubStat);
                }
            }
        }
        return clubStats;
    }
}
