package project.fx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.abstractClasses.AbstractCharacter;
import project.classes.Level;

import java.net.URL;
import java.util.ResourceBundle;

import static project.classes.Enemy.generateEnemies;
import static project.functions.GeneralFunctions.checkPositiveInt;
import static project.fx.controllers.GameMenuController.gameMenuScene;
import static project.fx.controllers.GameSceneController.gameScene;
import static project.fx.functions.JavaFxFunctions.returnFXMLURL;
import static project.fx.functions.JavaFxFunctions.sendToScene;

public class BattleArenaMenuController implements Initializable {

    @FXML
    private TextField enemyAmountT;

    @FXML
    private TextField enemyMaxLevelT;

    @FXML
    private TextField enemyMinLevelT;

    @FXML
    private Text ccErrorT;

    @FXML
    void onKeyPressed(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ESCAPE)) {
            backOnClick(new ActionEvent(event.getSource(), event.getTarget()));
        }
    }

    public static void battleArenaScene(ActionEvent event) {
        FXMLLoader gameSceneFxmlLoader = new FXMLLoader(returnFXMLURL("BattleArenaMenu.fxml"));
        sendToScene(event, gameSceneFxmlLoader);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        enemyMinLevelT.setText("1");
        enemyMaxLevelT.setText("10");
        enemyAmountT.setText("1");
    }

    @FXML
    void backOnClick(ActionEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        gameMenuScene(stage);
    }

    @FXML
    void fightOnClick(ActionEvent event) {
        int maxEnemyAmount = 14;

        String enemyMinLevel = enemyMinLevelT.getText();
        String enemyMaxLevel = enemyMaxLevelT.getText();
        String enemyAmount = enemyAmountT.getText();

        if(checkPositiveInt(enemyMinLevel) && checkPositiveInt(enemyMaxLevel) && checkPositiveInt(enemyAmount)) {
            int enemyAmountInt = Integer.parseInt(enemyAmount);
            int enemyMinLevelInt = Integer.parseInt(enemyMinLevel);
            int enemyMaxLevelInt = Integer.parseInt(enemyMaxLevel);

            ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());

            if(enemyAmountInt > maxEnemyAmount) {
                ccErrorT.setText("Max enemy amount is " + maxEnemyAmount);
                ccErrorT.setVisible(true);
            }
            else if(enemyMaxLevelInt > AbstractCharacter.maxLevel) {
                ccErrorT.setText("Enemy max level is " + AbstractCharacter.maxLevel);
                ccErrorT.setVisible(true);
            }
            else if(enemyMinLevelInt < 1) {
                ccErrorT.setText("Enemy min level is 1");
                ccErrorT.setVisible(true);
            }
            else if(enemyMinLevelInt > 10) {
                ccErrorT.setText("Enemy min level can't be greater than 10");
                ccErrorT.setVisible(true);
            }
            else {
                gameScene(actionEvent, enemyMinLevelInt, enemyMaxLevelInt,
                        enemyAmountInt, Level.Battle_Arena);

            }

        }
        else {
            ccErrorT.setVisible(true);
        }


    }


}
