package org.football.fifa_central.endpoint;

import lombok.RequiredArgsConstructor;
import org.football.fifa_central.service.SynchronizationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class synchronizationController {
    private final SynchronizationService synchronizationService;


    @GetMapping("/synchronisation")
    public Object getAllPlayersFromChampionships() {
        return synchronizationService.synchronize();
    }

}
