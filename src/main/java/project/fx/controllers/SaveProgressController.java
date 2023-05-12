package project.fx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import project.functions.SaveFunctions;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import static project.fx.functions.JavaFxFunctions.createPopup;
import static project.fx.functions.JavaFxFunctions.returnSaveInfoGridPane;
import static project.fx.controllers.GameMenuController.gameMenuScene;

public class SaveProgressController implements Initializable {

    @FXML
    private GridPane saveProgressGrid;

    @FXML
    void onKeyPressed(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ESCAPE)) {
            backOnClick(new javafx.event.ActionEvent(event.getSource(), event.getTarget()));
        }
    }

    @FXML
    void backOnClick(ActionEvent event) {
        ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());
        gameMenuScene(actionEvent);
    }

    @FXML
    void newSaveOnClick(ActionEvent event) {
        SaveFunctions.saveProgress();
        displaySaveFilesInfo();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displaySaveFilesInfo();
    }

    public void displaySaveFilesInfo() {
        saveProgressGrid.getChildren().clear();

        List<String> saveFiles = SaveFunctions.returnFormattedSaveFiles();

        AtomicInteger index = new AtomicInteger();

        saveFiles.forEach(filename -> {
            GridPane saveInfoGridPane = returnSaveInfoGridPane(filename);

            saveInfoGridPane.onMouseReleasedProperty().set(event -> {
                ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());
                Optional<ButtonType> option = createPopup(actionEvent, Alert.AlertType.CONFIRMATION,"Are you sure you want to overwrite this save file?");

                if(option.get() == ButtonType.OK) {
                    String oldFilenameCompressed = saveInfoGridPane.getId();
                    SaveFunctions.saveProgress(oldFilenameCompressed);
                    displaySaveFilesInfo();
                }



            });

            saveProgressGrid.add(saveInfoGridPane, 0, index.get());
            index.getAndIncrement();
        });
    }

}
