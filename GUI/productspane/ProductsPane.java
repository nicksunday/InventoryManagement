package GUI.productspane;

import Application.inventory.Inventory;
import Application.inventory.part.Part;
import Application.inventory.product.Product;
import GUI.productspane.addmodifyproduct.AddModifyProduct;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.text.NumberFormat;
import java.util.Optional;

public class ProductsPane extends BorderPane {

    public static TableView ProductTable(Inventory inventory) {

        TableView<Product> productsTV = new TableView();
        productsTV.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        productsTV.setPrefHeight(120);

        TableColumn productsIDColumn = new TableColumn("Products ID");
        productsIDColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        TableColumn productsNameColumn = new TableColumn("Products Name");
        productsNameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        TableColumn productsInventory = new TableColumn("Inventory Level");
        productsInventory.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
        TableColumn productsCost = new TableColumn("Price/Cost per unit");
        productsCost.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        productsCost.setCellFactory(p -> new TableCell<Product, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(NumberFormat.getCurrencyInstance().format(price));
                }
            }
        });

        productsTV.getColumns().addAll(productsIDColumn, productsNameColumn, productsInventory, productsCost);
        productsTV.setStyle("-fx-alignment: center-left");

        return productsTV;
    }

    public BorderPane newPane(Inventory inventory) {
        //
        // Root pane to return
        //
        BorderPane productsPane = new BorderPane();
        productsPane.setPadding(new Insets(20));

        // Center section
        TableView<Product> productsTV = ProductTable(inventory);
        productsTV.setItems(inventory.getAllProducts());

        //
        // Top section layout
        //
        Text productsTitle = new Text("Products");
        productsTitle.setFill(Color.BLACK);
        productsTitle.setFont(Font.font("Verdana", FontWeight.MEDIUM, 15));

        FlowPane productsFlow = new FlowPane();
        productsFlow.setHgap(10);
        Button productsSearchBtn = new Button("Search");
        Button invisibleBtn = new Button();
        invisibleBtn.setVisible(false);
        TextField productsSearchText = new TextField();
        productsFlow.getChildren().addAll(productsSearchBtn,productsSearchText);

        productsSearchBtn.setOnAction((ActionEvent e) -> {
            if (productsSearchText.getText().isEmpty()) {
                productsTV.setItems(inventory.getAllProducts());
            } else {
                if (productsSearchText.getText().chars().allMatch(Character::isDigit)) {
                    int productsSearchVar = Integer.parseInt(productsSearchText.getText().trim());
                    Product product = inventory.lookupProduct(productsSearchVar);
                    productsTV.setItems(inventory.lookupProduct(product.getName()));
                } else {
                    String productsSearchVar = productsSearchText.getText();
                    productsTV.setItems(inventory.lookupProduct(productsSearchVar));
                }
            }
        });

        GridPane productsTopGP = new GridPane();
        productsTopGP.setHgap(10);
        productsTopGP.setVgap(15);
        productsTopGP.setPadding(new Insets(10));

        ColumnConstraints col1Constraints = new ColumnConstraints();
        col1Constraints.setPercentWidth(15);
        ColumnConstraints col2Constraints = new ColumnConstraints();
        col2Constraints.setPercentWidth(30);
        ColumnConstraints col3Constraints = new ColumnConstraints();
        col3Constraints.setPercentWidth(15);
        productsTopGP.getColumnConstraints().addAll(col1Constraints, col2Constraints, col3Constraints);

        GridPane.setHalignment(productsTitle, HPos.LEFT);
        productsTopGP.add(productsTitle, 0,0,1,1);
        GridPane.setHalignment(productsFlow, HPos.RIGHT);
        productsTopGP.add(productsFlow,2,0,2,1);

        //
        // Bottom section layout
        //
        GridPane productsBottomGrid = new GridPane();
        productsBottomGrid.setHgap(20);
        productsBottomGrid.setPadding(new Insets(5,20,20,5));
        productsBottomGrid.setAlignment(Pos.BOTTOM_RIGHT);

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setContentText("Are you sure?");
        confirmation.setHeaderText(null);
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType cancelConfirm = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmation.getButtonTypes().setAll(yesButton, cancelConfirm);

        Button addProductsBtn = new Button("Add");
        addProductsBtn.setOnAction(e -> {
            AddModifyProduct AP = new AddModifyProduct();
            AP.main(inventory);
        });

        Button modifyProductsBtn = new Button("Modify");
        modifyProductsBtn.setOnAction(e -> {
            try {
                Product selectedProduct = productsTV.getSelectionModel().getSelectedItem();
                AddModifyProduct AP = new AddModifyProduct();
                AP.setIsModify(selectedProduct);
                AP.main(inventory);
                productsTV.getSelectionModel().clearSelection();
            }
            catch (NullPointerException nullPointer) {}
        });
        Button deleteProductsBtn = new Button("Delete");
        deleteProductsBtn.setOnAction((ActionEvent e) -> {
            Product product = productsTV.getSelectionModel().getSelectedItem();
            if (product != null) {
                Optional<ButtonType> result = confirmation.showAndWait();
                if (result.get() == yesButton) {
                    inventory.deleteProduct(product);
                }
            }
        });


        productsBottomGrid.add(addProductsBtn,0,0);
        productsBottomGrid.add(modifyProductsBtn,1,0);
        productsBottomGrid.add(deleteProductsBtn,2,0);

        // Assemble products pane
        //productsPane.setTop(productsTopBP);
        productsPane.setTop(productsTopGP);
        productsPane.setCenter(productsTV);
        productsPane.setBottom(productsBottomGrid);
        productsPane.getStylesheets().add("GUI/productspane/ProductsPane.css");
        productsPane.setStyle("-fx-border-color: black");

        return productsPane;
    }
}
