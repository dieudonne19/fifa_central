package org.football.fifa_central.service;

import lombok.RequiredArgsConstructor;
import org.football.fifa_central.dao.operations.ChampionshipCrudOperations;
import org.football.fifa_central.dao.operations.ClubCrudOperations;
import org.football.fifa_central.dao.operations.ClubStatsCrudOperations;
import org.football.fifa_central.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChampionshipService {
    private final ChampionshipCrudOperations championshipCrudOperations;
    private final ClubStatsCrudOperations clubStatsCrudOperations;
    private final ClubCrudOperations clubCrudOperations;
    private final BestClubService bestClubService;

    public List<Championship> getAll() {
        List<Championship> championships = championshipCrudOperations.getAll();
        return championships;
    }

    public List<Championship> saveAll(List<Championship> entities) {
        return championshipCrudOperations.saveAll(entities);
    }

    public ResponseEntity<Object> rankChampionship() {
        List<Championship> championships = this.getAll();

        if (championships.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No championship found.");
        }

        List<Club> allClubs = clubCrudOperations.getAll();
        if (allClubs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No club found.");
        }

        List<ClubStats> allClubsStats = bestClubService.getBestClubsForUse(allClubs.size());

        List<ChampionshipRanking> championshipRanking = new ArrayList<>();

        championships.forEach(championship -> {
            ChampionshipRanking ranking = new ChampionshipRanking();
            ranking.setChampionship(championship);
            ranking.setId("RK-" + championship.getId());

            List<String> clubIds = championship.getClubs().stream().map(Club::getId).toList();
            List<Integer> clubsDifferenceGoals = allClubsStats.stream()
                    .filter(clubStats -> clubIds.contains(clubStats.getClub().getId()))
                    .map(ClubStats::getDifferenceGoals)
                    .sorted()
                    .toList();

            int medianValue = 0;
            if (clubsDifferenceGoals.size() % 2 != 0) {
                int idx = clubsDifferenceGoals.size() / 2;
                medianValue = clubsDifferenceGoals.get(idx);

            } else {
                int firstIdx = clubsDifferenceGoals.size() / 2;
                int secondIdx = firstIdx + 1;

                medianValue = (clubsDifferenceGoals.get(firstIdx) + clubsDifferenceGoals.get(secondIdx)) / 2;
            }

            ranking.setDifferenceGoalMedian(medianValue);

            championshipRanking.add(ranking);
        });

        List<ChampionshipRanking> orderedRankings = championshipRanking.stream()
                .sorted(Comparator.comparing(ChampionshipRanking::getDifferenceGoalMedian))
                .toList();

        // List<ChampionshipRanking> ranked = new ArrayList<>();
        for (int i = 0; i < orderedRankings.size(); i++) {
            orderedRankings.get(i).setRank(i + 1);
        }

        return ResponseEntity.ok(orderedRankings);
    }

}
