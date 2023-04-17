package project.javafx.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import static project.javafx.controllers.CharacterCreationController.characterCreationScene;

public class MainMenuController {
    @FXML
    private Text loadGameT;
    @FXML
    private Text continueGameT;
    @FXML
    private Text newGameT;

    @FXML
    void continueGameOnClicked(MouseEvent event) {

    }

    @FXML
    void loadGameOnClicked(MouseEvent event) {

    }
    @FXML
    void newGameOnClicked(MouseEvent actionEvent) {
        characterCreationScene(actionEvent);
    }

}
