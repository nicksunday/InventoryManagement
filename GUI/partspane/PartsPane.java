package GUI.partspane;

import Application.inventory.Inventory;
import Application.inventory.part.Part;
import GUI.partspane.addmodifypart.AddModifyPart;
import GUI.partspane.partstable.PartsTable;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
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

public class PartsPane extends BorderPane {

    public BorderPane newPane(Inventory inventory) {
        //
        // Root pane to return
        //
        BorderPane partsPane = new BorderPane();
        partsPane.setPadding(new Insets(20));

        //
        // Center section layout
        //
        PartsTable pt = new PartsTable();
        TableView<Part> newTV = new TableView<>();
        TableView<Part> partsTV = pt.formatTable(newTV);

        partsTV.setItems(inventory.getAllParts());

        // Top section layout
        //
        Text partsTitle = new Text("Parts");
        partsTitle.setFill(Color.BLACK);
        partsTitle.setFont(Font.font("Verdana", FontWeight.MEDIUM, 15));

        FlowPane partsFlow = new FlowPane();
        partsFlow.setHgap(10);
        Button partsSearchBtn = new Button("Search");
        TextField partsSearchText = new TextField();

        partsSearchBtn.setOnAction((ActionEvent e) -> {
            if (partsSearchText.getText().isEmpty()) {
                partsTV.setItems(inventory.getAllParts());
            } else {
                if (partsSearchText.getText().chars().allMatch(Character::isDigit)) {
                    int partsSearchVar = Integer.valueOf(partsSearchText.getText().trim());
                    Part part = inventory.lookupPart(partsSearchVar);
                    partsTV.setItems(inventory.lookupPart(part.getName()));
                } else {
                    String partsSearchVar = partsSearchText.getText();
                    partsTV.setItems(inventory.lookupPart(partsSearchVar));
                }
            }
        });

        partsFlow.getChildren().addAll(partsSearchBtn,partsSearchText);

        GridPane partsTopGP = new GridPane();
        partsTopGP.setHgap(10);
        partsTopGP.setVgap(15);
        partsTopGP.setPadding(new Insets(10));

        ColumnConstraints col1Constraints = new ColumnConstraints();
        col1Constraints.setPercentWidth(15);
        ColumnConstraints col2Constraints = new ColumnConstraints();
        col2Constraints.setPercentWidth(30);
        ColumnConstraints col3Constraints = new ColumnConstraints();
        col3Constraints.setPercentWidth(15);
        partsTopGP.getColumnConstraints().addAll(col1Constraints, col2Constraints, col3Constraints);

        GridPane.setHalignment(partsTitle, HPos.LEFT);
        partsTopGP.add(partsTitle, 0,0,1,1);
        GridPane.setHalignment(partsFlow, HPos.RIGHT);
        partsTopGP.add(partsFlow, 2,0,2,1);

        //
        // Bottom section layout
        //
        GridPane partsBottomGrid = new GridPane();
        partsBottomGrid.setHgap(20);
        partsBottomGrid.setPadding(new Insets(5,20,20,5));
        partsBottomGrid.setAlignment(Pos.BOTTOM_RIGHT);

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setContentText("Are you sure?");
        confirmation.setHeaderText(null);
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType cancelConfirm = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmation.getButtonTypes().setAll(yesButton, cancelConfirm);

        Button addPartsBtn = new Button("Add");
        addPartsBtn.setOnAction((ActionEvent e) -> {
            AddModifyPart AP = new AddModifyPart();
            AP.main(inventory);
        });

        Button modifyPartsBtn = new Button("Modify");
        modifyPartsBtn.setOnAction((ActionEvent e) -> {
            try {
                int partId = partsTV.getSelectionModel().getSelectedItem().getId();
                AddModifyPart AP = new AddModifyPart();
                AP.setIsModify();
                AP.setPartIDString(partId);
                AP.main(inventory);
                partsTV.getSelectionModel().clearSelection();
            } catch (NullPointerException nullPointer) {}
        });
        Button deletePartsBtn = new Button("Delete");
        deletePartsBtn.setOnAction((ActionEvent e) -> {
            Part part = partsTV.getSelectionModel().getSelectedItem();
            if (part != null) {
                Optional<ButtonType> result = confirmation.showAndWait();
                if (result.get() == yesButton) {
                    inventory.deletePart(part);
                }
            }
        });

        partsBottomGrid.add(addPartsBtn,0,0);
        partsBottomGrid.add(modifyPartsBtn,1,0);
        partsBottomGrid.add(deletePartsBtn,2,0);

        // Assemble parts pane
        partsPane.setTop(partsTopGP);
        partsPane.setCenter(partsTV);
        partsPane.setBottom(partsBottomGrid);
        partsPane.getStylesheets().add("GUI/partspane/PartsPane.css");
        partsPane.setStyle("-fx-border-color: black");

        return partsPane;
    }
}
