package project.javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.classes.Wizard;
import project.functions.SaveFunctions;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static project.enums.EnumMethods.returnFormattedEnum;
import static project.functions.GeneralFunctions.returnFileDate;
import static project.functions.SaveFunctions.createWizardInstance;
import static project.javafx.controllers.GameMenuController.gameMenuScene;

public class JavaFxFunctions {

    public static void sendToScene(ActionEvent event, FXMLLoader FXMLLoader) {
        Scene scene;

        try {
            scene = new Scene(FXMLLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    public static void createPopup(ActionEvent event, Alert.AlertType alertType, String popUpMsg) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Alert alert = new Alert(alertType, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);

        alert.getDialogPane().setContentText(popUpMsg);
        alert.showAndWait();
    }

    public static GridPane returnSaveInfoGridPane(String filename) {
        GridPane saveInfoGridPane = new GridPane();
        saveInfoGridPane.setAlignment(Pos.CENTER);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHalignment(HPos.LEFT);
        column1.setPercentWidth(100);
        saveInfoGridPane.getColumnConstraints().add(column1);


        Wizard saveWizard = Wizard.builder().build();
        createWizardInstance(filename, saveWizard);

        String fileDate = returnFileDate("saves", filename);
        String wizardName = saveWizard.getName();
        String wizardLevel = String.valueOf((int) saveWizard.getLevel());
        String wizardHouse = returnFormattedEnum(saveWizard.getHouseName());

        Text wizardNameText = new Text(wizardName);
        Text wizardLevelText = new Text("Level " + wizardLevel);
        Text wizardHouseText = new Text("House of " + wizardHouse);
        Text fileDateText = new Text(fileDate);


        saveInfoGridPane.add(wizardNameText, 0, 0);
        saveInfoGridPane.add(wizardLevelText, 0, 1);
        saveInfoGridPane.add(wizardHouseText, 0, 2);
        saveInfoGridPane.add(fileDateText, 0, 3);

        saveInfoGridPane.getStyleClass().add("saveInfoGridPane");
        saveInfoGridPane.setGridLinesVisible(true);

        return saveInfoGridPane;

    }

}
