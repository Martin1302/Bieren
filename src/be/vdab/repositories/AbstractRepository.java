package be.vdab.repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

abstract class AbstractRepository {
    private static final String URL = "jdbc:mysql://localhost/bieren?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Europe/Brussels&noAccessToProcedureBodies=true";
    private static final String USER = "cursist";
    private static final String PASSWOORD = "cursist";

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWOORD);
    }
}
