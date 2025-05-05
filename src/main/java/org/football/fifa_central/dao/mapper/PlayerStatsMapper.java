package org.football.fifa_central.dao.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.football.fifa_central.dao.operations.PlayerCrudOperations;
import org.football.fifa_central.dao.operations.PlayingTimeCrudOperations;
import org.football.fifa_central.model.Player;
import org.football.fifa_central.model.PlayerStats;
import org.football.fifa_central.model.PlayingTime;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class PlayerStatsMapper implements Function<ResultSet, PlayerStats> {
    //private final PlayerCrudOperations playerCrudOperations;
    private final PlayingTimeCrudOperations playingTimeCrudOperations;

    @Override
    @SneakyThrows
    public PlayerStats apply(ResultSet resultSet) {
       // Player player = playerCrudOperations.getById(resultSet.getString("player_id"));
        PlayingTime playingTime = playingTimeCrudOperations.getById(resultSet.getString("playing_time_id"));

        PlayerStats playerStats = new PlayerStats();

        playerStats.setId(resultSet.getString("id"));
      //  playerStats.setPlayer(player);
        playerStats.setPlayingTime(playingTime);
        // playerStats.setSeason();
        playerStats.setScoredGoals(resultSet.getInt("scored_goals"));
        playerStats.setSyncDate(resultSet.getTimestamp("sync_date").toInstant());

        return playerStats;
    }
}
