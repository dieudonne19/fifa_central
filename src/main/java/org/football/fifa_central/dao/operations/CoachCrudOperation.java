package org.football.fifa_central.dao.operations;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.football.fifa_central.dao.DataSource;
import org.football.fifa_central.model.Coach;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository@RequiredArgsConstructor
public class CoachCrudOperation {
    private final DataSource dataSource;

    @SneakyThrows
    public Coach getByName(String name){
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

}
