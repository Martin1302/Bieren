package be.vdab;

import be.vdab.domain.Brouwer;
import be.vdab.repositories.BierenRepository;
import be.vdab.repositories.BrouwersRepository;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Takenbundel 1.2 Bieren verwijderen
        // Verwijder alle bieren waarvoor geen alcohol percentage is opgegeven.
        BierenRepository bierenRepository = new BierenRepository();
        try {
            System.out.println(bierenRepository.deleteBierenWhereAlcoholIsNull());
            System.out.println("Bieren zonder alcohol percentage gedeleted");
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }


        // Takenbundel 1.3 Gemiddelde
        // Toon de gemiddelde omzet van alle brouwers en daarna alle brouwers die een omzet hebben groter dan dat gemiddelde
        BrouwersRepository brouwersRepository = new BrouwersRepository();
        try {
            // Toon de gemiddelde omzet;
            System.out.println("\nDe gemiddelde omzet van alle brouwers is : " + brouwersRepository.gemiddeldeOmzetBrouwers() + "\n");

            // Toon alle brouwers met een omzet groter dan dat gemiddelde.
            for (Brouwer brouwer : brouwersRepository.getBrouwersMetOmzetGroterDanGemiddeldeOmzet()) {
                System.out.println(brouwer);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }


        // Takenbundel 1.4 Van tot
        // Toon alle brouwers die een omzet hebben tussen min en max opgegeven door de gebruiker
        try {
            // Vraag de gebruiker een min en max omzet in te geven.
            Scanner scanner = new Scanner(System.in);
            System.out.println("\nGeef de minimum omzet in :");
            int minOmzet = scanner.nextInt();
            System.out.println("Geef de maximum omzet in :");
            int maxOmzet = scanner.nextInt();

            // Toon alle brouwers met een omzet tussen min en max.
            for (Brouwer brouwer : brouwersRepository.getBrouwersMetOmzetTussenMinMax(minOmzet, maxOmzet)) {
                System.out.println(brouwer);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
