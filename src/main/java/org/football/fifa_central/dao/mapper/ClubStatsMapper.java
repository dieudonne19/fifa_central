package org.football.fifa_central.dao.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.football.fifa_central.dao.operations.ClubCrudOperations;
import org.football.fifa_central.dao.operations.SeasonCrudOperations;
import org.football.fifa_central.model.Club;
import org.football.fifa_central.model.ClubStats;
import org.football.fifa_central.model.Season;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component@RequiredArgsConstructor
public class ClubStatsMapper implements Function<ResultSet, ClubStats> {


    private final SeasonCrudOperations seasonCrudOperations;
    private final ClubCrudOperations clubCrudOperations;

    @Override@SneakyThrows
    public ClubStats apply(ResultSet resultSet) {
        ClubStats clubStats = new ClubStats();
        Season season = seasonCrudOperations.getById(resultSet.getString("season_id"));
        Club club = clubCrudOperations.getById(resultSet.getString("club_id"));
        clubStats.setSeason(season);
        clubStats.setClub(club);
        clubStats.setSyncDate(resultSet.getTimestamp("sync_date").toInstant());
        clubStats.setScoredGoals(resultSet.getInt("scored_goals"));
        clubStats.setDifferenceGoals(resultSet.getInt("difference_goals"));
        clubStats.setCleanSheetNumber(resultSet.getInt("clean_sheets"));
        clubStats.setRankingPoints(resultSet.getInt("points"));
        clubStats.setConcededGoals(resultSet.getInt("conceded_goals"));
        return  clubStats;
    }
}
