package org.football.fifa_central.endpoint;

import lombok.RequiredArgsConstructor;
import org.football.fifa_central.service.PlayerService;
import org.football.fifa_central.service.SynchronizationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SynchronizationController {
    private final SynchronizationService synchronizationService;
    private final PlayerService playerService;


    @GetMapping("/synchronisation")
    public Object synchronize() {
        return synchronizationService.synchronize();
    }

    @GetMapping("/bestPlayers")
    public Object getBestPlayers() {
        return playerService.getBestPlayerFromAllChampionship();
    }

}
