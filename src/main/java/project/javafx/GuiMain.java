package project.javafx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static project.javafx.controllers.MainMenuController.mainMenuScene;

public class GuiMain extends Application {
    @Override
    public void start(Stage stage){
        mainMenuScene(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}