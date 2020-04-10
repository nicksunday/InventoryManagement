package Application.inventory.part.outsourced;

import Application.inventory.part.Part;

public class Outsourced extends Part {
    public String companyName;

    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    // Setter
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    // Getter
    public String getCompanyName() {
        return companyName;
    }
}
