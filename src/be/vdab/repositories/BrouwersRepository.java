package be.vdab.repositories;

import be.vdab.domain.Brouwer;

import java.sql.*;
import java.util.*;


public class BrouwersRepository extends AbstractRepository {

    // Takenbundel 1.3 Gemiddelde
    // Method that sets up a connection to the dB bieren en de gemiddelde omzet berekent.
    public double gemiddeldeOmzetBrouwers() throws SQLException {
        String sql = "select avg(omzet) as gemiddeldeOmzet from brouwers;";
        try (Connection connection = super.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            result.next();
            return result.getDouble("gemiddeldeOmzet");
        }
    }

    // Takenbundel 1.3 Gemiddelde
    // Method that sets up a connection to the dB bieren en de brouwers opzoekt die een omzet hebben groter dan de gemiddelde omzet.
    public List<Brouwer> getBrouwersMetOmzetGroterDanGemiddeldeOmzet() throws SQLException {
        String sql = "select id, naam, adres, postcode, gemeente, omzet from brouwers WHERE omzet > (select avg(omzet) from brouwers);";
        try (Connection connection = super.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            List<Brouwer> brouwers = new ArrayList<>();
            while (result.next()) {
                brouwers.add(resultNaarBrouwer(result));
            }
            return brouwers;
        }
    }

    // Takenbundel 1.3 Gemiddelde
    // Routine die alle database gegevens van de brouwer in een brouwer object zet.
    private Brouwer resultNaarBrouwer(ResultSet result) throws SQLException {
        return new Brouwer(result.getLong("id"), result.getString("naam"), result.getString("adres"),
                result.getInt("postcode"), result.getString("gemeente"), result.getLong("omzet"));
    }


    // Takenbundel 1.4 Van tot
    // Method that sets up a connection to the dB bieren en de brouwers opzoekt die een omzet hebben tussen min en max opgegeven door de gebruiker
    public List<Brouwer> getBrouwersMetOmzetTussenMinMax(int minOmzet, int maxOmzet) throws SQLException {
        String sql = "select id, naam, adres, postcode, gemeente, omzet from brouwers WHERE omzet between ? and ? order by omzet, id;";
        try (Connection connection = super.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, minOmzet);
            statement.setInt(2, maxOmzet);
            try (ResultSet result = statement.executeQuery()) {
                List<Brouwer> brouwers = new ArrayList<>();
                while (result.next()) {
                    brouwers.add(resultNaarBrouwer(result));
                }
                return brouwers;
            }
        }
    }


    // Takenbundel 1.5 Id
    // Method that sets up a connection to the dB bieren en de brouwer opzoekt met een specifieke Id.
    public Optional<Brouwer> findBrouwerById(Long id) throws SQLException {
        String sql = "select id, naam, adres, postcode, gemeente, omzet from brouwers WHERE id = ?";
        try (Connection connection = super.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return Optional.of(resultNaarBrouwer(result));
                } else {
                    return Optional.empty();
                }
            }
        }
    }


    // Takenbundel 1.6 Stored procedure
    // Method that sets up a connection to the dB bieren en de brouwers opzoekt die een omzet hebben tussen min en max opgegeven door de gebruiker via stored procedure call.
    public List<Brouwer> getBrouwersMetOmzetTussenMinMaxSP(Long minOmzet, Long maxOmzet) throws SQLException {
        String call = "{call findBrouwersTussenMinMaxOmzet(?,?)}";
        try (Connection connection = super.getConnection();
             // Prepare the stored procedure call.
             CallableStatement statement = connection.prepareCall(call)) {
            // Vul de parameters in.
            statement.setLong(1, minOmzet);
            statement.setLong(2, maxOmzet);
            // Roep de stored procedure op.
            try (ResultSet result = statement.executeQuery()) {
                List<Brouwer> brouwers = new ArrayList<>();
                while (result.next()) {
                    brouwers.add(resultNaarBrouwer(result));
                }
                return brouwers;
            }
        }
    }


    // Takenbundel 1.13 Omzet leeg maken
    // Method that sets up a connection to the dB bieren and resets revenue for the given brouwer ids.
    public int brouwersOmzetLeegMaken(Set<Long> brouwerIds) throws SQLException {
        StringBuilder sql = new StringBuilder("update brouwers set omzet = null where id in (");
        sql.append("?,".repeat(brouwerIds.size()));
        sql.setCharAt(sql.length() - 1, ')');
        try (Connection connection = getConnection();
             // Prepare the sql commands.
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            // Vul de parameters in.
            int i = 1;
            for (Long id : brouwerIds) {
                statement.setLong(i++, id);
            }
            // Set Isolation level
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            // Set AutoCommit to false in order to execute all commands in one transaction.
            connection.setAutoCommit(false);
            // Execute the query
            int aangepast = statement.executeUpdate();
            // Close transaction
            connection.commit();
            return aangepast;
        }
    }

    // Takenbundel 1.13 Omzet leeg maken
    // Method that sets up a connection to the dB bieren and retrieves the given ids that exists.
    public Set<Long> findBrouwerIds(Set<Long> brouwerIds) throws SQLException {
        StringBuilder sql = new StringBuilder("select id from brouwers where id in (");
        sql.append("?,".repeat(brouwerIds.size()));
        sql.setCharAt(sql.length() - 1, ')');
        try (Connection connection = getConnection();
             // Prepare the sql commands.
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            // Vul de parameters in.
            int i = 1;
            for (Long id : brouwerIds) {
                statement.setLong(i++, id);
            }
            // Set Isolation level
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            // Set AutoCommit to false in order to execute all commands in one transaction.
            connection.setAutoCommit(false);
            // Execute the query
            try (ResultSet result = statement.executeQuery()) {
                Set<Long> gevondenIds = new HashSet<>();
                while (result.next()) {
                    gevondenIds.add(result.getLong("id"));
                }
                // Close transaction
                connection.commit();
                return gevondenIds;
            }
        }
    }
}
