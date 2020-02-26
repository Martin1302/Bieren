package be.vdab.repositories;

import be.vdab.domain.Brouwer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


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
}
