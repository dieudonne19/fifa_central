package org.football.fifa_central.service;

import lombok.RequiredArgsConstructor;
import org.football.fifa_central.dao.operations.ClubCrudOperations;
import org.football.fifa_central.endpoint.rest.URL;
import org.football.fifa_central.model.Championship;
import org.football.fifa_central.model.Club;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service@RequiredArgsConstructor
public class ClubService {

    private final ClubCrudOperations clubCrudOperations;

    public List<Club> sync(Championship championship){

        List<Club> clubs = clubCrudOperations.getAllFromApi(championship.getApiUrl());
        clubs.forEach(club -> {
            club.setSync_date(Instant.now());
            club.setChampionship(championship);
        });
      List<Club> synchronizedClub =  clubCrudOperations.saveAll(clubs);
        return synchronizedClub;

    }

}
