package org.football.fifa_central.dao.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.football.fifa_central.dao.operations.ChampionshipCrudOperations;
import org.football.fifa_central.dao.operations.ClubCrudOperations;
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
    private final ClubCrudOperations clubCrudOperations;

    @SneakyThrows
    @Override
    public Player apply(ResultSet resultSet) {
        String clubId = resultSet.getString("club_id");
        Club club = clubCrudOperations.getById(clubId);
        Championship championship = championshipCrudOperations.getById(resultSet.getString("championship_id"));

        String playerId = resultSet.getString("id");

        Player player = new Player();

        player.setId(playerId);
        player.setName(resultSet.getString("name"));
        player.setAge(resultSet.getInt("age"));
        player.setCountry(resultSet.getString("country"));
        player.setPosition(Positions.valueOf(resultSet.getObject("position").toString()));
        player.setNumber(resultSet.getInt("number"));

        player.setClub(club);
        // player.setPlayerStats(); dans service (refactored)
        player.setChampionship(championship);
        player.setSyncDate(resultSet.getTimestamp("sync_date").toInstant());

        return player;
    }
}
