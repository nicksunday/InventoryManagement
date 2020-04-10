package Application.inventory.product;

import Application.inventory.part.Part;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private List<Part> internalList = new ArrayList<>();
    public ObservableList<Part> associatedParts = FXCollections.observableList(internalList);
    public int id;
    public String name;
    public double price;
    public int stock;
    public int min;
    public int max;

    // Constructor
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public void setMin(int min) {
        this.min = min;
    }
    public void setMax(int max) {
        this.max = max;
    }

    // Getters
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }
    public int getStock() {
        return stock;
    }
    public int getMin() {
        return min;
    }
    public int getMax() {
        return max;
    }

    // Part Associations
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }
    public boolean deleteAssociatedPart(Part selectedAsPart) {
        try {
            associatedParts.remove(selectedAsPart);
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

}
