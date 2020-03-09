package be.vdab.repositories;

import be.vdab.dto.BrouwerNaamEnAantalBieren;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
            // Execute the sql commands.
            statementBrouwer1NaarBrouwer2.executeUpdate();
            statementBrouwer1NaarBrouwer3.executeUpdate();
            statementDeleteBrouwer1.executeUpdate();
            // Commit all executed commands
            connection.commit();
        }
    }

    // Takenbundel 1.9 Bieren van de maand
    // Method that sets up a connection to the dB bieren and retrieves all beers introduced in a particular month input by the user.
    public List<String> getBierenVerkochtSindsMaand(int maand) throws SQLException {
        String sql = "select naam from bieren where {fn month(sinds)} = ? order by sinds";
        // Set up a dB connection.
        try (Connection connection = getConnection();
             // Prepare the sql commands.
             PreparedStatement statement = connection.prepareStatement(sql)) {
            // Vul de parameters in.
            statement.setInt(1, maand);
            // Set Isolation level
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            // Set AutoCommit to false in order to execute all commands in one transaction.
            connection.setAutoCommit(false);
            // Execute the query
            try (ResultSet result = statement.executeQuery()) {
                List<String> bierNamen = new ArrayList<>();
                while (result.next()) {
                    bierNamen.add(result.getString("naam"));
                }
                connection.commit();
                return bierNamen;
            }
        }
    }


    // Takenbundel 1.10 Aantal bieren per brouwer
    // Method that sets up a connection to the dB bieren and retrieves the number of beers per brewer sorted per brewer;
    public List<BrouwerNaamEnAantalBieren> getAantalBierenPerBrouwer() throws SQLException {
        String sql = "select count(*) as aantalBieren, brouwers.naam from bieren inner join brouwers " +
                "on bieren.brouwerid = brouwers.id group by brouwerid order by brouwers.naam";
        // Set up a dB connection.
        try (Connection connection = getConnection();
             // Prepare the sql commands.
             PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set Isolation level
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            // Set AutoCommit to false in order to execute all commands in one transaction.
            connection.setAutoCommit(false);
            // Execute the query
            try (ResultSet result = statement.executeQuery()) {
                List<BrouwerNaamEnAantalBieren> aantalBierenPerBrouwer = new ArrayList<>();
                while (result.next()) {
                    aantalBierenPerBrouwer.add(new BrouwerNaamEnAantalBieren(result.getInt("aantalBieren"), result.getString("naam")));
                }
                connection.commit();
                return aantalBierenPerBrouwer;
            }
        }
    }
}
