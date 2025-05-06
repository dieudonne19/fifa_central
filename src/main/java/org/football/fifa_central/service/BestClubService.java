package org.football.fifa_central.service;


import lombok.RequiredArgsConstructor;
import org.football.fifa_central.dao.operations.ClubStatsCrudOperations;
import org.football.fifa_central.model.ClubStats;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service@RequiredArgsConstructor
public class BestClubService {

    private final ClubStatsCrudOperations clubStatsCrudOperations;

    public ResponseEntity<Object> getBestClubs(int top){

        List<ClubStats> clubStatsList = clubStatsCrudOperations.getAll();

        clubStatsList.stream().sorted((o1, o2) -> o1.getRankingPoints().compareTo(o2.getRankingPoints()  )).limit(top).toList();
        clubStatsList.
        return ResponseEntity.ok(clubStatsList);

    }
}
