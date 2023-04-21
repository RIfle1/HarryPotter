package project.javafx.controllers;

import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import project.classes.Color;
import project.classes.Enemy;
import project.classes.Potion;
import project.classes.Spell;
import project.enums.EnemyName;
import project.enums.MoveType;
import project.javafx.GuiMain;

import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static project.classes.Enemy.*;
import static project.classes.Wizard.wizard;
import static project.enums.EnumMethods.returnFormattedEnum;
import static project.functions.ConsoleFunctions.returnLineSeparator;
import static project.javafx.controllers.GameMenuController.gameMenuScene;
import static project.javafx.functions.JavaFxFunctions.*;

public class GameSceneController implements Initializable {
    private static final Text consoleTStatic = new Text();
    private static boolean buttonClicked = false;
    @FXML
    private Circle actionCircle;
    @FXML
    private Button attackBtn;
    @FXML
    private Circle baseActionCircle;
    @FXML
    private GridPane buttonsGridPane;
    @FXML
    private AnchorPane combatAnchorPane;
    @FXML
    private Text consoleT;
    @FXML
    private Button dodgeBtn;
    @FXML
    private GridPane enemyAvailableAttacksGrid;
    @FXML
    private GridPane enemyCombatGridPane;
    @FXML
    private Text esDefenseT;
    @FXML
    private ProgressBar esHealthPg;
    @FXML
    private Text esHealthT;
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
    private Button parryBtn;
    @FXML
    private GridPane playerAvailableSpellsGrid;
    @FXML
    private GridPane playerCombatGridPane;
    @FXML
    private GridPane playerPotionsGridPane;
    @FXML
    private Text psDefenseT;
    @FXML
    private ProgressBar psHealthPg;
    @FXML
    private Text psHealthT;
    @FXML
    private ImageView psIconIm;
    @FXML
    private Text psLevelT;
    @FXML
    private Text psNameT;
    @FXML
    private Circle successActionCircle;

    public static void updateConsoleTaStatic(String text) {
        HashMap<String, String> colorsHashMap = Color.returnAllColorsHashMap();

        for (Map.Entry<String, String> entry : colorsHashMap.entrySet()) {

            String color = entry.getKey();
            String colorCode = entry.getValue();

            if (text.contains(colorCode)) {
                text = text.replace(colorCode, "");
            }
        }

        text = text + "\n" + returnLineSeparator(text.length());
        consoleTStatic.setText(text);
    }

    public static void gameScene(ActionEvent event) {
        FXMLLoader gameSceneFxmlLoader = new FXMLLoader(GuiMain.class.getResource("GameScene.fxml"));
        sendToScene(event, gameSceneFxmlLoader);
    }

    private static GridPane returnEnemyAvailableAttacksGridPane(String spellName) {
        GridPane enemySpellGridPane = new GridPane();

        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();

        column1.setHalignment(HPos.CENTER);
        column1.setPercentWidth(20);
        column2.setHalignment(HPos.LEFT);
        column2.setPercentWidth(80);

        enemySpellGridPane.getColumnConstraints().addAll(column1, column2);


        Text attackText = new Text(spellName);
        ImageView spellIcon = returnObjectImageView(spellName, 30, 30);

        attackText.getStyleClass().add("simpleObjectText");

        enemySpellGridPane.add(spellIcon, 0, 0);
        enemySpellGridPane.add(attackText, 1, 0);

        return enemySpellGridPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TESTS TO DELETE AFTERWARDS
        generateEnemies(10, 10, 1, EnemyName.BELLATRIX_LESTRANGE);
        try {
            wizard.setPotionList(Arrays.asList(Potion.highDefensePotion.clone(), Potion.highDamagePotion.clone(), Potion.highDamagePotion.clone(), Potion.highDamagePotion.clone(), Potion.highHealthPotion.clone(), Potion.highHealthPotion.clone(), Potion.highHealthPotion.clone()));
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        disableActionButtons();

        displayPlayerStats();
        displayPlayerSpellsGrid();
        displayPlayerPotionsGridPane();
        displayEnemyStats(enemiesHashMap.get(enemiesKeyList.get(0)));
    }

    @FXML
    void onKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ESCAPE)) {
            ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());

            Optional<ButtonType> result = createPopup(actionEvent, Alert.AlertType.CONFIRMATION, "Are you sure you want to quit the battle? All progress will be lost.");

            if (result.get() == ButtonType.OK) {
                gameMenuScene(actionEvent);
            }

        }
    }

    @FXML
    void usePotionOnClick(ActionEvent event) {
        String potionName = playerPotionsGridPane.getChildren()
                .stream()
                .filter(node -> node.getStyleClass().contains("playerObjectInfoGridPanePressed"))
                .toList().get(0).getId();

        Potion potion = wizard.returnPotion(potionName);

        wizard.drinkPotion(potion);

        updateConsoleTa();
        displayPlayerStats();
        displayPlayerPotionsGridPane();
    }

    private void clearConsoleTa() {
        consoleT.setText("");
    }

    private void updateConsoleTa() {
        String previousText = consoleT.getText();

        if (previousText.length() > 0) {
            previousText = previousText + "\n";
        } else {
            previousText = "";
        }

        consoleT.setText(previousText + consoleTStatic.getText());
    }

    @FXML
    void attackOnClick(ActionEvent event) {
        actionButtonOnClick();
    }

    @FXML
    void dodgeOnClick(ActionEvent event) {
        actionButtonOnClick();
    }

    @FXML
    void parryOnClick(ActionEvent event) {
        actionButtonOnClick();

    }


    void startAction() {
        double baseActionCircleRadius = baseActionCircle.getRadius();
        boolean actionCircleShrink = true;
        double actionCircleSpeed = (20 / Math.exp(wizard.getLevel() * wizard.getDifficulty().getWizardDiffMultiplier()));
        System.out.println("Button Clicked :" + buttonClicked);

        ScaleTransition transition = new ScaleTransition(Duration.seconds(3), actionCircle);

        transition.setFromX(1);
        transition.setToX(3);
        transition.setFromY(1);
        transition.setToY(3);

        transition.setAutoReverse(true);
        transition.setCycleCount(ScaleTransition.INDEFINITE);


        if(buttonClicked) {
            transition.play();
        }
        else {
            transition.stop();
        }


//        while (buttonClicked) {
//            if (actionCircleShrink) {
//                for (double radius = baseActionCircleRadius; radius > 0; radius--) {
//                    if (startActionSub((int) actionCircleSpeed, radius)) break;
//                }
//                actionCircleShrink = false;
//            } else {
//                for (double radius = 0; radius < baseActionCircleRadius; radius++) {
//                    if (startActionSub((int) actionCircleSpeed, radius)) break;
//                }
//                actionCircleShrink = true;
//            }
//        }

    }

    private void animateCircleStart(Circle circle) {
        ScaleTransition transition = new ScaleTransition(Duration.seconds(3), circle);

        transition.setFromX(1);
        transition.setToX(3);
        transition.setFromY(1);
        transition.setToY(3);

        transition.setAutoReverse(true);
        transition.setCycleCount(ScaleTransition.INDEFINITE);
    }

    private void animateCircleStop(Circle circle) {
        circle.
    }

    private boolean startActionSub(int actionCircleSpeed, double radius) {


        try {
            actionCircle.setRadius(radius);
            System.out.println(radius);
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }


        try {
            Thread.sleep(actionCircleSpeed);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        return !buttonClicked;
    }

    void actionButtonOnClick() {
        Thread playerAction = new Thread(this::startAction);

        if (!buttonClicked) {
            buttonClicked = true;
            playerAction.start();
            disableGridPaneButtons(playerAvailableSpellsGrid);

        } else {
            buttonClicked = false;
            playerAction.interrupt();
            enableGridPaneButtons(playerAvailableSpellsGrid);
            deselectAllSubGridPanes(playerAvailableSpellsGrid);
            disableActionButtons();

            System.out.println("Action circle radius: " + actionCircle.getRadius());
            System.out.println("Chance circle radius: " + successActionCircle.getRadius());
        }
    }

    void disableActionButtons(Button button) {
        buttonsGridPane.getChildren().stream()
                .filter(node -> node instanceof Button)
                .filter(node -> node.equals(button))
                .forEach(node -> node.setDisable(true));
    }

    void disableActionButtons() {
        buttonsGridPane.getChildren().stream()
                .filter(node -> node instanceof Button)
                .forEach(node -> node.setDisable(true));
    }

    void enableActionButtons(Button button) {
        buttonsGridPane.getChildren().stream()
                .filter(node -> node instanceof Button)
                .filter(node -> node.equals(button))
                .forEach(node -> node.setDisable(false));
    }

    void enableActionButtons() {
        buttonsGridPane.getChildren().stream()
                .filter(node -> node instanceof Button)
                .forEach(node -> node.setDisable(false));
    }

    void disableGridPaneButtons(GridPane gridpane) {
        gridpane.getChildren().forEach(node -> {
            node.setDisable(true);
        });
    }

    void enableGridPaneButtons(GridPane gridpane) {
        gridpane.getChildren().forEach(node -> {
            node.setDisable(false);
        });
    }


    private void displayPlayerStats() {
        psNameT.setText(wizard.getName());

        psIconIm.setImage(returnObjectImage("hagrid"));

        psHealthPg.setProgress(wizard.getHealthPoints() / wizard.getMaxHealthPoints());
        psHealthPg.getStyleClass().add("healthBar");
        psHealthT.setText((int) wizard.getHealthPoints() + "/" + (int) wizard.getMaxHealthPoints());

        psDefenseT.setText(String.valueOf((int) wizard.getDefensePoints()));
        psLevelT.setText(String.valueOf((int) wizard.getLevel()));
    }

    private void displayPlayerSpellsGrid() {
        List<Spell> playerSpellsHashMap = wizard.returnTypedSpellsList(MoveType.ATTACK);
        int maxColumns = 3;
        AtomicInteger rowIndex = new AtomicInteger();
        AtomicInteger columnIndex = new AtomicInteger();

        playerSpellsHashMap.forEach(spell -> {
            String spellName = returnFormattedEnum(spell.getSpellName());
            GridPane playerSpellInfoGridPane = new GridPane();
            ColumnConstraints column1 = new ColumnConstraints();
            ColumnConstraints column2 = new ColumnConstraints();

            column1.setPercentWidth(20);
            column1.setHalignment(HPos.CENTER);
            column2.setPercentWidth(80);
            column2.setHalignment(HPos.LEFT);

            playerSpellInfoGridPane.getColumnConstraints().addAll(column1, column2);

            Text spellText = new Text(spellName);
            ImageView spellIcon = returnObjectImageView(spellName, 30, 30);
            spellText.getStyleClass().add("simpleObjectText");


            playerSpellInfoGridPane.add(spellIcon, 0, 0);
            playerSpellInfoGridPane.add(spellText, 1, 0);

            playerSpellInfoGridPane.getStyleClass().addAll("playerObjectInfoGridPane");

            playerSpellInfoGridPane.setOnMouseReleased(event -> {
                selectSpell(playerAvailableSpellsGrid, playerSpellInfoGridPane);
                enableActionButtons(attackBtn);
                defineSuccessActionCircle(spell);
            });

            playerAvailableSpellsGrid.add(playerSpellInfoGridPane, columnIndex.get(), rowIndex.get());

            if (columnIndex.get() == maxColumns - 1) {
                columnIndex.set(0);
                rowIndex.getAndIncrement();
            } else {
                columnIndex.getAndIncrement();
            }

        });

        playerAvailableSpellsGrid.setVgap(5);
        playerAvailableSpellsGrid.setHgap(5);
    }

    public void defineSuccessActionCircle(Spell spell) {
        int successActionCircleRadius = (int) (baseActionCircle.getRadius() * spell.getSpellChance());
        successActionCircle.setRadius(successActionCircleRadius);
    }

    private void displayPlayerPotionsGridPane() {
        playerPotionsGridPane.getChildren().clear();

        List<Potion> playerPotionsList = wizard.getPotionList();
        AtomicInteger index = new AtomicInteger();

        playerPotionsList.forEach(potion -> {
            String potionName = potion.getItemName();

            GridPane playerPotionInfoGridPane = new GridPane();

            ColumnConstraints column1 = new ColumnConstraints();
            ColumnConstraints column2 = new ColumnConstraints();

            column1.setPercentWidth(20);
            column1.setHalignment(HPos.CENTER);

            column2.setPercentWidth(80);
            column2.setHalignment(HPos.CENTER);

            playerPotionInfoGridPane.getColumnConstraints().addAll(column1, column2);
            playerPotionInfoGridPane.getStyleClass().addAll("playerObjectInfoGridPane");
            playerPotionInfoGridPane.setId(potionName);

            String formattedPotionName = potionName.replace("Minor ", "")
                    .replace("Medium ", "")
                    .replace("High ", "");

            formattedPotionName = formattedPotionName.substring(0, 1).toUpperCase() + formattedPotionName.substring(1);

            ImageView potionIcon = returnObjectImageView(formattedPotionName, 45, 45);
            Text potionText = new Text(potionName);
            potionText.getStyleClass().add("simpleObjectText");

            playerPotionInfoGridPane.add(potionIcon, 0, 0);
            playerPotionInfoGridPane.add(potionText, 1, 0);

            playerPotionInfoGridPane.setOnMouseReleased(event -> {
                selectPotion(playerPotionsGridPane, playerPotionInfoGridPane);
            });

            playerPotionsGridPane.add(playerPotionInfoGridPane, 0, index.get());
            index.getAndIncrement();
        });

        playerPotionsGridPane.setVgap(5);
        playerPotionsGridPane.setHgap(5);
    }


    private void selectPotion(GridPane playerPotionsGridPane, GridPane selectedGridPane) {
        selectSubGridPane(playerPotionsGridPane, selectedGridPane);
    }

    private void selectSpell(GridPane playerAvailableSpellsGrid, GridPane playerSpellInfoGridPane) {
        selectSubGridPane(playerAvailableSpellsGrid, playerSpellInfoGridPane);
    }

    private void selectSubGridPane(GridPane mainGridPane, GridPane selectedGridPane) {
        mainGridPane.getChildren().forEach(node -> {
            if (node instanceof GridPane) {
                ((GridPane) node).getStyleClass().remove("playerObjectInfoGridPanePressed");
            }
        });
        selectedGridPane.getStyleClass().add("playerObjectInfoGridPanePressed");
    }

    private void deselectAllSubGridPanes(GridPane mainGridPane) {
        mainGridPane.getChildren().forEach(node -> {
            if (node instanceof GridPane) {
                ((GridPane) node).getStyleClass().remove("playerObjectInfoGridPanePressed");
            }
        });
    }

    private void displayEnemyStats(Enemy enemy) {
        esNameT.setText(returnFormattedEnum(enemy.getEnemyName()));
        esHealthPg.setProgress(enemy.getHealthPoints() / enemy.getMaxHealthPoints());
        esHealthPg.getStyleClass().add("healthBar");
        esHealthT.setText((int) enemy.getHealthPoints() + "/" + (int) enemy.getMaxHealthPoints());

        esDefenseT.setText(String.valueOf((int) enemy.getDefensePoints()));
        esLevelT.setText(String.valueOf((int) enemy.getLevel()));

        esIconIm.setImage(returnObjectImage(returnFormattedEnum(enemy.getEnemyName())));

        HashMap<String, Spell> enemySpellsHashMap = enemy.getSpellsHashMap();


        if (enemySpellsHashMap.size() > 0) {
            AtomicInteger index = new AtomicInteger();
            enemySpellsHashMap.forEach((spellName, spell) -> {
                enemyAvailableAttacksGrid.add(returnEnemyAvailableAttacksGridPane(spellName), 0, index.get());
                index.getAndIncrement();
            });
        } else {
            String spellName = "Melee Attack";
            enemyAvailableAttacksGrid.add(returnEnemyAvailableAttacksGridPane(spellName), 0, 0);
        }

        enemyAvailableAttacksGrid.setVgap(5);
    }

}
