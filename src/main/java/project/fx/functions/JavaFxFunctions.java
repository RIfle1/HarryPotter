package project.fx.functions;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;
import project.abstractClasses.AbstractCharacter;
import project.classes.Enemy;
import project.classes.Wizard;
import project.enums.EnemyCombat;
import project.fx.GuiLauncherMain;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.List;

import static project.enums.EnumMethods.returnFormattedEnum;
import static project.functions.GeneralFunctions.chooseRandomDouble;
import static project.functions.GeneralFunctions.returnFileAttribute;
import static project.functions.SaveFunctions.returnWizardInstance;

public class JavaFxFunctions {

    public static void sendToScene(ActionEvent event, FXMLLoader FXMLLoader) {
        Scene scene = getScene(FXMLLoader);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void sendToScene(Stage stage, FXMLLoader FXMLLoader) {
        Scene scene = getScene(FXMLLoader);

        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private static Scene getScene(FXMLLoader FXMLLoader) {
        Scene scene;

        try {
            scene = new Scene(FXMLLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return scene;
    }

    public static Optional<ButtonType> createPopup(ActionEvent event, Alert.AlertType alertType, String popUpMsg) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Alert alert = new Alert(alertType, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);

        alert.getDialogPane().setContentText(popUpMsg);
        return alert.showAndWait();
    }

    public static Optional<ButtonType> createPopup(Stage stage, Alert.AlertType alertType, String popUpMsg) {
        Alert alert = new Alert(alertType, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);

        alert.getDialogPane().setContentText(popUpMsg);
        return alert.showAndWait();
    }

    public static GridPane returnSaveInfoGridPane(String filename) {
        GridPane saveInfoGridPane = new GridPane();
        saveInfoGridPane.setAlignment(Pos.CENTER);
        saveInfoGridPane.setId(filename);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHalignment(HPos.CENTER);
        column1.setPercentWidth(100);
        saveInfoGridPane.getColumnConstraints().add(column1);

        Wizard wizardInstance = returnWizardInstance(filename);

        String fileCreationTime = returnFileAttribute("saves", filename, "creationTime");
        String fileModifiedTime = returnFileAttribute("saves", filename, "lastModifiedTime");
        String wizardName = wizardInstance.getName();
        String wizardLevel = String.valueOf((int) wizardInstance.getLevel());
        String wizardHouse = returnFormattedEnum(wizardInstance.getHouseName());
        String wizardDifficulty = returnFormattedEnum(wizardInstance.getDifficulty());

        Text wizardNameText = new Text(wizardName);
        Text wizardLevelText = new Text("Level " + wizardLevel);
        Text wizardHouseText = new Text("House of " + wizardHouse);
        Text wizardDifficultyText = new Text("Difficulty: " + wizardDifficulty);
        Text fileCreationTimeText = new Text("Creation Time: " + fileCreationTime);
        Text fileModifiedTimeText = new Text("Last Modified Time: " + fileModifiedTime);

        saveInfoGridPane.add(wizardNameText, 0, 0);
        saveInfoGridPane.add(wizardLevelText, 0, 1);
        saveInfoGridPane.add(wizardHouseText, 0, 2);
        saveInfoGridPane.add(wizardDifficultyText, 0, 3);
        saveInfoGridPane.add(fileCreationTimeText, 0, 4);
        saveInfoGridPane.add(fileModifiedTimeText, 0, 5);

        saveInfoGridPane.getStyleClass().add("saveInfoGridPane");
        saveInfoGridPane.setGridLinesVisible(true);

        return saveInfoGridPane;
    }

    public static ImageView returnObjectImageView(String objectName, double height, double width, double opacity) {
        Image spellImage = returnObjectImage(objectName);
        ImageView spellImageView = new ImageView(spellImage);
        spellImageView.setFitHeight(height);
        spellImageView.setFitWidth(width);
        spellImageView.setOpacity(opacity);


        return spellImageView;
    }

    public static Image returnObjectImage(String objectName) {
        String imgPath;
        try {
            imgPath = returnImagePath(objectName);
        } catch (Exception e) {
            System.out.println("Image not found");
            imgPath = returnImagePath("unknown");
        }
        return new Image(imgPath);
    }

    public static String returnImagePath(String objectName) {
        return Objects.requireNonNull(GuiLauncherMain.class.getClassLoader().getResource("project/fx/images/"))  + objectName + ".png";
    }

    public static URL returnFXMLURL(String fxmlName) {
        try {
            return new URL(Objects.requireNonNull(GuiLauncherMain.class.getClassLoader().getResource("project/fx/fxml/")) + fxmlName);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }

    public static GridPane generateCharacterGridPane(AbstractCharacter abstractCharacter) {
        // CHARACTER NAME AND PROFILE PIC GRID PANE
        GridPane horizontalGridPane = new GridPane();
        horizontalGridPane.setAlignment(Pos.CENTER);
        ColumnConstraints horizontalGridPaneColumn1 = new ColumnConstraints();
        ColumnConstraints horizontalGridPaneColumn2 = new ColumnConstraints();

        RowConstraints horizontalGridPaneRow1 = new RowConstraints();

        horizontalGridPaneColumn1.setHalignment(HPos.CENTER);
        horizontalGridPaneColumn2.setHalignment(HPos.CENTER);

        horizontalGridPaneRow1.setValignment(VPos.CENTER);

        horizontalGridPaneColumn1.setMaxWidth(Region.USE_COMPUTED_SIZE);
        horizontalGridPaneColumn1.setMinWidth(Region.USE_COMPUTED_SIZE);
        horizontalGridPaneColumn1.setPrefWidth(70);

        horizontalGridPaneColumn2.setMaxWidth(Region.USE_COMPUTED_SIZE);
        horizontalGridPaneColumn2.setMinWidth(Region.USE_COMPUTED_SIZE);
        horizontalGridPaneColumn2.setPrefWidth(100);

//        horizontalGridPaneRow1.setMaxHeight(Region.USE_COMPUTED_SIZE);
//        horizontalGridPaneRow1.setMinHeight(Region.USE_COMPUTED_SIZE);
//        horizontalGridPaneRow1.setPrefHeight(60);

        horizontalGridPane.getColumnConstraints().addAll(horizontalGridPaneColumn1, horizontalGridPaneColumn2);
        horizontalGridPane.getRowConstraints().add(horizontalGridPaneRow1);


//        horizontalGridPane.setGridLinesVisible(true);

        // CHARACTER MAIN GRID PANE WITH INFO AT THE TOP AND HEALTH BAR AT THE BOTTOM
        GridPane verticalGridPane = new GridPane();
        verticalGridPane.setAlignment(Pos.CENTER);
        RowConstraints verticalGridPaneRow1 = new RowConstraints();
        RowConstraints verticalGridPaneRow2 = new RowConstraints();
        ColumnConstraints verticalGridPaneColumn1 = new ColumnConstraints();

        int verticalGridPaneColumn1Height = 170;

        verticalGridPaneColumn1.setMaxWidth(Region.USE_PREF_SIZE);
        verticalGridPaneColumn1.setMinWidth(Region.USE_PREF_SIZE);
        verticalGridPaneColumn1.setPrefWidth(verticalGridPaneColumn1Height);

        verticalGridPaneColumn1.setHgrow(Priority.ALWAYS);
        verticalGridPaneColumn1.setHalignment(HPos.CENTER);

        int verticalGridPaneRow1Height = 60;
        int verticalGridPaneRow2Height = 25;

        verticalGridPaneRow1.setMaxHeight(Region.USE_PREF_SIZE);
        verticalGridPaneRow1.setMinHeight(Region.USE_PREF_SIZE);
        verticalGridPaneRow1.setPrefHeight(verticalGridPaneRow1Height);

        verticalGridPaneRow2.setMaxHeight(Region.USE_PREF_SIZE);
        verticalGridPaneRow2.setMinHeight(Region.USE_PREF_SIZE);
        verticalGridPaneRow2.setPrefHeight(verticalGridPaneRow2Height);

        verticalGridPaneRow2.setVgrow(Priority.ALWAYS);
        verticalGridPaneRow2.setValignment(VPos.CENTER);

        verticalGridPane.getRowConstraints().addAll(verticalGridPaneRow1, verticalGridPaneRow2);
        verticalGridPane.getColumnConstraints().addAll(verticalGridPaneColumn1);

        StackPane backgroundPane1 = new StackPane();
        backgroundPane1.getStyleClass().add("characterTopGridPaneBackground");
        GridPane.setFillHeight(backgroundPane1, true);
        GridPane.setFillWidth(backgroundPane1, true);
        verticalGridPane.add(backgroundPane1, 0, 0);

        verticalGridPane.add(horizontalGridPane, 0, 0);

//        verticalGridPane.setGridLinesVisible(true);

        String characterName;
        String characterNameString;
        if(abstractCharacter.getClass().equals(Wizard.class)) {
            characterName = "hagrid";
            characterNameString = abstractCharacter.getName();
        }
        else {
            characterNameString = characterName = returnFormattedEnum(((Enemy) abstractCharacter).getEnemyName());
        }

        ImageView characterImageView = returnObjectImageView(characterName, 50, 50, 1);

        Text characterNameText = new Text(characterNameString);
        characterNameText.setWrappingWidth(80);
        characterNameText.getStyleClass().add("characterGridPaneText");

        horizontalGridPane.add(characterImageView, 0, 0);
        horizontalGridPane.add(characterNameText, 1, 0);

        ProgressBar characterHealthBar = generateCharacterProgressBar(abstractCharacter.getHealthPoints() / abstractCharacter.getMaxHealthPoints());

        verticalGridPane.setId(abstractCharacter.getName());

        StackPane backgroundPane2 = new StackPane();
        backgroundPane2.getStyleClass().add("characterBottomGridPaneBackground");
        GridPane.setFillHeight(backgroundPane2, true);
        GridPane.setFillWidth(backgroundPane2, true);
        verticalGridPane.add(backgroundPane2, 0, 1);

        verticalGridPane.add(characterHealthBar, 0, 1);
//        verticalGridPane.getStyleClass().add("clickableNode");


        verticalGridPane.setPrefHeight(verticalGridPaneRow1Height + verticalGridPaneRow2Height);
        verticalGridPane.setPrefWidth(verticalGridPaneColumn1Height);

//        verticalGridPane.setGridLinesVisible(true);

        return verticalGridPane;
    }


    private static ProgressBar generateCharacterProgressBar(double progressBarValue) {
        ProgressBar characterHealthBar = new ProgressBar();
        characterHealthBar.setProgress(progressBarValue);
        characterHealthBar.setPrefHeight(12);
        characterHealthBar.setPrefWidth(120);
        characterHealthBar.getStyleClass().add("characterGridPaneHealthBar");

        return characterHealthBar;
    }

    public static String checkPlayerNameLength(String playerName, int maxLength) {

        StringBuilder stringBuilder = new StringBuilder();
        playerName = playerName.toLowerCase();

        try {
            Arrays.stream(playerName.split(" ")).toList().forEach(word ->
                    stringBuilder.append(word.substring(0, 1).toUpperCase())
                            .append(word.substring(1))
                            .append(" ")
            );
        } catch (Exception e) {
            System.out.println("Error while checking player name length: " + e.getMessage());
        }

        playerName = stringBuilder.toString();

        if(playerName.length() > maxLength) {
            playerName = playerName.substring(0, maxLength) + "...";
        }
        return playerName;
    }

    public static void disableGridPaneButton(GridPane parentGridPane, Node node) {
        parentGridPane.getChildren().stream()
                .filter(n -> n.equals(node))
                .forEach(n -> n.setDisable(true));
    }

    public static void disableAllGridPaneButtons(GridPane parentGridPane) {
        parentGridPane.getChildren()
                .forEach(node -> node.setDisable(true));
    }

    public static void disableAllGridPaneButtons(GridPane mainGridPane, String parentGridPaneFxID) {
        GridPane parentGridPane = (GridPane) mainGridPane.lookup("#" + parentGridPaneFxID);
        parentGridPane.getChildren().forEach(node -> node.setDisable(true));
    }


    public static void enableGridPaneButton(GridPane parentGridPane, Node node) {
        parentGridPane.getChildren().stream()
                .filter(n -> n.equals(node))
                .forEach(n -> n.setDisable(false));
    }

    public static void enableAllGridPaneButtons(GridPane parentGridPane) {
        parentGridPane.getChildren().forEach(node -> node.setDisable(false));
    }

    public static void enableAllGridPaneButtons(GridPane mainGridPane, String parentGridPaneFxID) {
        GridPane parentGridPane = (GridPane) mainGridPane.lookup("#" + parentGridPaneFxID);
        parentGridPane.getChildren().forEach(node -> node.setDisable(false));
    }

    public static void selectSubGridPane(GridPane mainGridPane, GridPane selectedGridPane) {
        mainGridPane.getChildren().forEach(node -> {
            if (node instanceof GridPane) {
                ((GridPane) node).getStyleClass().remove("clickableNodePressed");
            }
        });
        selectedGridPane.getStyleClass().add("clickableNodePressed");
    }

    public static void selectSubGridPane(GridPane mainGridPane, String selectedGridPaneFxID) {
        Objects.requireNonNull(mainGridPane.getChildren().stream()
                        .filter(node -> node.getId().equals(selectedGridPaneFxID))
                        .findFirst()
                        .orElse(null))
                .getStyleClass().add("clickableNodePressed");
    }

    public static void deselectAllSubGridPanes(GridPane gridPane) {
        gridPane.getChildren().forEach(node -> {
            if (node instanceof GridPane) {
                ((GridPane) node).getStyleClass().remove("clickableNodePressed");
            }
        });
    }

    public static void deselectAllSubGridPanes(GridPane parentGridPane, String fxID) {
        GridPane gridPane = (GridPane) parentGridPane.lookup("#" + fxID);

        gridPane.getChildren().forEach(node -> {
            if (node instanceof GridPane) {
                ((GridPane) node).getStyleClass().remove("clickableNodePressed");
            }
        });
    }

    public static List<Object> returnSelectedNodes(GridPane parentGridPane, String selectedObjectClass) {
        return returnSelectedNodesSub(selectedObjectClass, parentGridPane);
    }

    public static List<Object> returnSelectedNodes(GridPane mainAnchorPane, String parentGridPaneFxID, String selectedObjectClass) {
        GridPane parentGridPane = (GridPane) mainAnchorPane.lookup("#" + parentGridPaneFxID);
        return returnSelectedNodesSub(selectedObjectClass, parentGridPane);
    }

    @NotNull
    public static List<Object> returnSelectedNodesSub(String selectedObjectClass, GridPane parentGridPane) {

        try {
            GridPane selectedNode = (GridPane) returnNodeByClass(parentGridPane, selectedObjectClass);
            String nodeId = selectedNode.getId();

            return Arrays.asList(nodeId, selectedNode);
        }
        catch (NullPointerException e) {
            System.out.println("returnSelectedNodesSub *FUNCTION* -> No node found");
            return new ArrayList<>();
        }

    }

    public static Node returnNodeByClass(GridPane parentGridPane, String objectClass) {
        return parentGridPane.getChildren().stream()
                .filter(node -> node.getStyleClass().stream().anyMatch(styleClass -> styleClass.equals(objectClass)))
                .findFirst()
                .orElse(null);
    }

    public static Node returnChildNodeById(GridPane parentGridPane, String childGridPaneFxId) {
        return parentGridPane.getChildren().stream().filter(node -> node.getId().equals(childGridPaneFxId))
                .map(node -> (GridPane) node)
                .findFirst()
                .orElse(null);
    }


    public static void shakeEffect(Node node, Runnable onFinishedFunc) {
        Timeline nodeTimeline = new Timeline();
        double timeStamp = 0.6;
        double multiplier = 0.5;
        double maxVal = 2.5;
        double increment = 0.1;
        List<KeyFrame> keyFrames = new ArrayList<>();

        for (double x = timeStamp; x < maxVal; x += increment) {
            int y = (int) ((Math.sin(13 * x) * 12) / x);
            KeyValue nodeKeyValueQ = new KeyValue(node.translateXProperty(),  y, Interpolator.EASE_IN);
            KeyFrame nodeKeyFrameQ = new KeyFrame(Duration.seconds(x * multiplier), nodeKeyValueQ);

            keyFrames.add(nodeKeyFrameQ);
        }

        KeyValue nodeKeyValueS = new KeyValue(node.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame nodeKeyFrameS = new KeyFrame(Duration.seconds((maxVal + increment) * multiplier), nodeKeyValueS);
        keyFrames.add(nodeKeyFrameS);

        nodeTimeline.getKeyFrames().addAll(keyFrames);
        nodeTimeline.setCycleCount(1);

        nodeTimeline.play();
        nodeTimeline.setOnFinished(event -> onFinishedFunc.run());
    }

    public static void animateAttack(GridPane mainGridPane, Node attackingNode, Node attackedNode,
                                     String imgName, int imgHeight, int imgWidth, int imgColumn, int imgRow, boolean rotate,
                                     boolean isTranslated, Runnable beforeFinishRunnable, double beforeFinishTimeStampPercent,
                                     Runnable onFinishedFunc) {
        ImageView imageView = returnObjectImageView(imgName, imgHeight, imgWidth, 1);
        if(rotate) imageView.setRotate(180);

        mainGridPane.add(imageView, imgColumn, imgRow);

        Bounds startingBounds = attackingNode.localToScene(attackingNode.getBoundsInLocal());
        Bounds endingBounds = attackedNode.localToScene(attackedNode.getBoundsInLocal());

        double boundsXDiff = endingBounds.getCenterX() - startingBounds.getCenterX();
        double boundsYDiff = endingBounds.getCenterY() - startingBounds.getCenterY();

        Timeline nodeTimelineX = translationEffect(isTranslated, imageView, boundsXDiff, boundsYDiff,
                1, beforeFinishRunnable, beforeFinishTimeStampPercent,
                1, false);

        nodeTimelineX.setOnFinished(event -> {
            onFinishedFunc.run();
            mainGridPane.getChildren().remove(imageView);
        });
    }

    @NotNull
    public static Timeline translationEffect(boolean isTranslated, Node node,
                                             double boundsXDiff, double boundsYDiff,
                                             double timeStamp, Runnable beforeFinishRunnable, double beforeFinishTimeStampPercent,
                                             int cycleCount, boolean autoReverse) {

        Timeline nodeTimelineX = new Timeline();
        Timeline nodeTimelineY = new Timeline();

        KeyFrame nodeKeyFrameX = new KeyFrame(Duration.seconds(timeStamp * beforeFinishTimeStampPercent), event -> beforeFinishRunnable.run());

        KeyValue nodeKeyValueX2 = new KeyValue(node.translateXProperty(), boundsXDiff, Interpolator.EASE_IN);
        KeyFrame nodeKeyFrameX2 = new KeyFrame(Duration.seconds(timeStamp), nodeKeyValueX2);

        nodeTimelineX.getKeyFrames().addAll(nodeKeyFrameX, nodeKeyFrameX2);

        if(isTranslated) {
            double endY = chooseRandomDouble(new double[]{-1, 1});
            double power = 0.006;

            for (int x = 0; x < Math.abs(boundsXDiff); x++) {
                double y = endY * Math.exp(x * power);
                KeyValue nodeKeyValueY = new KeyValue(node.translateYProperty(), y, Interpolator.EASE_IN);
                KeyFrame nodeKeyFrameY = new KeyFrame(Duration.seconds((timeStamp / Math.abs(boundsXDiff)) * x), nodeKeyValueY);

                nodeTimelineY.getKeyFrames().add(nodeKeyFrameY);
            }

        }
        else {
            KeyValue nodeKeyValueY = new KeyValue(node.translateYProperty(), boundsYDiff, Interpolator.EASE_IN);
            KeyFrame nodeKeyFrameY = new KeyFrame(Duration.seconds(timeStamp), nodeKeyValueY);
            nodeTimelineY.getKeyFrames().add(nodeKeyFrameY);
        }




        nodeTimelineX.setCycleCount(cycleCount);
        nodeTimelineY.setCycleCount(cycleCount);

        nodeTimelineX.setAutoReverse(autoReverse);
        nodeTimelineY.setAutoReverse(autoReverse);

        Thread threadX = new Thread(nodeTimelineX::play);
        Thread threadY = new Thread(nodeTimelineY::play);

        threadX.start();
        threadY.start();
        return nodeTimelineX;
    }

    public static void parryAnimation(boolean attackerAttackSucceeded, GridPane attackingCharacterParentGridPane,
                                      GridPane attackingCharacterGridPane, GridPane attackedCharacterGridPane,
                                      GridPane attackedCharacterParentGridPane,
                                      String imgName, int imgHeight, int imgWidth, AbstractCharacter attackingCharacter,
                                      Runnable onEndFunc) {

        ImageView shield = returnObjectImageView("shield", 100,220, 0.2);
        boolean rotateInitialAttack;
        boolean rotateParry;

        if(attackingCharacter instanceof Enemy) {
            rotateInitialAttack = ((Enemy) attackingCharacter).getEnemyName().getEnemyCombat().equals(EnemyCombat.SPELL);
            rotateParry = false;
        }
        else {
            rotateInitialAttack = false;
            rotateParry = true;
        }

        animateAttack(attackingCharacterParentGridPane,
                attackingCharacterGridPane,
                attackedCharacterGridPane,
                imgName,
                imgHeight,
                imgWidth,
                GridPane.getColumnIndex(attackingCharacterGridPane),
                GridPane.getRowIndex(attackingCharacterGridPane),
                rotateInitialAttack,
                !attackerAttackSucceeded,
                () -> {
                    if(attackerAttackSucceeded) {
                        int columnIndex = GridPane.getColumnIndex(attackedCharacterGridPane);
                        int rowIndex = GridPane.getRowIndex(attackedCharacterGridPane);

                        attackedCharacterParentGridPane.add(shield, columnIndex, rowIndex);
                    }
                },
                0.1,
                () -> {
                    if(attackerAttackSucceeded) animateAttack(attackedCharacterParentGridPane,
                            attackedCharacterGridPane,
                            attackingCharacterGridPane,
                            "FollowUp Spell",
                            100,
                            200,
                            GridPane.getColumnIndex(attackedCharacterGridPane),
                            GridPane.getRowIndex(attackedCharacterGridPane),
                            rotateParry,
                            false,
                            () -> {},
                            0.1,
                            () -> {
                                shakeEffect(attackingCharacterGridPane, onEndFunc);
                                attackedCharacterParentGridPane.getChildren().remove(shield);
                            }
                    );
                });
    }

    public static void dodgeAnimation(boolean attackerAttackSucceeded, GridPane attackingCharacterParentGridPane,
                                      GridPane attackingCharacterGridPane, GridPane attackedCharacterGridPane,
                                      String imgName, int imgHeight, int imgWidth, boolean rotate) {
        animateAttack(attackingCharacterParentGridPane,
                attackingCharacterGridPane,
                attackedCharacterGridPane,
                imgName,
                imgHeight,
                imgWidth,
                GridPane.getColumnIndex(attackingCharacterGridPane),
                GridPane.getRowIndex(attackingCharacterGridPane),
                rotate,
                !attackerAttackSucceeded,
                () -> {
                    double randomDodge = chooseRandomDouble(new double[]{-1, 1});

                    if(attackerAttackSucceeded) {
                        translationEffect(false, attackedCharacterGridPane, 100, randomDodge * 200,
                                1, () -> {}, 0,
                                2, true);
                    }
                },
                0.1,
                () ->{});
    }

    public static void attackAnimation(boolean attackingCharacterSucceeded, GridPane attackingCharacterParentGridPane,
                                 GridPane attackingCharacterGridPane, GridPane attackedCharacterGridPane,
                                 String imgName, int imgHeight, int imgWidth, boolean rotate, Runnable onEndFunc) {
        animateAttack(attackingCharacterParentGridPane,
                attackingCharacterGridPane,
                attackedCharacterGridPane,
                imgName,
                imgHeight,
                imgWidth,
                GridPane.getColumnIndex(attackingCharacterGridPane),
                GridPane.getRowIndex(attackingCharacterGridPane),
                rotate,
                !attackingCharacterSucceeded,
                () -> {},
                0,
                () -> {
                    if(attackingCharacterSucceeded) shakeEffect(attackedCharacterGridPane, onEndFunc);
                });
    }


}
