package be.vdab;

import be.vdab.domain.Brouwer;
import be.vdab.dto.BrouwerNaamEnAantalBieren;
import be.vdab.repositories.BierenRepository;
import be.vdab.repositories.BrouwersRepository;

import java.sql.SQLException;
import java.util.*;

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
        // Vraag de gebruiker een min en max omzet in te geven.
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nGeef de minimum omzet in :");
        int minOmzet = scanner.nextInt();
        System.out.println("Geef de maximum omzet in :");
        int maxOmzet = scanner.nextInt();
        try {
            // Toon alle brouwers met een omzet tussen min en max.
            for (Brouwer brouwer : brouwersRepository.getBrouwersMetOmzetTussenMinMax(minOmzet, maxOmzet)) {
                System.out.println(brouwer);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }


        // Takenbundel 1.5 Id
        // Method that sets up a connection to the dB bieren en de brouwer opzoekt met een specifieke Id.
        // Vraag de gebruiker een id in te geven.
        System.out.println("\nGeef de id van de brouwer in de tabel :");
        long id = scanner.nextLong();
        try {
            // Toon de brouwer met deze id.
            Optional<Brouwer> optionalBrouwer = brouwersRepository.findBrouwerById(id);
            if (optionalBrouwer.isPresent()) {
                System.out.println(optionalBrouwer.get());
            } else {
                System.out.println("Brouwer met id : " + id + " niet gevonden !");
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }


        // Takenbundel 1.6 Stored procedures
        // Toon alle brouwers die een omzet hebben tussen min en max opgegeven door de gebruiker
        // Vraag de gebruiker een min en max omzet in te geven.
        // Maak gebruik van de stored procedure "findBrouwersTussenMinMaxOmzet" in dB bieren
        System.out.println("\nGeef de minimum omzet in (voor stored procedure call) :");
        Long minOmzetL = scanner.nextLong();
        System.out.println("Geef de maximum omzet in (voor stored procedure call) :");
        Long maxOmzetL = scanner.nextLong();
        try {
            // Toon alle brouwers met een omzet tussen min en max.
            for (Brouwer brouwer : brouwersRepository.getBrouwersMetOmzetTussenMinMaxSP(minOmzetL, maxOmzetL)) {
                System.out.println(brouwer);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }


        // Takenbundel 1.7 Brouwer met id=1 failliet
        // Method that sets up a connection to the dB bieren. Bieren >= 8.5% gaan naar brouwer 2. Bieren < 8.5% gaan naar brouwer 3. Brouwer 1 wordt verwijderd.
        System.out.println("\nBrouwer 1 failliet. Bieren >= 8.50 % naar brouwer 2. Bieren < 8.50 % naar brouwer 3");
        try {
            bierenRepository.brouwer1Failliet();
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }


        // Takenbundel 1.9 Bieren van de maand
        // Method that sets up a connection to the dB bieren and retrieves all beers introduced in a particular month input by the user.
        System.out.println("\nGeef een maand in :");
        int maand = scanner.nextInt();
        scanner.skip("\n");
        try {
            // Toon alle bieren verkocht sinds de maand
            for (String bierNaam : bierenRepository.getBierenVerkochtSindsMaand(maand)) {
                System.out.println(bierNaam);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }


        // Takenbundel 1.10 Aantal bieren per brouwer
        // Method that sets up a connection to the dB bieren and retrieves the number of beers per brewer sorted per brewer;
        System.out.println("\nHet aantal bieren per brouwer is : ");
        try {
            // Toon per brouwer hoeveel bieren hij brouwt
            for (BrouwerNaamEnAantalBieren brouwerNaamEnAantalBieren : bierenRepository.getAantalBierenPerBrouwer()) {
                System.out.println(brouwerNaamEnAantalBieren.getBrouwer() + " : " + brouwerNaamEnAantalBieren.getAantalBieren());
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }


        // Takenbundel 1.12 Bieren van een soort
        // Method that sets up a connection to the dB bieren and retrieves the beers of a particular kind;
        System.out.println("\nGeef een soort bier op waarvoor de bieren opgehaald woorden:");
        String bierSoort = scanner.nextLine();
        try {
            List<String> biernamenPerSoort = bierenRepository.getBierenVanSoort(bierSoort);
            if (biernamenPerSoort.isEmpty()) {
                System.out.println("Geen bieren voor deze soort (" + bierSoort + ") gevonden.");
            } else {
                // Toon de bieren van een bepaalde soort
                for (String bierNaam : biernamenPerSoort) {
                    System.out.println(bierNaam);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }


        // Takenbundel 1.13 Omzet leeg maken
        // Method that sets up a connection to the dB bieren and resets revenue for the given brouwer ids.
        System.out.println("\nGeef de brouwer ids op wiens omzet gereset wordt (0 om te stoppen)");
        Set<Long> ids = new LinkedHashSet<>();
        id = scanner.nextLong();
        while (id != 0) {
            if (id > 0) {
                // Voeg id toe aan de set. Add returns false if already present.
                if (!ids.add(id)) {
                    System.out.println("Id = " + id + " was reeds opgegeven !");
                }
            } else {
                System.out.println("Negatieve waarde voor id !");
            }
            id = scanner.nextLong();
        }
        try {
            if (!ids.isEmpty()) {
                if (brouwersRepository.brouwersOmzetLeegMaken(ids) != ids.size()) {
                    System.out.print("Niet alle ids werden aangepast : ");
                    Set<Long> gevondenIds = brouwersRepository.findBrouwerIds(ids);
                    for (Long opgegevenId : ids) {
                        if (!gevondenIds.contains(opgegevenId)) {
                            System.out.print(opgegevenId + ", ");
                        }
                    }
                }
            } else {
                System.out.println("Geen ids opgegeven !");
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
