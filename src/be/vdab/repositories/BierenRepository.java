package be.vdab.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BierenRepository extends AbstractRepository {

    // Takenbundel 1.2 Bieren verwijderen
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


    // Takenbundel 1.7 Brouwer met id=1 failliet
    // Takenbundel 1.8 Isolation level
    // Method that sets up a connection to the dB bieren. Bieren >= 8.5% gaan naar brouwer 2. Bieren < 8.5% gaan naar brouwer 3. Brouwer 1 wordt verwijderd.
    public void brouwer1Failliet() throws SQLException {
        String sqlBrouwer1NaarBrouwer2 = "update bieren set brouwerid = 2 where ((brouwerid = 1) and (alcohol >= 8.50))";
        String sqlBrouwer1NaarBrouwer3 = "update bieren set brouwerid = 3 where brouwerid = 1";
        String sqlDeleteBrouwer1 = "delete from brouwers where id = 1";
        // Set up a dB connection.
        try (Connection connection = getConnection();
             // Prepare the sql commands.
             PreparedStatement statementBrouwer1NaarBrouwer2 = connection.prepareStatement(sqlBrouwer1NaarBrouwer2);
             PreparedStatement statementBrouwer1NaarBrouwer3 = connection.prepareStatement(sqlBrouwer1NaarBrouwer3);
             PreparedStatement statementDeleteBrouwer1 = connection.prepareStatement(sqlDeleteBrouwer1)) {
            // Set Isolation level
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            // Set AutoCommit to false in order to execute all commands in one transaction.
            connection.setAutoCommit(false);
            // Execute the sql command.
            statementBrouwer1NaarBrouwer2.executeUpdate();
            statementBrouwer1NaarBrouwer3.executeUpdate();
            statementDeleteBrouwer1.executeUpdate();
            // Commit all executed commands
            connection.commit();
        }
    }
}
