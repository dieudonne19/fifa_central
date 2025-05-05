package org.football.fifa_central.dao.mapper;

import lombok.RequiredArgsConstructor;
import org.football.fifa_central.dao.operations.DTO.ClubStatsDto;
import org.football.fifa_central.model.Club;
import org.football.fifa_central.model.ClubStats;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component@RequiredArgsConstructor
public class ClubStatsDtoMapper {

    public ClubStats toModel(ClubStatsDto dto) {
        ClubStats stats = new ClubStats();
        Club club = new Club();
        club.setId(dto.getId());
        club.setName(dto.getName());
        club.setStadium(dto.getStadium());
        club.setYearCreation(dto.getYearCreation());
        club.setAcronym(dto.getAcronym());
        club.setCoach(dto.getCoach());
        stats.setClub(club);
        stats.setConcededGoals(dto.getConcededGoals());
        stats.setRankingPoints(dto.getRankingPoints());
        stats.setDifferenceGoals(dto.getDifferenceGoals());
        stats.setCleanSheetNumber(dto.getCleanSheetNumber());
        stats.setScoredGoals(dto.getScoredGoals());
        stats.setSyncDate(Instant.now());
        return stats;

    }

}
