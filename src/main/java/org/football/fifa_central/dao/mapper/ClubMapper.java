package org.football.fifa_central.dao.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.football.fifa_central.dao.operations.CoachCrudOperation;
import org.football.fifa_central.model.Club;
import org.football.fifa_central.model.Coach;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.time.Instant;
import java.time.Year;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ClubMapper implements Function<ResultSet, Club> {

    private final CoachCrudOperation coachCrudOperation;

    @SneakyThrows
    @Override
    public Club apply(ResultSet resultSet) {
        Club club = new Club();
        Coach coach = coachCrudOperation.getByName(resultSet.getString("coach_name"));
        club.setId(resultSet.getString("id"));
        club.setName(resultSet.getString("name"));
        club.setSync_date((resultSet.getTimestamp("sync_date")).toInstant());

        club.setCoach(coach);
        club.setAcronym(resultSet.getString("acronym"));
        club.setStadium(resultSet.getString("stadium"));
        club.setYearCreation(Year.parse(resultSet.getString("year_creation")));
        //club.setChampionship();
        return club;
    }
}
