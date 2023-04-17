package project.javafx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.javafx.GuiMain;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

import static project.javafx.GuiMain.createScene;
import static project.javafx.controllers.ChooseLevelController.chooseLevelScene;

public class GameMenuController {

    @FXML
    private Text continueGameT;

    @FXML
    private Text continueGameT1;

    @FXML
    private Text loadGameT;

    @FXML
    private Text loadGameT1;

    @FXML
    private Text newGameT;

    @FXML
    private Text newGameT1;

    @FXML
    void checkAvailableSpellsOnClicked(MouseEvent event) {

    }

    @FXML
    void checkStatsOnClicked(MouseEvent event) {

    }

    @FXML
    void chooseLevelOnClicked(MouseEvent event) {
        chooseLevelScene(event);
    }

    @FXML
    void loadGameOnClicked(MouseEvent event) {

    }

    @FXML
    void saveGameOnClicked(MouseEvent event) {

    }

    @FXML
    void upgradeSpecsOnClicked(MouseEvent event) {

    }


    public static void gameMenuScene(ActionEvent event) {
        FXMLLoader FXMLLoader = new FXMLLoader(GuiMain.class.getResource("GameMenu.fxml"));
        createScene(event, FXMLLoader, "generalStyles.css");
    }

}
