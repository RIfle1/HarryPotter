package project.javafx.controllers;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import project.classes.*;
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
import static project.functions.LevelFunctions.*;
import static project.javafx.controllers.GameMenuController.gameMenuScene;
import static project.javafx.functions.JavaFxFunctions.*;

// TODO - USE applyPotionEffect() before the player's turn

public class GameSceneController implements Initializable {
    private static final Text consoleTStatic = new Text();
    private static boolean actionButtonClicked = false;
    private static Spell enemyChosenSpell;
    private static Spell wizardChosenSpell;
    private static Enemy attackingEnemy;
    private static double enemyCalculatedDamage;
    private static boolean isSpellSelected = false;
    private static boolean isEnemySelected = false;
    @FXML
    private AnchorPane gameSceneMainAnchorPane;
    @FXML
    private Circle actionCircle;
    @FXML
    private Button attackBtn;
    @FXML
    private Circle baseActionCircle;
    @FXML
    private GridPane actionsGridPane;
    @FXML
    private AnchorPane combatAnchorPane;
    @FXML
    private Text consoleT;
    @FXML
    private Button dodgeBtn;
    @FXML
    private GridPane enemyAvailableAttacksGrid;
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
    private GridPane combatGridPane;
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
    private final Timeline actionCircleTimeline = new Timeline();
    private final GridPane enemyCombatGridPane = new GridPane();

    public static void gameScene(ActionEvent event) {
        FXMLLoader gameSceneFxmlLoader = new FXMLLoader(GuiMain.class.getResource("GameScene.fxml"));
        sendToScene(event, gameSceneFxmlLoader);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TESTS TO DELETE AFTERWARDS
        generateEnemies(10, 10, 1, EnemyName.VOLDEMORT);
        try {
            wizard.setPotionList(Arrays.asList(Potion.highDefensePotion.clone(), Potion.highDamagePotion.clone(), Potion.highDamagePotion.clone(), Potion.highDamagePotion.clone(), Potion.highHealthPotion.clone(), Potion.highHealthPotion.clone(), Potion.highHealthPotion.clone()));
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        // TESTS TO DELETE AFTERWARDS

        enemyCombatGridPane.setAlignment(Pos.CENTER);
        enemyCombatGridPane.setId("enemyCombatGridPane");
        combatGridPane.add(enemyCombatGridPane, 2, 0);

        disableAllGridPaneButtons(actionsGridPane);
        initializeActionCircleTimeline();

        displayPlayerStats();
        displayPlayerSpellsGrid();
        displayPlayerPotionsGridPane();
        displayEnemyStats(enemiesHashMap.get(enemiesKeyList.get(0)));
        displayCharacters();
    }

    @FXML
    void onKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ESCAPE)) {
            ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());

            Optional<ButtonType> result = Objects.requireNonNull(createPopup(actionEvent, Alert.AlertType.CONFIRMATION, "Are you sure you want to quit the battle? All progress will be lost."));

            if (result.get() == ButtonType.OK) {
                isEnemySelected = false;
                isSpellSelected = false;
                gameMenuScene(actionEvent);
            }

        }
    }

    @FXML
    void usePotionOnClick(ActionEvent event) {
        String potionName = playerPotionsGridPane.getChildren()
                .stream()
                .filter(node -> node.getStyleClass().contains("clickableNodePressed"))
                .toList().get(0).getId();

        Potion potion = wizard.returnPotion(potionName);

        wizard.drinkPotion(potion);

        updateConsoleT();
        displayPlayerStats();
        displayPlayerPotionsGridPane();
    }

    private void clearConsoleTa() {
        consoleT.setText("");
    }

    private void updateConsoleT() {
        String previousText = consoleT.getText();

        if (previousText.length() > 0) {
            previousText = previousText + "\n";
        } else {
            previousText = "";
        }

        consoleT.setText(previousText + consoleTStatic.getText());
        consoleTStatic.setText("");
    }

    @FXML
    private void attackOnClick(ActionEvent event) {
        defineSuccessActionCircle(wizardChosenSpell);
        actionButtonOnClick(event);
    }

    @FXML
    private void dodgeOnClick(ActionEvent event) {
        double dodgeChance = attackingEnemy.returnSpellChance(enemyChosenSpell);
        defineSuccessActionCircle(wizard.returnDodgeChance(attackingEnemy, dodgeChance));
        actionButtonOnClick(event);
    }

    @FXML
    private void parryOnClick(ActionEvent event) {
        double parrySuccess = wizard.returnParryChance() / enemyCalculatedDamage;
        if(parrySuccess > 1) parrySuccess = 1;

        defineSuccessActionCircle(parrySuccess);
        actionButtonOnClick(event);
    }


    private void initializeActionCircleTimeline() {
        double baseActionCircleRadius = baseActionCircle.getRadius();
        double actionCircleSpeed = (4 / Math.exp(wizard.getLevel() * wizard.getDifficulty().getWizardDiffMultiplier()));

        KeyValue actionCircleKeyValueGrow = new KeyValue(actionCircle.radiusProperty(), baseActionCircleRadius, Interpolator.EASE_IN);
        KeyFrame actionCircleKeyFrameGrow = new KeyFrame(Duration.seconds(actionCircleSpeed), actionCircleKeyValueGrow);
        actionCircleTimeline.getKeyFrames().add(actionCircleKeyFrameGrow);
        actionCircleTimeline.setAutoReverse(true);
        actionCircleTimeline.setCycleCount(Timeline.INDEFINITE);
    }


    void actionButtonOnClick(ActionEvent event) {
        Node buttonSource = (Node) event.getSource();
        String buttonId = buttonSource.getId();


        if (!actionButtonClicked) {
            actionButtonClicked = true;


            disableAllGridPaneButtons(combatGridPane, "enemyCombatGridPane");

            actionCircle.setRadius(1);
            actionCircleTimeline.play();

        } else {
            actionButtonClicked = false;
            isSpellSelected = false;

            boolean actionSucceeded = actionCircle.getRadius() <= successActionCircle.getRadius();

            if(buttonId.equals(attackBtn.getId())) {
                attackBtnOnClick(actionSucceeded);
            } else if(buttonId.equals(dodgeBtn.getId())) {
                dodgeBtnOnClick(actionSucceeded);
            } else if(buttonId.equals(parryBtn.getId())) {
                parryBtnOnClick(actionSucceeded);
            }

            actionCircleTimeline.stop();

            displayCharacters();
            displayPlayerStats();

            if(!enemiesHashMap.isEmpty()) {
                try {
                    List<Object> enemyObjectsList = returnSelectedNodes(gameSceneMainAnchorPane, "enemyCombatGridPane", "clickableNodePressed");
                    Enemy enemyVictim = enemiesHashMap.get((String) enemyObjectsList.get(0));

                    displayEnemyStats(enemiesHashMap.get(enemyVictim.getName()));
                    selectSubGridPane(enemyCombatGridPane, enemyVictim.getName());
                } catch (Exception e) {
                    displayEnemyStats(enemiesHashMap.get(enemiesKeyList.get(0)));
                    selectSubGridPane(enemyCombatGridPane, enemiesHashMap.get(enemiesKeyList.get(0)).getName());
                }
            }
            else {

                createPopup(event, Alert.AlertType.INFORMATION, "You won the battle! Congratulations!");
            }

            updateConsoleT();
        }
    }

    private void parryBtnOnClick(boolean actionSucceeded) {
        wizardDodgeOrParry(attackingEnemy, enemyChosenSpell, MoveType.PARRY, actionSucceeded);

    }

    private void dodgeBtnOnClick(boolean actionSucceeded) {
        wizardDodgeOrParry(attackingEnemy, enemyChosenSpell, MoveType.DODGE, actionSucceeded);

    }

    private void attackBtnOnClick(boolean actionSucceeded) {
        // GET THE ENEMY VICTIM
        List<Object> enemyObjectsList = returnSelectedNodes(gameSceneMainAnchorPane, "enemyCombatGridPane", "clickableNodePressed");
        Enemy enemyVictim = enemiesHashMap.get((String) enemyObjectsList.get(0));

        // Maybe I'll need this later for optimizations IDK like only reloading the grid pane in question and not the whole thing
        GridPane enemyGridPane = (GridPane) enemyObjectsList.get(1);

        // GET THE WIZARD SPELL
        List<Object> spellObjectsList = returnSelectedNodes(playerAvailableSpellsGrid, "clickableNodePressed");
        Spell wizardChosenSpell = wizard.getSpellsHashMap().get((String) spellObjectsList.get(0));

        // Maybe I'll need this later for optimizations IDK
        GridPane spellGridPane = (GridPane) spellObjectsList.get(1);

        // ACTIVATE POTIONS ----------------- MIGHT HAVE TO REMOVE THIS AND MOVE IT TO A MORE GENERAL FUNCTION LATER
        Wizard.wizard.applyPotionEffect();

        // WARN THE PLAYER ABOUT ENEMY CHANGES ----------------- MIGHT HAVE TO REMOVE THIS AND MOVE IT TO A MORE GENERAL FUNCTION LATER
        Enemy.checkEnemiesHpRatio();

        boolean isVulnerableSpell = enemyVictim.vulnerabilityChecker(enemyVictim.getEnemyName().getEnemyHpLimitRatio(), wizardChosenSpell);

        wizardCombatSystem(wizardChosenSpell, enemyVictim, actionSucceeded, isVulnerableSpell);

        if(enemiesHashMap.size() > 0) {
            displayPlayerSpellsGrid();
            disableAllGridPaneButtons(playerAvailableSpellsGrid);
            disableAllGridPaneButtons(combatGridPane, "enemyCombatGridPane");

            enemyTurn();
        }
        else {
            isEnemySelected = false;
        }
    }

    private void enemyTurn() {
        attackingEnemy = getRandomEnemy();
        enemyChosenSpell = getEnemyRandomSpell(attackingEnemy);
        enemyCalculatedDamage = attackingEnemy.returnEnemyCalculatedDamage(enemyChosenSpell);


        String chosenSpellName = attackingEnemy.returnChosenSpellName(enemyChosenSpell);
        notifyEnemyChosenSpell(chosenSpellName, attackingEnemy);

        disableGridPaneButton(actionsGridPane, attackBtn);

        enableGridPaneButton(actionsGridPane, parryBtn);
        enableGridPaneButton(actionsGridPane, dodgeBtn);
    }

    public void wizardDodgeOrParry(Enemy attackingEnemy, Spell enemyChosenSpell, MoveType wizardMoveType, boolean actionSucceeded) {
        boolean dodgeSuccess;
        boolean parrySuccess;

        dodgeSuccess = parrySuccess = actionSucceeded;

        String attackName = attackingEnemy.returnAttackName(enemyChosenSpell);

        boolean wizardDodgeOrParrySuccess = isWizardDodgeOrParrySuccess(attackingEnemy, enemyCalculatedDamage, attackName, wizardMoveType, dodgeSuccess, parrySuccess);
        enemyAttack(wizardDodgeOrParrySuccess, enemyCalculatedDamage, attackingEnemy, enemyChosenSpell);
        updateConsoleT();

        disableAllGridPaneButtons(actionsGridPane);
//        enableGridPaneButton(actionsGridPane, attackBtn);

        enableAllGridPaneButtons(playerAvailableSpellsGrid);
        enableAllGridPaneButtons(combatGridPane, "enemyCombatGridPane");
    }

    private void displayPlayerStats() {
        psNameT.setText(checkPlayerNameLength(wizard.getName(), 20));

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

        playerAvailableSpellsGrid.getChildren().clear();

        playerSpellsHashMap.forEach(spell -> {
            String spellName = spell.getSpellName();
            GridPane playerSpellInfoGridPane = new GridPane();
            playerSpellInfoGridPane.setId(spellName);
            ColumnConstraints column1 = new ColumnConstraints();
            ColumnConstraints column2 = new ColumnConstraints();

            column1.setPercentWidth(20);
            column1.setHalignment(HPos.CENTER);
            column2.setPercentWidth(80);
            column2.setHalignment(HPos.LEFT);

            playerSpellInfoGridPane.getColumnConstraints().addAll(column1, column2);

            Text spellText = new Text(spellName);
            ImageView spellIcon = returnObjectImageView(spellName, 30, 30);

            playerSpellInfoGridPane.add(spellIcon, 0, 0);
            playerSpellInfoGridPane.add(spellText, 1, 0);

            playerSpellInfoGridPane.getStyleClass().addAll("clickableNode");

            if(wizard.checkSpellReady(spell)) {
                spellText.getStyleClass().add("simpleEnabledNode");
                playerSpellInfoGridPane.setOnMouseReleased(event -> {
                    selectSubGridPane(playerAvailableSpellsGrid, playerSpellInfoGridPane);
                    defineSuccessActionCircle(spell);
                    wizardChosenSpell = spell;
                    isSpellSelected = true;
                    actionCircle.setRadius(1);
                    checkAttackBtnConditions();
                });
            }
            else {
                spellText.getStyleClass().add("simpleDisabledNode");
            }


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

    public void defineSuccessActionCircle(double successChance) {
        int successActionCircleRadius = (int) (baseActionCircle.getRadius() * successChance);
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
            playerPotionInfoGridPane.getStyleClass().addAll("clickableNode");
            playerPotionInfoGridPane.setId(potionName);

            String formattedPotionName = potionName.replace("Minor ", "")
                    .replace("Medium ", "")
                    .replace("High ", "");

            formattedPotionName = formattedPotionName.substring(0, 1).toUpperCase() + formattedPotionName.substring(1);

            ImageView potionIcon = returnObjectImageView(formattedPotionName, 45, 45);
            Text potionText = new Text(potionName);
            potionText.getStyleClass().add("simpleEnabledNode");

            playerPotionInfoGridPane.add(potionIcon, 0, 0);
            playerPotionInfoGridPane.add(potionText, 1, 0);

            playerPotionInfoGridPane.setOnMouseReleased(event -> selectSubGridPane(playerPotionsGridPane, playerPotionInfoGridPane));

            playerPotionsGridPane.add(playerPotionInfoGridPane, 0, index.get());
            index.getAndIncrement();
        });

        playerPotionsGridPane.setVgap(5);
        playerPotionsGridPane.setHgap(5);
    }

    private void displayCharacters() {

        // DISPLAY PLAYER IN COMBAT AREA
        combatGridPane.add(returnCharacterGridPane(wizard), 0, 0);

        // DISPLAY ENEMIES IN COMBAT AREA
        displayEnemiesGridPanes();
    }

    private void displayEnemiesGridPanes() {
        enemyCombatGridPane.getChildren().clear();

//        enemyCombatGridPane.setGridLinesVisible(true);

        AtomicInteger enemyCombatGridPaneRow = new AtomicInteger(0);
        AtomicInteger enemyCombatGridPaneColumn = new AtomicInteger(0);

        enemiesHashMap.forEach((enemyName, enemy) -> {
            GridPane enemyGridPane = returnCharacterGridPane(enemy);
            GridPane.setMargin(enemyGridPane, new Insets(0, 10, 20, 10));
            enemyGridPane.setId(enemy.getName());

            enemyGridPane.getStyleClass().add("clickableNode");

            enemyGridPane.setOnMouseReleased(mouseEvent -> enemyGridPaneOnClick(enemy, enemyGridPane));

            enemyGridPane.getChildren().forEach(node -> node.setOnMouseReleased(mouseEvent -> enemyGridPaneOnClick(enemy, enemyGridPane)));

            enemyCombatGridPane.add(enemyGridPane, enemyCombatGridPaneColumn.get(), enemyCombatGridPaneRow.get());
            enemyCombatGridPaneRow.getAndIncrement();

            if(enemyCombatGridPaneRow.get() == 7) {
                enemyCombatGridPaneRow.set(0);
                enemyCombatGridPaneColumn.getAndIncrement();
            }
        });
    }

    private void enemyGridPaneOnClick(Enemy enemy, GridPane enemyGridPane) {
        selectSubGridPane(enemyCombatGridPane, enemyGridPane);
        displayEnemyStats(enemy);
        isEnemySelected = true;
        actionCircle.setRadius(1);
        checkAttackBtnConditions();
    }

    public void checkAttackBtnConditions() {
        if(isEnemySelected && isSpellSelected) {
            enableGridPaneButton(actionsGridPane, attackBtn);
        }
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

        enemyAvailableAttacksGrid.getChildren().clear();

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

    public static void updateConsoleTaStatic(String text, boolean isSeparationNeeded) {
        HashMap<String, String> colorsHashMap = Color.returnAllColorsHashMap();

        for (Map.Entry<String, String> entry : colorsHashMap.entrySet()) {
            // I can probably make use of the color later if I want to add colored text in the gui console
            String color = entry.getKey();
            String colorCode = entry.getValue();

            if (text.contains(colorCode)) {
                text = text.replace(colorCode, "");
            }
        }
        if(isSeparationNeeded) {
            text = text + "\n" + returnLineSeparator(text.length());
        }

        if(consoleTStatic.getText().length() > 0) {
            text = consoleTStatic.getText() + "\n" + text;
        }

        consoleTStatic.setText(text);
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

        attackText.getStyleClass().add("simpleEnabledNode");

        enemySpellGridPane.add(spellIcon, 0, 0);
        enemySpellGridPane.add(attackText, 1, 0);

        return enemySpellGridPane;
    }

}


