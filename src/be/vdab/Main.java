package be.vdab;

import be.vdab.repositories.BierenRepository;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        BierenRepository bierenRepository = new BierenRepository();
        try {
            System.out.println(bierenRepository.deleteBierenWhereAlcoholIsNull());
            System.out.println("Bieren zonder alcohol percentage gedeleted");
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
