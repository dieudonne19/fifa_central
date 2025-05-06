package org.football.fifa_central.endpoint;


import lombok.RequiredArgsConstructor;
import org.football.fifa_central.model.DurationUnit;
import org.football.fifa_central.service.PlayerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BestPlayersController {

    private final PlayerService playerService;

    @GetMapping("/bestPlayers")
    public Object getBestPlayers(@RequestParam(name = "top", required = false) Integer top, @RequestParam(name = "playingTimeUnit", defaultValue = "MINUTE") DurationUnit playingTimeUnit) {
        return playerService.getBestPlayerFromAllChampionship(top, playingTimeUnit);
    }
}
