package GUI.partspane.partstable;

import Application.inventory.part.Part;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.NumberFormat;

public class PartsTable {

    //public static TableView<Part> NewTable() {
    public static TableView<Part> formatTable(TableView<Part> partsTV) {

        partsTV.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        partsTV.setPrefHeight(120);

        TableColumn partIDColumn = new TableColumn("Part ID");
        partIDColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        TableColumn partNameColumn = new TableColumn("Part Name");
        partNameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        TableColumn partInventory = new TableColumn("Inventory Level");
        partInventory.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        TableColumn partCost = new TableColumn("Price/Cost per unit");
        partCost.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        partCost.setCellFactory(p -> new javafx.scene.control.TableCell<Part, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    try {
                        setText(NumberFormat.getCurrencyInstance().format(price));
                    } catch (IllegalArgumentException e) {
                    }
                }
            }
        });

        partsTV.getColumns().addAll(partIDColumn, partNameColumn, partInventory, partCost);
        partsTV.setStyle("-fx-alignment: center-left");

        return partsTV;
    }
}