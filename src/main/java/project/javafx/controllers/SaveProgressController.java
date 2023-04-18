package project.javafx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.functions.SaveFunctions;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.scene.layout.GridPane.setHalignment;
import static project.javafx.controllers.GameMenuController.gameMenuScene;

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
        SaveFunctions.saveProgress("test");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> saveFiles = SaveFunctions.returnFormattedSaveFiles();

        saveFiles.forEach(saveFile -> {
            Text saveFileText = new Text(saveFile);
            saveFileText.getStyleClass().add("menuItemText");

            saveFileText.onMouseReleasedProperty().set(mouseEvent -> {
                System.out.println(saveFile);
            });

            saveProgressGrid.add(saveFileText, 0, saveFiles.indexOf(saveFile) + 1);
            setHalignment(saveFileText, HPos.CENTER);
        });
    }

}
