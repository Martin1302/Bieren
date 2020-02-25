package be.vdab.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BierenRepository extends AbstractRepository {

    // Method that sets up a connection to the dB bieren and deletes all bieren without alcohol percentage.
    public int deleteBierenWhereAlcoholIsNull() throws SQLException {
        String sql = "delete from bieren where alcohol is null;";
        // Set up a dB connection.
        try (Connection connection = getConnection();
             // Prepare the sql command.
             PreparedStatement statement = connection.prepareStatement(sql)) {
            // Execute the sql command.
            return statement.executeUpdate();
        }
    }
}
