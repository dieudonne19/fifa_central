package org.football.fifa_central.dao.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.football.fifa_central.model.DurationUnit;
import org.football.fifa_central.model.PlayingTime;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class PlayingTimeMapper implements Function<ResultSet, PlayingTime> {
    @Override
    @SneakyThrows
    public PlayingTime apply(ResultSet resultSet) {
        PlayingTime playingTime = new PlayingTime();

        playingTime.setId(resultSet.getString("id"));
        playingTime.setUnit(DurationUnit.valueOf(resultSet.getString("unit")));
        playingTime.setValue(resultSet.getInt("value"));

        return playingTime;
    }
}
