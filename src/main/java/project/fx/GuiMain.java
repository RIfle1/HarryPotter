package project.fx;

import javafx.application.Application;
import javafx.stage.Stage;

import static project.fx.controllers.MainMenuController.mainMenuScene;

public class GuiMain extends Application {
    @Override
    public void start(Stage stage){
        mainMenuScene(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}