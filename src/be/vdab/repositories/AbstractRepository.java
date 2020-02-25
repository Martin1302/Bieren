package be.vdab.repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

abstract class AbstractRepository {
    private static final String URL = "jdbc:mysql://localhost/bieren?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Europe/Brussels";
    private static final String USER = "cursist";
    private static final String PASSWOORD = "cursist";

    Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWOORD);
    }
}
