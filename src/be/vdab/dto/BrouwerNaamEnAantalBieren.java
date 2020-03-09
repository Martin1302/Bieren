package be.vdab.dto;

// Data transfer object for number of beers per brewer
public class BrouwerNaamEnAantalBieren {
    private final int aantalBieren;
    private final String brouwer;

    // Constructor
    public BrouwerNaamEnAantalBieren(int aantalBieren, String brouwer) {
        this.aantalBieren = aantalBieren;
        this.brouwer = brouwer;
    }

    // Getter
    public int getAantalBieren() {
        return aantalBieren;
    }

    // Getter
    public String getBrouwer() {
        return brouwer;
    }
}
