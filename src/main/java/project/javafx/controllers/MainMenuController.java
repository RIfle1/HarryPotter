package project.javafx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.javafx.GuiMain;

import java.io.IOException;
import java.util.Objects;

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

    public static void mainMenuScene(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(GuiMain.class.getResource("MainMenu.fxml")));
            Scene mainMenuScene = new Scene(root);
            stage.setScene(mainMenuScene);
            String generalStyles = Objects.requireNonNull(GuiMain.class.getResource("generalStyles.css")).toExternalForm();
            mainMenuScene.getStylesheets().add(generalStyles);
            Image icon = new Image("file:src/main/resources/icons/icon.png");
            stage.getIcons().add(icon);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
