package project.javafx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.enums.Level;
import project.javafx.GuiMain;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static project.javafx.GuiMain.createScene;

public class ChooseLevelController implements Initializable {
    @FXML
    private GridPane chooseLevelGrid;

    public static void chooseLevelScene(MouseEvent event) {
        FXMLLoader FXMLLoader = new FXMLLoader(GuiMain.class.getResource("ChooseLevel.fxml"));
        createScene(event, FXMLLoader, "generalStyles.css");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Level.returnAllUnlockedLevelsList().forEach(level -> {
            Text levelText = new Text(level.toString());
            levelText.getStyleClass().add(".itemText");
            chooseLevelGrid.add(levelText, 0, level.ordinal() + 1);
        });
    }
}
