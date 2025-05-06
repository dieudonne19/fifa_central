package org.football.fifa_central.dao.mapper;

import lombok.RequiredArgsConstructor;
import org.football.fifa_central.model.BestPlayer;
import org.football.fifa_central.model.PlayerStats;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class BestPlayerMapper implements Function<PlayerStats, BestPlayer> {
    @Override
    public BestPlayer apply(PlayerStats playerStats) {
        BestPlayer bestPlayer = new BestPlayer();

        bestPlayer.setId(playerStats.getPlayer().getId());
        bestPlayer.setName(playerStats.getPlayer().getName());
        bestPlayer.setAge(playerStats.getPlayer().getAge());
        bestPlayer.setCountry(playerStats.getPlayer().getCountry());
        bestPlayer.setNumber(playerStats.getPlayer().getNumber());
        bestPlayer.setPosition(playerStats.getPlayer().getPosition());
        bestPlayer.setScoredGoals(playerStats.getScoredGoals());
        bestPlayer.setPlayingTime(playerStats.getPlayingTime());

        return bestPlayer;
    }
}
