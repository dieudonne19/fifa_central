package org.football.fifa_central.dao.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.football.fifa_central.dao.operations.ChampionshipCrudOperations;
import org.football.fifa_central.model.Championship;
import org.football.fifa_central.model.Season;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.time.Year;
import java.util.function.Function;
@Component@RequiredArgsConstructor
public class SeasonMapper implements Function<ResultSet, Season> {

    private final ChampionshipCrudOperations championshipCrudOperations;


    @SneakyThrows
    @Override
    public Season apply(ResultSet resultSet) {
        Season season = new Season();
        Championship championship = championshipCrudOperations.getById(resultSet.getString("championship_id"));
        season.setId(resultSet.getString("id"));
        season.setYear(Year.parse(resultSet.getString("year")));
        season.setAlias(resultSet.getString("alias"));
        season.setChampionship(championship);
        return season;
    }
}
