package org.football.fifa_central.dao.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.football.fifa_central.dao.operations.PlayerCrudOperations;
import org.football.fifa_central.model.Championship;
import org.football.fifa_central.model.ChampionshipName;
import org.football.fifa_central.model.Player;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ChampionshipMapper implements Function<ResultSet, Championship> {
    private final PlayerCrudOperations playerCrudOperations;

    @Override
    @SneakyThrows
    public Championship apply(ResultSet resultSet) {
        String championshipId = resultSet.getString("id");
        List<Player> players = playerCrudOperations.getManyByChampionshipId(championshipId);

        Championship championship = new Championship();

        championship.setId(championshipId);
        championship.setName(ChampionshipName.valueOf(resultSet.getString("name")));
        championship.setCountry(resultSet.getString("country"));
        championship.setApiUrl(resultSet.getString("api_url"));

        // championship.setClubs();
        championship.setPlayers(players);
        // championship.setSeasons();

        return championship;
    }
}
