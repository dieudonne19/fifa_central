package org.football.fifa_central.endpoint;

import lombok.RequiredArgsConstructor;
import org.football.fifa_central.model.DurationUnit;
import org.football.fifa_central.service.PlayerService;
import org.football.fifa_central.service.SynchronizationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SynchronizationController {
    private final SynchronizationService synchronizationService;


    @PostMapping("/synchronisation")
    public Object synchronize() {
        return synchronizationService.synchronize();
    }



}
