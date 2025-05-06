package org.football.fifa_central.endpoint;

import lombok.RequiredArgsConstructor;
import org.football.fifa_central.service.BestClubService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController@RequiredArgsConstructor
public class BestClubsController {

    private final BestClubService bestClubService;

    @GetMapping("/bestClubs")
    public Object getBestClubs(@RequestParam(required = false,defaultValue = "5") int top) {
        return bestClubService.getBestClubs(top);
    }

}
