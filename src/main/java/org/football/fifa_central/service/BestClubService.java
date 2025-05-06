package org.football.fifa_central.service;


import lombok.RequiredArgsConstructor;
import org.football.fifa_central.dao.operations.ClubStatsCrudOperations;
import org.football.fifa_central.model.Club;
import org.football.fifa_central.model.ClubStats;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BestClubService {

    private final ClubStatsCrudOperations clubStatsCrudOperations;

    public ResponseEntity<Object> getBestClubs(int top) {

        List<ClubStats> clubStatsList = clubStatsCrudOperations.getAll();

        List<ClubStats> sortedStats = clubStatsList.stream().sorted(Comparator.comparingInt(ClubStats::getRankingPoints).reversed()
                        .thenComparing(Comparator.comparingInt(ClubStats::getDifferenceGoals).reversed()
                                .thenComparing(Comparator.comparingInt(ClubStats::getCleanSheetNumber).reversed())))
                .limit(top).toList();

        return ResponseEntity.ok(sortedStats);

    }

    public List<ClubStats> getBestClubsForUse(int top) {

        List<ClubStats> clubStatsList = clubStatsCrudOperations.getAll();

        List<ClubStats> sortedStats = clubStatsList.stream()
                .sorted(Comparator.comparingInt(ClubStats::getRankingPoints).reversed()
                        .thenComparing(Comparator.comparingInt(ClubStats::getDifferenceGoals).reversed()
                                .thenComparing(Comparator.comparingInt(ClubStats::getCleanSheetNumber).reversed())))
                .limit(top).toList();

        return sortedStats;

    }
}
