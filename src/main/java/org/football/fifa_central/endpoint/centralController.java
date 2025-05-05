package org.football.fifa_central.endpoint;

import lombok.RequiredArgsConstructor;
import org.football.fifa_central.service.CentralService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class centralController {
    private final CentralService centralService;


    @GetMapping("/players")
    public Object getAllPlayersFromChampionships() {
        return centralService.getAllPlayersFromChampionships();
    }

}
