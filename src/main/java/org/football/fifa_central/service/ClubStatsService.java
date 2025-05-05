package org.football.fifa_central.service;

import lombok.RequiredArgsConstructor;
import org.football.fifa_central.dao.operations.ClubStatsCrudOperations;
import org.football.fifa_central.endpoint.rest.URL;
import org.football.fifa_central.model.Championship;
import org.football.fifa_central.model.Club;
import org.football.fifa_central.model.ClubStats;
import org.football.fifa_central.model.Season;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service@RequiredArgsConstructor
public class ClubStatsService {

    private final ClubStatsCrudOperations clubStatsCrudOperations;

    public List<ClubStats> sync(Championship championship, Season season) {
        List<ClubStats> clubStatsList = clubStatsCrudOperations.getClubStatsFromApi(championship.getApiUrl(),season);
     //   List<Club> syncClubs = clubService.sync(championship);
        clubStatsList.forEach(clubStats -> {
            clubStats.setSeason(season);
        });
       List<ClubStats> syncClubStats =  clubStatsCrudOperations.saveAll(clubStatsList);


        return syncClubStats;
    }


}
