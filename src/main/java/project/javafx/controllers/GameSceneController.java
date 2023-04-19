package project.javafx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import project.classes.Enemy;
import project.classes.Spell;
import project.enums.EnemyName;
import project.javafx.GuiMain;

import java.net.URISyntaxException;
import java.net.URL;
import java.text.CollationElementIterator;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import static project.classes.Enemy.*;
import static project.classes.Wizard.wizard;
import static project.javafx.JavaFxFunctions.*;
import static project.javafx.controllers.GameMenuController.gameMenuScene;

public class GameSceneController implements Initializable {

    @FXML
    private Circle attackChanceCircle;

    @FXML
    private AnchorPane combatAnchorPane;

    @FXML
    private TextFlow consoleTf;

    @FXML
    private GridPane enemyAvailableAttacksGrid;

    @FXML
    private GridPane enemyCombatGridPane;

    @FXML
    private Text esDefenseT;

    @FXML
    private ProgressBar esHealthPg;

    @FXML
    private ImageView esIconIm;

    @FXML
    private Text esLevelT;

    @FXML
    private Text esNameT;

    @FXML
    private GridPane gameMainGrid;

    @FXML
    private TextFlow objectiveTf;

    @FXML
    private GridPane playerCombatGridPane;

    @FXML
    private GridPane playerSpellsGrid;

    @FXML
    private Text psDefenseT;

    @FXML
    private ProgressBar psHealthPg;

    @FXML
    private ImageView psIconIm;

    @FXML
    private Text psLevelT;

    @FXML
    private Text psNameT;

    @FXML
    void attackOnClick(ActionEvent event) {

    }

    @FXML
    void dodgeOnClick(ActionEvent event) {

    }

    @FXML
    void onKeyPressed(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ESCAPE)) {
            ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());

            Optional<ButtonType> result = createPopup(actionEvent, Alert.AlertType.CONFIRMATION, "Are you sure you want to quit the battle? All progress will be lost.");

            if(result.get() == ButtonType.OK) {
                gameMenuScene(actionEvent);
            }

        }
    }

    @FXML
    void parryOnClick(ActionEvent event) {

    }

    public static void gameScene(ActionEvent event) {
        FXMLLoader gameSceneFxmlLoader = new FXMLLoader(GuiMain.class.getResource("GameScene.fxml"));
        sendToScene(event, gameSceneFxmlLoader);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayPlayerStats();
        generateEnemies(1, 1, 1, EnemyName.GOBLIN);
        displayEnemyStats(enemiesHashMap.get(enemiesKeyList.get(0)));
    }

    public void displayPlayerStats() {
        psHealthPg.setProgress(wizard.getHealthPoints() / wizard.getMaxHealthPoints());
        psHealthPg.getStyleClass().add("healthBar");

        psDefenseT.setText(String.valueOf((int) wizard.getDefensePoints()));
        psLevelT.setText(String.valueOf((int) wizard.getLevel()));
    }

    public void displayEnemyStats(Enemy enemy) {
        esHealthPg.setProgress(enemy.getHealthPoints() / enemy.getMaxHealthPoints());
        esHealthPg.getStyleClass().add("healthBar");

        esDefenseT.setText(String.valueOf((int) enemy.getDefensePoints()));
        esLevelT.setText(String.valueOf((int) enemy.getLevel()));

        HashMap<String, Spell> enemySpellsHashMap = enemy.getSpellsHashMap();


        if (enemySpellsHashMap.size() > 0) {
            AtomicInteger index = new AtomicInteger();

            enemySpellsHashMap.forEach((spellName, spell) -> {
                GridPane enemySpellGridPane = returnEnemySpellGridPane();

                Text attackText = new Text(spellName);
                ImageView spellIcon = returnSpellImage(spellName, 30, 30);

                attackText.getStyleClass().add("availableAttacksText");

                enemySpellGridPane.add(spellIcon, 0, 0);
                enemySpellGridPane.add(attackText, 1, 0);

                enemyAvailableAttacksGrid.add(enemySpellGridPane, index.get(), 0);
                index.getAndIncrement();
            });
        }
        else {
            GridPane enemySpellGridPane = returnEnemySpellGridPane();

            String spellName = "Melee Attack";

            Text attackText = new Text(spellName);
            ImageView spellIcon = returnSpellImage(spellName, 30, 30);

            enemySpellGridPane.add(spellIcon, 0, 0);
            enemySpellGridPane.add(attackText, 1, 0);

            attackText.getStyleClass().add("availableAttacksText");
            enemyAvailableAttacksGrid.add(enemySpellGridPane, 0, 0);
        }
    }

    public static GridPane returnEnemySpellGridPane() {
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();

        column1.setHalignment(HPos.CENTER);
        column1.setPercentWidth(20);
        column2.setHalignment(HPos.CENTER);
        column2.setPercentWidth(80);

        GridPane enemySpellGridPane = new GridPane();
        enemySpellGridPane.setVgap(5);
        enemySpellGridPane.setPadding(new javafx.geometry.Insets(30, 10, 10, 10));
        enemySpellGridPane.getColumnConstraints().addAll(column1, column2);

        return enemySpellGridPane;
    }
}
