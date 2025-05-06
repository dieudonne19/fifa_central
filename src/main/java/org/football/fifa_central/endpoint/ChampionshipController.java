package org.football.fifa_central.endpoint;

import lombok.RequiredArgsConstructor;
import org.football.fifa_central.service.ChampionshipService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChampionshipController {

    private final ChampionshipService championshipService;

    @GetMapping("/championshipRankings")
    public Object rankChampionships() {
        return championshipService.rankChampionship();
    }
}
