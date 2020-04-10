package Application.inventory.part.inhouse;

import Application.inventory.part.Part;

public class InHouse extends Part {
    int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    // Setter
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    // Getter
    public int getMachineId() {
        return machineId;
    }
}
