package GUI.productspane.addmodifyproduct;

import Application.inventory.Inventory;
import Application.inventory.part.Part;
import Application.inventory.product.Product;
import GUI.partspane.PartsPane;
import GUI.partspane.partstable.PartsTable;
import GUI.productspane.ProductsPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddModifyProduct {
    Text title = new Text("Add Product");
    Boolean isModify = false;
    String productIDString = "Auto Gen - Disabled";

    public void setIsModify(Product currentProduct) {
        this.isModify = true;
        if (isModify) {
            this.title = new Text("Modify Product");
            this.productIDString = String.valueOf(currentProduct.getId());
        }
    }

    public static void addTextFieldError(TextField tf) {
        tf.getStyleClass().add("error");
    }
    public static void removeTextFieldError(TextField tf) {
        tf.getStyleClass().remove("error");
    }

    public void main(Inventory inventory) {

        BorderPane root = new BorderPane();

        GridPane leftHandGP = new GridPane();
        leftHandGP.setAlignment(Pos.CENTER_LEFT);
        leftHandGP.setHgap(10);
        leftHandGP.setVgap(15);
        leftHandGP.setPadding(new Insets(15));

        // First ROw
        title.setFill(Color.BLACK);
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

        GridPane.setHalignment(title, HPos.CENTER);
        leftHandGP.add(title, 0, 0);


        // Second Row
        Text productID = new Text("ID");
        productID.setFont(Font.font("Verdana", FontWeight.MEDIUM, 12));
        TextField idField = new TextField(productIDString);
        idField.setDisable(true);
        idField.setStyle("-fx-background-color: lightgrey");

        leftHandGP.add(productID,0,2);
        leftHandGP.add(idField,1,2,2,1);

        // Third Row
        Text name = new Text("Name");
        name.setFont(Font.font("Verdana", FontWeight.MEDIUM, 12));
        TextField nameField = new TextField();
        nameField.setPromptText("Product Name");

        leftHandGP.add(name,0,3);
        leftHandGP.add(nameField,1,3,2,1);

        // Fourth Row
        Text invLvl = new Text("Inv");
        invLvl.setFont(Font.font("Verdana", FontWeight.MEDIUM, 12));
        TextField invField = new TextField();
        invField.setPromptText("Inv");

        leftHandGP.add(invLvl,0,4);
        leftHandGP.add(invField,1,4,1,1);

        // Fifth Row
        Text price = new Text("Price");
        price.setFont(Font.font("Verdana", FontWeight.MEDIUM, 12));
        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        leftHandGP.add(price, 0, 5);
        leftHandGP.add(priceField,1,5,1,1);

        // Sixth Row
        Text max = new Text("Max");
        max.setFont(Font.font("Verdana", FontWeight.MEDIUM, 12));
        TextField maxInv = new TextField();
        maxInv.setPromptText("Max");
        Text min = new Text("Min");
        min.setFont(Font.font("Verdana", FontWeight.MEDIUM, 12));
        TextField minInv = new TextField();
        minInv.setPromptText("Min");
        Text minError = new Text("Min must be less than Max.");
        minError.setFont(Font.font("Verdana",FontWeight.MEDIUM, 12));
        minError.setFill(Color.RED);
        minError.setVisible(false);

        leftHandGP.add(max, 0, 6);
        leftHandGP.add(maxInv,1,6,1,1);
        leftHandGP.add(min,2,6);
        leftHandGP.add(minInv,3,6,1,1);
        leftHandGP.add(minError,3,7,1,1);

        //
        // Right hand
        //

        BorderPane rightHandPane = new BorderPane();
        rightHandPane.setPadding(new Insets(20));

        // Top of BorderPane
        FlowPane partsFlow = new FlowPane();
        partsFlow.setHgap(10);
        Button partsSearchBtn = new Button("Search");
        TextField partsSearchText = new TextField();
        partsFlow.getChildren().addAll(partsSearchBtn,partsSearchText);
        GridPane topGP = new GridPane();
        topGP.setPadding(new Insets(6,10,5,50));
        topGP.add(partsFlow, 0,0,3,1);

        rightHandPane.setTop(topGP);

        //
        // Middle of BorderPane
        //

        VBox middleVBox = new VBox();

        BorderPane addPane = new BorderPane();
        PartsTable pt1 = new PartsTable();
        TableView<Part> newTV1 = new TableView<>();
        TableView<Part> productTopTV = pt1.formatTable(newTV1);
        productTopTV.setItems(inventory.getAllParts());
        productTopTV.autosize();

        partsSearchBtn.setOnAction((ActionEvent e) -> {
            if (partsSearchText.getText().isEmpty()) {
                productTopTV.setItems(inventory.getAllParts());
            } else {
                if (partsSearchText.getText().chars().allMatch(Character::isDigit)) {
                    int partsSearchVar = Integer.valueOf(partsSearchText.getText().trim());
                    Part part = inventory.lookupPart(partsSearchVar);
                    productTopTV.setItems(inventory.lookupPart(part.getName()));
                } else {
                    String partsSearchVar = partsSearchText.getText();
                    productTopTV.setItems(inventory.lookupPart(partsSearchVar));
                }
            }
        });

        List<Part> apList = new ArrayList<>();
        ObservableList<Part> associatedPartInventory = FXCollections.observableList(apList);
        if (isModify) {
            associatedPartInventory.clear();
            for (Part part : inventory.lookupProduct(Integer.parseInt(productIDString)).getAllAssociatedParts()) {
                if (!associatedPartInventory.contains(part)) {
                    associatedPartInventory.add(part);
                }
            }
        }
        BorderPane deletePane = new BorderPane();

        PartsTable pt2 = new PartsTable();
        TableView<Part> newTV2 = new TableView<>();
        TableView<Part> productBottomTV = pt2.formatTable(newTV2);
        productBottomTV.setItems(associatedPartInventory);
        productBottomTV.autosize();

        Button addBtn = new Button("Add");
        addBtn.setOnAction((ActionEvent e) -> {
            Part part = productTopTV.getSelectionModel().getSelectedItem();
            if (part != null) {
                associatedPartInventory.add(part);
                productTopTV.getSelectionModel().clearSelection();
            }
        });

        GridPane addGP = new GridPane();
        addGP.setPadding(new Insets(5,20,5,5));
        addGP.setAlignment(Pos.BOTTOM_RIGHT);
        addGP.add(addBtn,0,0);

        addPane.setCenter(productTopTV);
        addPane.setBottom(addGP);

        // Confirmation button:
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setContentText("Are you sure?");
        confirmation.setHeaderText(null);
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType cancelProduct = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmation.getButtonTypes().setAll(yesButton, cancelProduct);

        Button deleteBtn = new Button("Delete");
        deleteBtn.setOnAction((ActionEvent e) -> {
            Part part = productBottomTV.getSelectionModel().getSelectedItem();
            if (part != null) {
                Optional<ButtonType> result = confirmation.showAndWait();
                if (result.get() == yesButton) {
                    associatedPartInventory.remove(part);
                }
            }
        });

        GridPane deleteGP = new GridPane();
        deleteGP.setPadding(new Insets(5,20,5,5));
        deleteGP.setAlignment(Pos.BOTTOM_RIGHT);
        deleteGP.add(deleteBtn,0,0);

        deletePane.setCenter(productBottomTV);
        deletePane.setBottom(deleteGP);

        middleVBox.getChildren().addAll(addPane, deletePane);

        rightHandPane.setCenter(middleVBox);

        //
        List<TextField> textFields = new ArrayList<>();
        textFields.add(nameField);
        textFields.add(invField);
        textFields.add(priceField);
        textFields.add(minInv);
        textFields.add(maxInv);

        // Bottom
        GridPane bottomGP = new GridPane();
        bottomGP.setAlignment(Pos.BOTTOM_RIGHT);
        bottomGP.setPadding(new Insets(5,20,20,5));
        bottomGP.setHgap(10);
        Button saveBtn = new Button("Save");

        if (isModify) {
            Product currentProduct = inventory.lookupProduct(Integer.valueOf(productIDString));
            nameField.setText(String.valueOf(currentProduct.getName()));
            invField.setText(String.valueOf(currentProduct.getStock()));
            priceField.setText(String.valueOf(currentProduct.getPrice()));
            minInv.setText(String.valueOf(currentProduct.getMin()));
            maxInv.setText(String.valueOf(currentProduct.getMax()));
        }

        saveBtn.setOnAction((ActionEvent e) -> {

            int errors = 0;
            for (TextField field : textFields) {
                minError.setVisible(false);
                removeTextFieldError(field);
                if (field.getText().isEmpty()) {
                    addTextFieldError(field);
                    errors += 1;
                }
            }

            int productIDNum;
            String productName;
            int invAmt;
            Double productPrice;
            int minInvAmt;
            int maxInvAmt;

            if (errors == 0) {
                try {
                    if (isModify) {
                        productIDNum = Integer.valueOf(productIDString);
                    } else {
                        productIDNum = inventory.getAllProducts().size() + 1;
                    }
                    productName = nameField.getText().trim();
                    invAmt = Integer.parseInt(invField.getText().trim());
                    productPrice = Double.parseDouble(priceField.getText().trim());
                    minInvAmt = Integer.parseInt(minInv.getText().trim());
                    maxInvAmt = Integer.parseInt(maxInv.getText().trim());
                    if (minInvAmt > maxInvAmt) {
                        addTextFieldError(minInv);
                        minError.setVisible(true);
                        throw new Exception();
                    }
                    Product product = new Product(
                            productIDNum, productName, productPrice, invAmt, minInvAmt, maxInvAmt
                    );
                    Stage stage = (Stage) saveBtn.getScene().getWindow();
                    for (Part part : associatedPartInventory) {
                        product.addAssociatedPart(part);
                    }
                    if (isModify) {
                        inventory.updateProduct(productIDNum, product);
                    } else {
                        inventory.addProduct(product);
                    }
                    associatedPartInventory.clear();
                    productBottomTV.getItems().clear();
                    stage.close();
                }
                catch (Exception newProductException) {
                    System.out.println("Error adding new part");
                    newProductException.printStackTrace();
                }
            }


        });


        bottomGP.add(saveBtn, 2, 6);
        Button cancelBtn = new Button("Cancel");
        bottomGP.add(cancelBtn,3,6);
        cancelBtn.setOnAction((ActionEvent e) -> {
            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.get() == yesButton) {
                Stage stage = (Stage) cancelBtn.getScene().getWindow();
                stage.close();
            }
        });

        // Add children to root
        root.setLeft(leftHandGP);
        root.setRight(rightHandPane);
        root.setBottom(bottomGP);

        //leftHandGP.setGridLinesVisible(true);
        // Set the Stage
        Scene scene = new Scene(root);
        scene.getStylesheets().add("GUI/productspane/addmodifyproduct/AddModifyProduct.css");
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

}

