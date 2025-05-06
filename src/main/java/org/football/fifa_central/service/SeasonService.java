package org.football.fifa_central.service;


import lombok.RequiredArgsConstructor;
import org.football.fifa_central.dao.operations.SeasonCrudOperations;
import org.football.fifa_central.model.Championship;
import org.football.fifa_central.model.Season;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeasonService {

    private final SeasonCrudOperations seasonCrudOperations;

    public List<Season> sync(Championship championship) {
        List<Season> seasonToSave = seasonCrudOperations.getSeasonsFromApi(championship);
        seasonToSave.forEach(season -> {
            season.setChampionship(championship);
        });
        List<Season> savedSeason = seasonCrudOperations.saveAll(seasonToSave);
        return savedSeason;
    }

}
