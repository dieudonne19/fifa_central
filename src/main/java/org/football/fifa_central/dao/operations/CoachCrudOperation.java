package org.football.fifa_central.dao.operations;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.football.fifa_central.dao.DataSource;
import org.football.fifa_central.model.Coach;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
@RequiredArgsConstructor
public class CoachCrudOperation {
    private final DataSource dataSource;

    @SneakyThrows
    public Coach getByName(String name) {
        Coach coach = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT coach_name, nationality FROM coach WHERE coach_name = ?");
        ) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    coach = new Coach(resultSet.getString("coach_name"), resultSet.getString("nationality"));
                }
            }
        }
        return coach;
    }
    @SneakyThrows
    public Coach save(Coach coach){
        Coach savedCoach = null;
        try(
                Connection conn = dataSource.getConnection();
                PreparedStatement statement = conn.prepareStatement("INSERT INTO coach (coach_name, nationality) VALUES (?,?) ON CONFLICT (coach_name) do nothing " +
                        "RETURNING coach_name, nationality");
                ){
            statement.setString(1, coach.getName());
            statement.setString(2, coach.getCountry());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    savedCoach = new Coach(resultSet.getString("coach_name"), resultSet.getString("nationality"));
                }
            }
        }
        return savedCoach;
    }
}
