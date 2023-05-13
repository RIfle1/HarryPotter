package project.fx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.fx.GuiLauncherMain;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import static project.functions.SaveFunctions.continueGame;
import static project.fx.controllers.CharacterCreationController.characterCreationScene;
import static project.fx.controllers.LoadGameController.loadGameScene;
import static project.fx.functions.JavaFxFunctions.returnFXMLURL;
import static project.fx.functions.JavaFxFunctions.returnImagePath;

public class MainMenuController {
    @FXML
    private Text loadGameT;
    @FXML
    private Text continueGameT;
    @FXML
    private Text newGameT;

    @FXML
    void continueGameOnClicked(MouseEvent event) {
        continueGame(event);
    }

    @FXML
    void loadGameOnClicked(MouseEvent event) {
        loadGameScene(event);
    }
    @FXML
    void newGameOnClicked(MouseEvent actionEvent) {
        characterCreationScene(actionEvent);
    }

    public static void mainMenuScene(Stage stage) {
        try {
            Parent root = FXMLLoader.load(returnFXMLURL("MainMenu.fxml"));
            Scene mainMenuScene = new Scene(root);

            stage.setScene(mainMenuScene);

            Image icon = new Image(returnImagePath("icon"));
            stage.getIcons().add(icon);
            stage.setTitle("Harry Potter: THE GUI");

            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
