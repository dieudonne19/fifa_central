package org.football.fifa_central.service;

import lombok.RequiredArgsConstructor;
import org.football.fifa_central.dao.operations.ChampionshipCrudOperations;
import org.football.fifa_central.model.Championship;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChampionshipService {
    private final ChampionshipCrudOperations championshipCrudOperations;

    public List<Championship> getAll() {
        List<Championship> championships = championshipCrudOperations.getAll();
        return championships;
    }

    public List<Championship> saveAll(List<Championship> entities) {
        return championshipCrudOperations.saveAll(entities);
    }

}
