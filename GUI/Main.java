package GUI;

import Application.inventory.Inventory;
import GUI.partspane.PartsPane;
import GUI.productspane.ProductsPane;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Optional;

public class Main extends Application {

    Inventory inventory = new Inventory();

    @Override
    public void start(Stage primaryStage) throws Exception{

        /*
        ----- Setting up root pane -----
        */
        Text title = new Text("Inventory Management System");
        title.setFill(Color.CORNFLOWERBLUE);
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(30,15,30,15));

        /*
        ----- Organize sub-panes in HBox -----
        */
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(15));
        hBox.setSpacing(15);

        /*
        ----- Use Children objects to create sub panes -----
        */
        BorderPane partsPane = new PartsPane().newPane(inventory);

        BorderPane productsPane = new ProductsPane().newPane(inventory);

        /*
        ---------- Exit Button ----------
        */
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setContentText("Are you sure?");
        confirmation.setHeaderText(null);
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType cancelConfirm = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmation.getButtonTypes().setAll(yesButton, cancelConfirm);

        HBox exitHbox = new HBox();
        exitHbox.setAlignment(Pos.BOTTOM_RIGHT);
        Button exitBtn = new Button("Exit");
        exitBtn.setOnAction((ActionEvent e) -> {
            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.get() == yesButton) {
                Platform.exit();
            }
        });
        exitHbox.getChildren().add(exitBtn);

        /*
        ----- Add children to root -----
        */
        root.setTop(title);
        HBox.setHgrow(partsPane, Priority.ALWAYS);
        HBox.setHgrow(productsPane, Priority.ALWAYS);
        hBox.getChildren().addAll(partsPane,productsPane);
        root.setCenter(hBox);
        root.setBottom(exitHbox);

        Scene scene = new Scene(root);
        scene.getStylesheets().add("GUI/Main.css");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
