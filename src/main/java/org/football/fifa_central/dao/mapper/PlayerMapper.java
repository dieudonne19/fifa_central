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

    private final ChampionshipCrudOperations championshipCrudOperations;

    @SneakyThrows
    @Override
    public Player apply(ResultSet resultSet) {
        String championshipId = resultSet.getString("championship_id");
        String playerId = resultSet.getString("id");

        Player player = new Player();
        Championship championship = championshipCrudOperations.getById(championshipId);

        player.setId(playerId);
        player.setName(resultSet.getString("name"));
        player.setAge(resultSet.getInt("age"));
        player.setCountry(resultSet.getString("country"));
        player.setPosition(Positions.valueOf(resultSet.getObject("position").toString()));
        player.setNumber(resultSet.getInt("number"));

        // player.setClub();
        // player.setPlayerStats(); efa any am DAO evitena boucle
        player.setSyncDate(resultSet.getTimestamp("sync_date").toInstant());
        player.setChampionship(championship);

        return player;
    }
}
