package project.fx.controllers;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import project.classes.*;
import project.enums.*;

import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static project.classes.Enemy.*;
import static project.classes.Wizard.wizard;
import static project.enums.EnumMethods.returnFormattedEnum;
import static project.functions.ConsoleFunctions.returnLineSeparator;
import static project.functions.LevelFunctions.*;
import static project.fx.controllers.GameMenuController.gameMenuScene;
import static project.fx.functions.JavaFxFunctions.*;


public class GameSceneController implements Initializable {
    private static final Text consoleTStatic = new Text();
    private static boolean actionButtonClicked = false;
    private static Spell enemyChosenSpell;
    private static Spell wizardChosenSpell;
    private static Enemy attackingEnemy;
    private static double enemyCalculatedDamage;
    private static boolean isSpellSelected = false;
    private static boolean isEnemySelected = false;
    private static Level level;
    private static int combatTimeout;
    private static String deathLine;
    private static double dodgeChance;
    private static Stage gameSceneStage;
    private static GridPane playerGridPane;
    private final Timeline actionCircleTimeline = new Timeline();
    private final GridPane enemyCombatGridPane = new GridPane();
    private final GridPane playerCombatGridPane = new GridPane();
    private static boolean isBackButtonEnabled = true;
    @FXML
    private Circle actionCircle;
    @FXML
    private Button attackBtn;
    @FXML
    private Circle baseActionCircle;
    @FXML
    private GridPane actionsGridPane;
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
    private Text objectiveT;
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
    @FXML
    private GridPane middleGridPane;

    public static void gameScene(ActionEvent event, Level levelParam) {
        Enemy.clearEnemies();
        levelParam.getEnemyNameList().forEach(enemyName ->
                generateEnemies(levelParam.getEnemyMinLevel(), levelParam.getEnemyMaxLevel(), levelParam.getEnemyAmount(), enemyName));
        combatTimeout = levelParam.getCombatTimeout();
        level = levelParam;

        gameSceneStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FXMLLoader gameSceneFxmlLoader = new FXMLLoader(returnFXMLURL("GameScene.fxml"));
        sendToScene(event, gameSceneFxmlLoader);
    }

    public static void gameScene(ActionEvent event, int enemyMinLevel, int enemyMaxLevel, int enemyAmount, Level levelParam) {
        Enemy.clearEnemies();
        generateEnemies(enemyMinLevel,  enemyMaxLevel, enemyAmount);
        combatTimeout = levelParam.getCombatTimeout();
        level = levelParam;

        gameSceneStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FXMLLoader gameSceneFxmlLoader = new FXMLLoader(returnFXMLURL("GameScene.fxml"));
        sendToScene(event, gameSceneFxmlLoader);
    }

    private static void returnToGameMenu(Stage stage, Alert.AlertType alertType, String alertMessage) {
        Optional<ButtonType> result = Objects.requireNonNull(createPopup(gameSceneStage, alertType, alertMessage));

        if(result.isPresent()) {
            if(result.get() == ButtonType.OK) {
                isEnemySelected = false;
                isSpellSelected = false;
                gameMenuScene(stage);
            }
        }
        else if (!isBackButtonEnabled) {
            isEnemySelected = false;
            isSpellSelected = false;
            gameMenuScene(stage);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        enemyCombatGridPane.setAlignment(Pos.CENTER);
        enemyCombatGridPane.setId("enemyCombatGridPane");

        playerCombatGridPane.setAlignment(Pos.CENTER);
        playerCombatGridPane.setId("playerCombatGridPane");

        combatGridPane.add(enemyCombatGridPane, 2, 0);
        combatGridPane.add(playerCombatGridPane, 0, 0);

        disableAllGridPaneButtons(actionsGridPane);
        initializeActionCircleTimeline();

        displayPlayerStats();
        displayObjective();
        updateDeathLine();
        displayPlayerSpellsGrid();
        displayPlayerPotionsGridPane();
        displayEnemyStats(enemiesHashMap.get(enemiesKeyList.get(0)));
        displayCharacters();

        switchTeams();
    }

    @FXML
    void onKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ESCAPE)) {
            String alertMsg = "Are you sure you want to quit the battle? All progress will be lost.";
            returnToGameMenu(gameSceneStage, Alert.AlertType.CONFIRMATION, alertMsg);
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
        displayPlayerSpellsGrid();
    }

    @FXML
    private void attackOnClick(ActionEvent event) {
        defineSuccessActionCircle(wizardChosenSpell);
        actionButtonOnClick(event);
    }

    @FXML
    private void dodgeOnClick(ActionEvent event) {
        defineSuccessActionCircle(wizard.returnDodgeChance(attackingEnemy, dodgeChance));
        actionButtonOnClick(event);
    }

    @FXML
    private void parryOnClick(ActionEvent event) {
        double parrySuccess = wizard.returnParryChance() / enemyCalculatedDamage;
        if (parrySuccess > 1) parrySuccess = 1;

        defineSuccessActionCircle(parrySuccess);
        actionButtonOnClick(event);
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
        if (isSeparationNeeded) {
            text = text + "\n" + returnLineSeparator(text.length());
        }

        if (consoleTStatic.getText().length() > 0) {
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
        ImageView spellIcon = returnObjectImageView(spellName, 30, 30, 1);

        attackText.getStyleClass().add("simpleEnabledNode");

        enemySpellGridPane.add(spellIcon, 0, 0);
        enemySpellGridPane.add(attackText, 1, 0);

        return enemySpellGridPane;
    }

    private void switchTeams() {
        if (level.isSwitchTeams()) {
            Optional<ButtonType> result = createPopup(gameSceneStage, Alert.AlertType.CONFIRMATION, "You have the possibility the join the enemies. Do you want to join them?");

            if (result.get() == ButtonType.OK) {
                createPopup(gameSceneStage, Alert.AlertType.INFORMATION, "You have switched teams! Great job! You gained nothing, now do the level properly.");
            }
        }
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

            if (buttonId.equals(attackBtn.getId())) {
                attackBtnOnClick(actionSucceeded);
            } else if (buttonId.equals(dodgeBtn.getId())) {
                dodgeBtnOnClick(actionSucceeded);
            } else if (buttonId.equals(parryBtn.getId())) {
                parryBtnOnClick(actionSucceeded);
            }

            actionCircleTimeline.stop();

            updateConsoleT();

            if (enemiesHashMap.isEmpty()) {
                wizardWon();
            } else if (wizard.checkIfDead()) {
                wizardLost();
            }
        }
    }

    private void wizardLost() {
        String alertMsg = "You lost the battle! Better luck next time!";

        isBackButtonEnabled = false;
        returnToGameMenu(gameSceneStage, Alert.AlertType.INFORMATION, alertMsg);
    }

    private void wizardWon() {
        String popUpMsg = deathLine + "\n" + level.getGraduationLine();
        wizardEndLevelRewards(level);

        isBackButtonEnabled = false;
        returnToGameMenu(gameSceneStage, Alert.AlertType.INFORMATION, popUpMsg);
    }

    private void checkCombatTimeout() {
        combatTimeout--;

        if (combatTimeout == 0) {
            String timeoutMsg;
            if (level.equals(Level.The_Order_of_the_Phoenix)) {
                timeoutMsg = "You distracted Dolores Umbridge long enough for the fireworks to go off." + "\n" + level.getGraduationLine();
                actionCircleTimeline.stop();
                wizardEndLevelRewards(level);
            } else {
                timeoutMsg = EnemyName.timeoutDeathLine;
            }

            isBackButtonEnabled = false;
            returnToGameMenu(gameSceneStage, Alert.AlertType.INFORMATION, timeoutMsg);
        }
    }

    private void parryBtnOnClick(boolean actionSucceeded) {
        wizardDodgeOrParry(attackingEnemy, enemyChosenSpell, MoveType.PARRY, false, actionSucceeded);

    }

    private void dodgeBtnOnClick(boolean actionSucceeded) {
        wizardDodgeOrParry(attackingEnemy, enemyChosenSpell, MoveType.DODGE, actionSucceeded, false);

    }

    private void attackBtnOnClick(boolean actionSucceeded) {
        try {
            // GET THE ENEMY VICTIM
            List<Object> enemyObjectsList = returnSelectedNodes(combatGridPane, "enemyCombatGridPane", "clickableNodePressed");
            Enemy enemyVictim = enemiesHashMap.get((String) enemyObjectsList.get(0));
//        combatGridPane.getChildren().forEach(node -> System.out.println(node.getStyleClass()));
//        System.out.println(enemyVictim);

            // Maybe I'll need this later for optimizations IDK like only reloading the grid pane in question and not the whole thing
            GridPane enemyGridPane = (GridPane) enemyObjectsList.get(1);

            // GET THE WIZARD SPELL
            List<Object> spellObjectsList = returnSelectedNodes(playerAvailableSpellsGrid, "clickableNodePressed");
            Spell wizardChosenSpell = wizard.getSpellsHashMap().get((String) spellObjectsList.get(0));


            // Maybe I'll need this later for optimizations IDK
            GridPane spellGridPane = (GridPane) spellObjectsList.get(1);

            // ACTIVATE POTIONS ----------------- MIGHT HAVE TO REMOVE THIS AND MOVE IT TO A MORE GENERAL FUNCTION LATER
            Wizard.wizard.applyPotionEffect();

            enemyVictim.updateBossVulnerableSpellsList();
            boolean isVulnerableSpell = enemyVictim.vulnerabilityChecker(enemyVictim.getEnemyName().getEnemyHpLimitRatio(), wizardChosenSpell);

            // CALCULATE THE SPELL DAMAGE BASED ON OTHER BUFFS
            double wizardCalculatedDamage = wizard.returnSpellCalculatedDamage(wizardChosenSpell, enemyVictim);

            // GET PARRY AND DODGE SUCCESS
            double spellSuccess = Math.random();
            double dodgeChance = wizard.returnSpellChance(wizardChosenSpell);

            boolean enemyDodgeSuccess = spellSuccess <= enemyVictim.returnDodgeChance(wizard, dodgeChance);
            boolean enemyParrySuccess = enemyVictim.returnParryChance() > wizardCalculatedDamage;

            MoveType enemyMoveType = returnEnemyMoveType();

            wizardCombatSystem(wizardChosenSpell, enemyVictim, actionSucceeded, isVulnerableSpell, enemyDodgeSuccess, enemyParrySuccess, wizardCalculatedDamage, enemyMoveType);


            if(enemyDodgeSuccess && enemyMoveType.equals(MoveType.DODGE)) {
                dodgeAnimation(actionSucceeded, playerCombatGridPane, playerGridPane, enemyGridPane,
                        wizardChosenSpell.getSpellImg(), 100, 200, false);
            }
            else if(enemyParrySuccess && enemyMoveType.equals(MoveType.PARRY)) {
                parryAnimation(actionSucceeded, playerCombatGridPane,
                        playerGridPane, enemyGridPane, enemyCombatGridPane,
                        wizardChosenSpell.getSpellImg(), 100, 200, wizard,
                        () -> refreshPlayerGridPane(playerGridPane));
            }
            else if (isVulnerableSpell) {
                attackAnimation(actionSucceeded, playerCombatGridPane,
                        playerGridPane, enemyGridPane,
                        wizardChosenSpell.getSpellImg(), 100, 200, false, () -> refreshEnemiesGridPane(enemyGridPane));
            }

            displayPlayerPotionsGridPane();

            if (!enemiesHashMap.isEmpty()) {
                displayPlayerSpellsGrid();
                disableAllGridPaneButtons(playerAvailableSpellsGrid);
                disableAllGridPaneButtons(combatGridPane, "enemyCombatGridPane");

                enemyTurn();
            } else {
                isEnemySelected = false;
            }
        }
        catch (Exception e) {
            System.out.println("attackBtnOnClick *FUNCTION* -> No enemy selected");

            List<Object> enemyObjectsList = returnSelectedNodes(combatGridPane, "enemyCombatGridPane", "clickableNodePressed");
            GridPane enemyGridPane = (GridPane) enemyObjectsList.get(1);
            System.out.println(enemyGridPane);
            enemyCombatGridPane.getChildren().remove(enemyGridPane);


            isEnemySelected = false;
            isSpellSelected = false;
            checkAttackBtnConditions();
            displayPlayerSpellsGrid();
            createPopup(gameSceneStage, Alert.AlertType.ERROR, "No enemy selected");
        }

    }

    private void enemyTurn() {
        attackingEnemy = getRandomEnemy();

        String chosenSpellName = "";

        if (attackingEnemy.getEnemyName().getEnemyCombat() == EnemyCombat.SPELL) {
            enemyChosenSpell = getEnemyRandomSpell(attackingEnemy);
            enemyCalculatedDamage = attackingEnemy.returnSpellCalculatedDamage(enemyChosenSpell, wizard);
            chosenSpellName = attackingEnemy.returnChosenSpellName(enemyChosenSpell);
            dodgeChance = attackingEnemy.returnSpellChance(enemyChosenSpell);

        } else if (attackingEnemy.getEnemyName().getEnemyCombat() == EnemyCombat.MELEE) {
            chosenSpellName = "Melee Attack";
            enemyCalculatedDamage = attackingEnemy.returnMeleeCalculatedDamage(wizard);
            dodgeChance = attackingEnemy.getEnemyName().getEnemyCombat().getCombatChance();
        }

        notifyEnemyChosenSpell(chosenSpellName, attackingEnemy);

        disableGridPaneButton(actionsGridPane, attackBtn);

        enableGridPaneButton(actionsGridPane, parryBtn);
        enableGridPaneButton(actionsGridPane, dodgeBtn);

        checkCombatTimeout();
    }

    public void wizardDodgeOrParry(Enemy attackingEnemy, Spell enemyChosenSpell, MoveType wizardMoveType, boolean dodgeSuccess, boolean parrySuccess) {
        String attackName = attackingEnemy.returnAttackName(enemyChosenSpell);

        boolean enemyAttackSucceeded = isAttackSucceeded(attackingEnemy, enemyChosenSpell);
        boolean wizardDodgeOrParrySuccess = isWizardDodgeOrParrySuccess(attackingEnemy, enemyCalculatedDamage, attackName, wizardMoveType, dodgeSuccess, parrySuccess, enemyAttackSucceeded);

        enemyAttack(enemyAttackSucceeded, wizardDodgeOrParrySuccess, enemyCalculatedDamage, attackingEnemy, enemyChosenSpell);
        GridPane enemyGridPane = (GridPane) returnChildNodeById(enemyCombatGridPane, attackingEnemy.getName());

        String spellImg = "";
        int imgHeight = 0;
        int imgWidth = 0;
        boolean rotate = true;

        if(attackingEnemy.getEnemyName().getEnemyCombat() == EnemyCombat.SPELL) {
            spellImg = enemyChosenSpell.getSpellImg();
            imgHeight = 100;
            imgWidth = 200;
        }
        else if(attackingEnemy.getEnemyName().getEnemyCombat() == EnemyCombat.MELEE) {
            spellImg = "Melee";
            imgHeight = 200;
            imgWidth = 50;
            rotate = false;
        }

        if(!wizardDodgeOrParrySuccess) {
            attackAnimation(enemyAttackSucceeded, enemyCombatGridPane,
                    enemyGridPane, playerGridPane, spellImg, imgHeight, imgWidth, rotate,
                    () -> refreshPlayerGridPane(playerGridPane));
        }
        else if(dodgeSuccess) {
            dodgeAnimation(enemyAttackSucceeded, enemyCombatGridPane,
                    enemyGridPane, playerGridPane, spellImg, imgHeight, imgWidth, rotate);
        }
        else if(parrySuccess) {
            parryAnimation(enemyAttackSucceeded, enemyCombatGridPane,
                    enemyGridPane, playerGridPane, playerCombatGridPane,
                    spellImg, imgHeight, imgWidth, attackingEnemy,
                    () -> refreshEnemiesGridPane(enemyGridPane));
        }

        // WARN THE PLAYER ABOUT ENEMY CHANGES
        Enemy.checkEnemiesHpRatio();
        displayPlayerSpellsGrid();

        updateConsoleT();

        disableAllGridPaneButtons(actionsGridPane);
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

        Image img = returnObjectImage(level.getLevelName());

        BackgroundImage levelBackgroundImage = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background levelBackground = new Background(levelBackgroundImage);

        middleGridPane.setBackground(levelBackground);

    }

    private void displayObjective() {
        boolean gryffindorHouse = wizard.getHouseName() == HouseName.GRYFFINDOR;
        List<String> objectiveList = level.getObjectiveList();
        String objective;

        if (gryffindorHouse && objectiveList.size() > 1) {
            objective = objectiveList.get(1);
        } else {
            objective = objectiveList.get(0);
        }
        objectiveT.setText(objective);
    }

    private void updateDeathLine() {
        deathLine = "";

        if(!level.getEnemyNameList().isEmpty()) {
            List<EnemyName> enemyNameList = level.getEnemyNameList();
            List<String> enemyDeathLineList = new ArrayList<>();

            if (enemyNameList.size() > 1) {
                StringBuilder deathLine = new StringBuilder();
                enemyNameList.forEach(enemyName -> deathLine.append(enemyName.getEnemyDeathLine().get(0)).append("\n"));
                enemyDeathLineList.add(deathLine.toString());
            } else if (!enemyNameList.get(0).getEnemyDeathLine().isEmpty()) {
                enemyDeathLineList.addAll(enemyNameList.get(0).getEnemyDeathLine());
            }

            boolean gryffindorHouse = wizard.getHouseName() == HouseName.GRYFFINDOR;
            List<String> objectiveList = level.getObjectiveList();

            if (enemyDeathLineList.size() > 1) {
                if ((gryffindorHouse && objectiveList.size() > 1) || level.equals(Level.The_Order_of_the_Phoenix)) {
                    deathLine = enemyDeathLineList.get(1);
                } else {
                    deathLine = enemyDeathLineList.get(0);
                }
            } else if (enemyDeathLineList.size() == 1) {
                deathLine = enemyDeathLineList.get(0);
            }
        }
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
            ImageView spellIcon = returnObjectImageView(spellName, 30, 30, 1);

            playerSpellInfoGridPane.add(spellIcon, 0, 0);
            playerSpellInfoGridPane.add(spellText, 1, 0);

            playerSpellInfoGridPane.getStyleClass().addAll("clickableNode");

            if (wizard.checkSpellReady(spell)) {
                spellText.getStyleClass().add("simpleEnabledNode");
                playerSpellInfoGridPane.setOnMouseReleased(event -> {
                    selectSubGridPane(playerAvailableSpellsGrid, playerSpellInfoGridPane);
                    defineSuccessActionCircle(spell);
                    wizardChosenSpell = spell;
                    isSpellSelected = true;
                    actionCircle.setRadius(1);
                    checkAttackBtnConditions();
                });
            } else {
                spellText.getStyleClass().add("simpleDisabledNode");
                playerSpellInfoGridPane.setOnMouseReleased(event -> {
                    ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());
                    createPopup(actionEvent, Alert.AlertType.WARNING, spell.getSpellName() + " will be ready in " + spell.getSpellReadyIn() + " turn(s).");
                });
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

            ImageView potionIcon = returnObjectImageView(formattedPotionName, 45, 45, 1);
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
        playerGridPane = generateCharacterGridPane(wizard);
        playerCombatGridPane.add(playerGridPane, 0, 0);

        // DISPLAY ENEMIES IN COMBAT AREA
        displayEnemiesGridPanes();
    }

    private void displayEnemiesGridPanes() {
        enemyCombatGridPane.getChildren().clear();

        double maxRows = 7;
        double maxColumns = Math.round(enemiesHashMap.size() / maxRows);
        AtomicInteger addedRows = new AtomicInteger();
        AtomicInteger addedColumns = new AtomicInteger();

        int horizontalMargin = 5;
        int verticalMargin = 15;

//        enemyCombatGridPane.setGridLinesVisible(true);

        AtomicInteger enemyCombatGridPaneRow = new AtomicInteger(0);
        AtomicInteger enemyCombatGridPaneColumn = new AtomicInteger(0);

        enemiesHashMap.forEach((enemyName, enemy) -> {

            GridPane enemyGridPane = generateCharacterGridPane(enemy);
//            GridPane.setMargin(enemyGridPane, new Insets(verticalMargin, horizontalMargin, verticalMargin, horizontalMargin));
            enemyGridPane.setId(enemy.getName());
//            enemyGridPane.getStyleClass().add("clickableNode");

            if(addedRows.get() < maxRows) {
                RowConstraints row = new RowConstraints();
//                row.setPrefHeight(enemyGridPane.getPrefHeight() + horizontalMargin * 2);
                row.setPrefHeight(enemyGridPane.getPrefHeight());
                row.setMaxHeight(Region.USE_PREF_SIZE);
                row.setMinHeight(Region.USE_PREF_SIZE);

                row.setValignment(VPos.CENTER);
                enemyCombatGridPane.getRowConstraints().add(row);

                addedRows.getAndIncrement();
            }

            if(addedColumns.get() < maxColumns) {
                ColumnConstraints column = new ColumnConstraints();
//                column.setPrefWidth(enemyGridPane.getPrefWidth() + verticalMargin * 2);
                column.setPrefWidth(enemyGridPane.getPrefWidth());
                column.setMaxWidth(Region.USE_PREF_SIZE);
                column.setMinWidth(Region.USE_PREF_SIZE);

                column.setHalignment(HPos.CENTER);
                enemyCombatGridPane.getColumnConstraints().add(column);

                addedColumns.getAndIncrement();
            }

            enemyGridPane.getStyleClass().add("clickableNode");
            enemyGridPane.setOnMouseReleased(mouseEvent -> enemyGridPaneOnClick(enemy, enemyGridPane));
            enemyGridPane.getChildren().forEach(node -> node.setOnMouseReleased(mouseEvent -> enemyGridPaneOnClick(enemy, enemyGridPane)));

            enemyCombatGridPane.add(enemyGridPane, enemyCombatGridPaneColumn.get(), enemyCombatGridPaneRow.get());
            enemyCombatGridPaneRow.getAndIncrement();


            if (enemyCombatGridPaneRow.get() == maxRows) {
                enemyCombatGridPaneRow.set(0);
                enemyCombatGridPaneColumn.getAndIncrement();
            }
        });
//        enemyCombatGridPane.setGridLinesVisible(true);
        enemyCombatGridPane.setHgap(verticalMargin * 2);
        enemyCombatGridPane.setVgap(horizontalMargin * 2);
    }

    private void refreshEnemiesGridPane(GridPane enemyGridPane) {
        ProgressBar enemyHealthBar = (ProgressBar) enemyGridPane.getChildren()
                .stream().filter(node -> node instanceof ProgressBar)
                .findFirst().orElse(null);

        Enemy enemy = enemiesHashMap.get(enemyGridPane.getId());

        if (enemy == null) {
            enemyCombatGridPane.getChildren().remove(enemyGridPane);

            if(returnSelectedNodes(enemyCombatGridPane, "clickableNodePressed").isEmpty()) {
                isEnemySelected = false;
                checkAttackBtnConditions();
            }

        } else {
            assert enemyHealthBar != null;
            enemyHealthBar.setProgress(enemy.getHealthPoints() / enemy.getMaxHealthPoints());
        }

        if (!enemiesHashMap.isEmpty()) {
            try {
                List<Object> enemyObjectsList = returnSelectedNodes(combatGridPane, "enemyCombatGridPane", "clickableNodePressed");
                Enemy enemyVictim = enemiesHashMap.get((String) enemyObjectsList.get(0));
                displayEnemyStats(enemiesHashMap.get(enemyVictim.getName()));

            } catch (Exception e) {
                System.out.println("refreshEnemiesGridPane *FUNCTION* -> No enemy node selected");
                displayEnemyStats(enemiesHashMap.get(enemiesKeyList.get(0)));
                isEnemySelected = false;
            }
        }
        else {
            setEnemyStatsNull();
        }
    }

    private void setEnemyStatsNull() {
        esHealthPg.setProgress(0);
    }

    private void refreshPlayerGridPane(GridPane wizardGridPane) {
        ProgressBar enemyHealthBar = (ProgressBar) wizardGridPane.getChildren()
                .stream().filter(node -> node instanceof ProgressBar)
                .findFirst().orElse(null);


        assert enemyHealthBar != null;
        enemyHealthBar.setProgress(wizard.getHealthPoints() / wizard.getMaxHealthPoints());

        displayPlayerStats();
    }

    private void enemyGridPaneOnClick(Enemy enemy, GridPane enemyGridPane) {
        selectSubGridPane(enemyCombatGridPane, enemyGridPane);
        displayEnemyStats(enemy);
        isEnemySelected = true;
        actionCircle.setRadius(1);
        checkAttackBtnConditions();
    }

    public void checkAttackBtnConditions() {
        if (isEnemySelected && isSpellSelected) {
            enableGridPaneButton(actionsGridPane, attackBtn);
        }
        else {
            actionCircleTimeline.stop();
            disableGridPaneButton(actionsGridPane, attackBtn);
            enableAllGridPaneButtons(combatGridPane, "enemyCombatGridPane");
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

}


