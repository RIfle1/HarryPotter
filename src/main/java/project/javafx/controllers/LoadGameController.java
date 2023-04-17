package project.javafx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.functions.SaveFunctions;
import project.javafx.GuiMain;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.scene.layout.GridPane.setHalignment;
import static project.javafx.JavaFxFunctions.sendToScene;
import static project.javafx.controllers.GameMenuController.gameMenuScene;
import static project.javafx.controllers.MainMenuController.mainMenuScene;

public class LoadGameController implements Initializable {


    @FXML
    private GridPane loadGameGrid;

    @FXML
    void onKeyPressed(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ESCAPE)) {
            backOnClick(new ActionEvent(event.getSource(), event.getTarget()));
        }
    }

    @FXML
    void backOnClick(ActionEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        mainMenuScene(stage);
    }

    public static void loadGameScene(MouseEvent event) {
        FXMLLoader FXMLLoader = new FXMLLoader(GuiMain.class.getResource("LoadGame.fxml"));
        ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());
        sendToScene(actionEvent, FXMLLoader);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> saveFiles = SaveFunctions.returnFormattedSaveFiles("saves");

        saveFiles.forEach(saveFile -> {
            Text saveFileText = new Text(saveFile);
            saveFileText.getStyleClass().add("menuItemText");

            saveFileText.onMouseReleasedProperty().set(mouseEvent -> {
                SaveFunctions.loadProgress(saveFile);
                ActionEvent actionEvent = new ActionEvent(mouseEvent.getSource(), mouseEvent.getTarget());
                gameMenuScene(actionEvent);
            });

            loadGameGrid.add(saveFileText, 0, saveFiles.indexOf(saveFile) + 1);
            setHalignment(saveFileText, HPos.CENTER);
        });
    }
}
