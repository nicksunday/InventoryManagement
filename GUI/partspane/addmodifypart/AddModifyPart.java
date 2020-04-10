package GUI.partspane.addmodifypart;

import Application.inventory.Inventory;
import Application.inventory.part.Part;
import Application.inventory.part.inhouse.InHouse;
import Application.inventory.part.outsourced.Outsourced;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jdk.jfr.events.ExceptionThrownEvent;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class AddModifyPart {
    Text title = new Text("Add Part");
    Boolean isModify = false;
    String partIDString = "Auto Gen - Disabled";

    public void setIsModify() {
        this.isModify = true;
        if (isModify) {
            this.title = new Text("Modify Part");
        }
    }

    public void setPartIDString(int newNum) {
        this.partIDString = String.valueOf(newNum);
    }

    public static void addTextFieldError(TextField tf) {
        tf.getStyleClass().add("error");
    }
    public static void removeTextFieldError(TextField tf) {
        tf.getStyleClass().remove("error");
    }


    public void main(Inventory inventory) {

        GridPane root = new GridPane();
        root.setHgap(10);
        root.setVgap(15);
        root.setPadding(new Insets(15));

        // First Row
        title.setFill(Color.BLACK);
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

        ToggleGroup toggleGroup = new ToggleGroup();

        RadioButton inHouseRB = new RadioButton("In-House");
        RadioButton outSourcedRB = new RadioButton("Outsourced");

        inHouseRB.setToggleGroup(toggleGroup);
        inHouseRB.setSelected(true);
        outSourcedRB.setToggleGroup(toggleGroup);

        root.add(title, 0,0);
        GridPane.setHalignment(inHouseRB, HPos.CENTER);
        root.add(inHouseRB, 1,0);
        GridPane.setHalignment(outSourcedRB, HPos.CENTER);
        root.add(outSourcedRB,2,0);

        // Second Row
        Text partID = new Text("ID");
        partID.setFont(Font.font("Verdana", FontWeight.MEDIUM, 12));
        TextField idField = new TextField(partIDString);
        idField.setDisable(true);
        idField.setStyle("-fx-background-color: lightgrey");

        root.add(partID,0,1);
        root.add(idField,1,1,2,1);

        // Third Row
        Text name = new Text("Name");
        name.setFont(Font.font("Verdana", FontWeight.MEDIUM, 12));
        TextField nameField = new TextField();
        nameField.setPromptText("Part Name");

        root.add(name,0,2);
        root.add(nameField,1,2,2,1);

        // Fourth Row
        Text invLvl = new Text("Inv");
        invLvl.setFont(Font.font("Verdana", FontWeight.MEDIUM, 12));
        TextField invField = new TextField();
        invField.setPromptText("Inv");

        root.add(invLvl,0,3);
        root.add(invField,1,3,2,1);

        // Fifth Row
        Text price = new Text("Price/Cost");
        price.setFont(Font.font("Verdana", FontWeight.MEDIUM, 12));
        TextField priceField = new TextField();
        priceField.setPromptText("Price/Cost");

        root.add(price, 0, 4);
        root.add(priceField,1,4,2,1);

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

        root.add(max, 0, 5);
        root.add(maxInv,1,5,1,1);
        GridPane.setHalignment(min, HPos.CENTER);
        root.add(min,2,5);
        root.add(minInv,3,5,1,1);
        root.add(minError,3,6,1,1);

        // Seventh row - In House
        Text machineID = new Text("Machine ID");
        machineID.setFont(Font.font("Verdana", FontWeight.MEDIUM, 12));
        TextField machineIDField = new TextField();
        machineIDField.setPromptText("Mach ID");

        // Seventh row - Outsourced
        Text compName = new Text("Company Name");
        compName.setFont(Font.font("Verdana", FontWeight.MEDIUM, 12));
        TextField compNameField = new TextField();
        compNameField.setPromptText("Comp Nm");

        // List of text fields
        List<TextField> textFields = new ArrayList<>();
        textFields.add(nameField);
        textFields.add(invField);
        textFields.add(priceField);
        textFields.add(minInv);
        textFields.add(maxInv);
        textFields.add(machineIDField);

        // Seventh Row logic
        root.add(machineID, 0, 6);
        root.add(machineIDField,1,6);

        inHouseRB.setOnAction((ActionEvent e) -> {
            root.getChildren().removeAll(compName, compNameField, machineID, machineIDField);
            textFields.remove(compNameField);

            textFields.add(machineIDField);
            root.add(machineID, 0, 6);
            root.add(machineIDField,1,6);
        });

        outSourcedRB.setOnAction((ActionEvent e) -> {
            root.getChildren().removeAll(compName, compNameField, machineID, machineIDField);
            textFields.remove(machineIDField);

            textFields.add(compNameField);
            root.add(compName,0,6);
            root.add(compNameField,1,6);
        });

        if (isModify) {
            try {
                InHouse part = (InHouse) inventory.lookupPart(Integer.valueOf(partIDString));
                nameField.setText(String.valueOf(part.getName()));
                invField.setText(String.valueOf(part.getStock()));
                priceField.setText(String.valueOf(part.getPrice()));
                minInv.setText(String.valueOf(part.getMin()));
                maxInv.setText(String.valueOf(part.getMax()));
                machineIDField.setText(String.valueOf(part.getMachineId()));
                inHouseRB.setSelected(true);
            } catch (Exception modifyError) {
                Outsourced part = (Outsourced) inventory.lookupPart(Integer.valueOf(partIDString));
                nameField.setText(String.valueOf(part.getName()));
                invField.setText(String.valueOf(part.getStock()));
                priceField.setText(String.valueOf(part.getPrice()));
                minInv.setText(String.valueOf(part.getMin()));
                maxInv.setText(String.valueOf(part.getMax()));
                compNameField.setText(String.valueOf(part.getCompanyName()));
                // Handle Radio button/field change
                outSourcedRB.setSelected(true);
                textFields.remove(machineIDField);
                textFields.add(compNameField);
                root.getChildren().removeAll(compName, compNameField, machineID, machineIDField);
                root.add(compName,0,6);
                root.add(compNameField,1,6);
            }
        }
        // Eighth Row

        Button saveBtn = new Button("Save");
        GridPane.setHalignment(saveBtn, HPos.RIGHT);
        root.add(saveBtn, 2, 7);

        // Confirmation button
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setContentText("Are you sure?");
        confirmation.setHeaderText(null);
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType cancelConfirm = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmation.getButtonTypes().setAll(yesButton, cancelConfirm);

        // Save Button Logic
        saveBtn.setOnAction((ActionEvent e) -> {

            int errors = 0;
            for (TextField field : textFields) {
                minError.setVisible(false);
                removeTextFieldError(field);
                if (field.getText().trim().isEmpty() && !field.isDisabled()) {
                    addTextFieldError(field);
                    errors += 1;
                }
            }

            int partId;
            String partName;
            int invAmt;
            double partPrice;
            int minInvAmt = 0;
            int maxInvAmt = 0;
            int machineIDNum;
            String companyName;
            if (errors == 0) {
                try {
                    if (isModify) {
                        partId = Integer.valueOf(partIDString);
                    } else {
                        partId = inventory.getAllParts().size() + 1;
                    }
                    partName = nameField.getText().trim();
                    partPrice = Double.parseDouble(priceField.getText().trim());
                    invAmt = Integer.parseInt(invField.getText().trim());
                    minInvAmt = Integer.parseInt(minInv.getText().trim());
                    maxInvAmt = Integer.parseInt(maxInv.getText().trim());
                    if (minInvAmt > maxInvAmt) {
                        addTextFieldError(minInv);
                        minError.setVisible(true);
                        throw new Exception();
                    }
                    if (inHouseRB.isSelected()) {
                        machineIDNum = Integer.parseInt(machineIDField.getText().trim());
                        InHouse part = new InHouse(
                                partId, partName, partPrice, invAmt, minInvAmt, maxInvAmt, machineIDNum
                        );
                        if (isModify) {
                            inventory.updatePart(partId, part);
                        } else {
                            inventory.addPart(part);
                        }
                        for (TextField field : textFields) {
                            field.clear();
                        }
                    }
                    if (outSourcedRB.isSelected()) {
                        companyName = compNameField.getText().trim();
                        Outsourced part = new Outsourced(
                                partId, partName, partPrice, invAmt, minInvAmt, maxInvAmt, companyName
                        );
                        if (isModify) {
                            inventory.updatePart(partId, part);
                        } else {
                            inventory.addPart(part);
                        }
                        for (TextField field : textFields) {
                            field.clear();
                        }
                    }
                    Stage stage = (Stage) saveBtn.getScene().getWindow();
                    stage.close();

                } catch (Exception newPartException) {
                   System.out.println("Error adding new part");
                   newPartException.printStackTrace();
                }
            }
        });

        Button cancelBtn = new Button("Cancel");
        GridPane.setHalignment(cancelBtn, HPos.LEFT);
        root.add(cancelBtn,3,7);
        cancelBtn.setOnAction((ActionEvent e) -> {
            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.get() == yesButton) {
                Stage stage = (Stage) cancelBtn.getScene().getWindow();
                stage.close();
            }
        });

        // SET GRID LINES ON FOR TESTING
        //root.setGridLinesVisible(true);

        // Set the Stage
        Scene scene = new Scene(root);
        scene.getStylesheets().add("GUI/partspane/addmodifypart/AddModifyPart.css");
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }
}
