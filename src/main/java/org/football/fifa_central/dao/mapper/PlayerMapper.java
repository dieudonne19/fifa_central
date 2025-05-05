package org.football.fifa_central.dao.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.football.fifa_central.dao.operations.ChampionshipCrudOperations;
import org.football.fifa_central.model.Championship;
import org.football.fifa_central.model.Club;
import org.football.fifa_central.model.Player;
import org.football.fifa_central.model.Positions;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class PlayerMapper implements Function<ResultSet, Player> {

    @SneakyThrows
    @Override
    public Player apply(ResultSet resultSet) {
        String playerId = resultSet.getString("id");

        Player player = new Player();

        player.setId(playerId);
        player.setName(resultSet.getString("name"));
        player.setAge(resultSet.getInt("age"));
        player.setCountry(resultSet.getString("country"));
        player.setPosition(Positions.valueOf(resultSet.getObject("position").toString()));
        player.setNumber(resultSet.getInt("number"));

        // player.setClub();
        // player.setPlayerStats(); dans service (refactored)
        // player.setChampionship(); dans service
        player.setSyncDate(resultSet.getTimestamp("sync_date").toInstant());

        return player;
    }
}
