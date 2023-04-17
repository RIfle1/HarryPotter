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

public class GuiMain extends Application {
    @Override
    public void start(Stage stage){
        stage.setTitle("Harry Potter: The Text RPG");
        mainScene(stage);
    }

    public static void mainScene(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(GuiMain.class.getResource("MainMenu.fxml")));
            Scene mainMenuScene = new Scene(root);
            stage.setScene(mainMenuScene);
            String generalStyles = Objects.requireNonNull(GuiMain.class.getResource("generalStyles.css")).toExternalForm();
            mainMenuScene.getStylesheets().add(generalStyles);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createScene(ActionEvent event, FXMLLoader FXMLLoader, String cssStylesheet) {
        createSceneSub(FXMLLoader, cssStylesheet, (Node) event.getSource());
    }

    public static void createScene(MouseEvent event, FXMLLoader FXMLLoader, String cssStylesheet) {
        createSceneSub(FXMLLoader, cssStylesheet, (Node) event.getSource());
    }

    public static void createSceneSub(FXMLLoader FXMLLoader, String cssStylesheet, Node source) {
        Scene scene;

        try {
            scene = new Scene(FXMLLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = (Stage) source.getScene().getWindow();
        stage.setScene(scene);
        String generalStyles = Objects.requireNonNull(GuiMain.class.getResource(cssStylesheet)).toExternalForm();
        scene.getStylesheets().add(generalStyles);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}