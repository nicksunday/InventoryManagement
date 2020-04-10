package Application.inventory;

import Application.inventory.part.Part;
import Application.inventory.product.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private static List<Part> parts = new ArrayList<Part>();
    private static List<Product> products = new ArrayList<Product>();
    public static ObservableList<Part> allParts = FXCollections.observableList(parts);
    public static ObservableList<Product> allProducts = FXCollections.observableList(products);

    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    public static Part lookupPart(int partId) {
        return allParts.get(partId - 1);
    }

    public static Product lookupProduct(int productId) {
        return allProducts.get(productId - 1);
    }

    public static ObservableList<Part> lookupPart(String partName) {
        List lookupList = new ArrayList<Part>();
        ObservableList<Part> newList = FXCollections.observableList(lookupList);
        allParts.forEach(part -> {
            if (part.getName().contains(partName)) {
                newList.add(part);
            }
        });
        return newList;
    }

    public static ObservableList<Product> lookupProduct(String productName) {
        List lookupList = new ArrayList<Part>();
        ObservableList<Product> newList = FXCollections.observableList(lookupList);
        allProducts.forEach(product -> {
            if (product.getName().contains(productName)) {
                newList.add(product);
            }
        });
        return newList;
    }

    public static void updatePart(int index, Part selectedPart) {
        allParts.remove(index - 1);
        allParts.add(index - 1, selectedPart);
    }

    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.remove(index - 1);
        allProducts.add(index - 1, selectedProduct);
    }

    public static boolean deletePart(Part selectedPart) {
        try {
            allParts.remove(selectedPart);
            return true;
        }
        catch(IndexOutOfBoundsException e) {
            return false;
        }
    }

    public static boolean deleteProduct(Product selectedProduct) {
        try {
            allProducts.remove(selectedProduct);
            return true;
        }
        catch(IndexOutOfBoundsException e) {
            return false;
        }
    }

    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
