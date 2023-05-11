package project.javafx.functions;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static project.enums.EnumMethods.returnFormattedEnum;
import static project.functions.GeneralFunctions.returnFileAttribute;
import static project.functions.SaveFunctions.createWizardInstance;

public class JavaFxFunctions {

    public static void sendToScene(ActionEvent event, FXMLLoader FXMLLoader) {
        Scene scene = getScene(FXMLLoader);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    public static void sendToScene(Stage stage, FXMLLoader FXMLLoader) {
        Scene scene = getScene(FXMLLoader);

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


        Wizard wizardInstance = Wizard.builder().build();
        createWizardInstance(filename, wizardInstance);

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

    public static ImageView returnObjectImageView(String objectName, double height, double width) {
        Image spellImage = returnObjectImage(objectName);
        ImageView spellImageView = new ImageView(spellImage);
        spellImageView.setFitHeight(height);
        spellImageView.setFitWidth(width);


        return spellImageView;
    }

    public static Image returnObjectImage(String objectName) {
        FileInputStream imgInputStream = null;
        try {
            imgInputStream = new FileInputStream("src/main/resources/icons/" + objectName + ".png");
        } catch (FileNotFoundException e) {
            System.out.println("Image not found");
            try {
                imgInputStream = new FileInputStream("src/main/resources/icons/unknown.png");
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
        return new Image(imgInputStream);
    }

    public static GridPane generateCharacterGridPane(AbstractCharacter abstractCharacter) {

        // CHARACTER NAME AND PROFILE PIC GRID PANE
        GridPane horizontalGridPane = new GridPane();
        horizontalGridPane.setAlignment(Pos.CENTER);
        ColumnConstraints horizontalGridPaneColumn1 = new ColumnConstraints();
        ColumnConstraints horizontalGridPaneColumn2 = new ColumnConstraints();

        horizontalGridPaneColumn1.setHalignment(HPos.CENTER);
        horizontalGridPaneColumn2.setHalignment(HPos.CENTER);

        horizontalGridPaneColumn1.setMaxWidth(Region.USE_COMPUTED_SIZE);
        horizontalGridPaneColumn1.setMinWidth(Region.USE_COMPUTED_SIZE);
        horizontalGridPaneColumn1.setPrefWidth(50);

        horizontalGridPaneColumn2.setMaxWidth(Region.USE_COMPUTED_SIZE);
        horizontalGridPaneColumn2.setMinWidth(Region.USE_COMPUTED_SIZE);
        horizontalGridPaneColumn2.setPrefWidth(100);

        horizontalGridPane.getColumnConstraints().addAll(horizontalGridPaneColumn1, horizontalGridPaneColumn2);


//        horizontalGridPane.setGridLinesVisible(true);

        // CHARACTER MAIN GRID PANE WITH INFO AT THE TOP AND HEALTH BAR AT THE BOTTOM
        GridPane verticalGridPane = new GridPane();
        verticalGridPane.setAlignment(Pos.CENTER);
        RowConstraints verticalGridPaneRow1 = new RowConstraints();
        RowConstraints verticalGridPaneRow2 = new RowConstraints();
        ColumnConstraints verticalGridPaneColumn1 = new ColumnConstraints();

        verticalGridPaneColumn1.setMaxWidth(Region.USE_PREF_SIZE);
        verticalGridPaneColumn1.setMinWidth(Region.USE_PREF_SIZE);
        verticalGridPaneColumn1.setPrefWidth(150);

        verticalGridPaneColumn1.setHgrow(Priority.ALWAYS);
        verticalGridPaneColumn1.setHalignment(HPos.CENTER);

        verticalGridPaneRow2.setMaxHeight(Region.USE_PREF_SIZE);
        verticalGridPaneRow2.setMinHeight(Region.USE_PREF_SIZE);
        verticalGridPaneRow2.setPrefHeight(25);

        verticalGridPaneRow2.setVgrow(Priority.ALWAYS);
        verticalGridPaneRow2.setValignment(VPos.CENTER);

        verticalGridPane.getRowConstraints().addAll(verticalGridPaneRow1, verticalGridPaneRow2);
        verticalGridPane.getColumnConstraints().addAll(verticalGridPaneColumn1);

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

        ImageView characterImageView = returnObjectImageView(characterName, 50, 50);

        Text characterNameText = new Text(characterNameString);
        characterNameText.setWrappingWidth(100);
        characterNameText.getStyleClass().add("characterGridPaneText");

        horizontalGridPane.add(characterImageView, 0, 0);
        horizontalGridPane.add(characterNameText, 1, 0);

        ProgressBar characterHealthBar = returnCharacterProgressBar(abstractCharacter.getHealthPoints() / abstractCharacter.getMaxHealthPoints());

        verticalGridPane.add(characterHealthBar, 0, 1);
        verticalGridPane.setId(abstractCharacter.getName());

        return verticalGridPane;
    }


    private static ProgressBar returnCharacterProgressBar(double progressBarValue) {
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

        Arrays.stream(playerName.split(" ")).toList().forEach(word ->
                stringBuilder.append(word.substring(0, 1).toUpperCase())
                        .append(word.substring(1))
                        .append(" ")
        );

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

    public static List<Object> returnSelectedNodes(AnchorPane mainAnchorPane, String parentGridPaneFxID, String selectedObjectClass) {
        GridPane parentGridPane = (GridPane) mainAnchorPane.lookup("#" + parentGridPaneFxID);
        return returnSelectedNodesSub(selectedObjectClass, parentGridPane);
    }

    @NotNull
    public static List<Object> returnSelectedNodesSub(String selectedObjectClass, GridPane parentGridPane) {

        GridPane selectedNode = (GridPane) returnNodeByClass(parentGridPane, selectedObjectClass);
        String nodeId = selectedNode.getId();

        return Arrays.asList(nodeId, selectedNode);
    }

    public static Node returnNodeByClass(GridPane parentGridPane, String objectClass) {
        return parentGridPane.getChildren().stream()
                .filter(node -> node.getStyleClass().stream().anyMatch(styleClass -> styleClass.equals(objectClass)))
                .findFirst()
                .orElse(null);
    }

    public static Node returnChildNodeById(GridPane parentGridPane, String childGridPaneFxId) {
        return parentGridPane.lookup("#" + childGridPaneFxId);
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

    public static void translationEffect(Node attackedNode, ImageView imageViewNode, double startX, double startY, double endX, double endY, Runnable onFinishedFunc) {
        double timeStamp = 1;

        Timeline nodeTimelineX = new Timeline();
        Timeline nodeTimelineY = new Timeline();

        KeyValue nodeKeyValueX = new KeyValue(imageViewNode.translateXProperty(), endX - startX, Interpolator.EASE_IN);
        KeyValue nodeKeyValueY = new KeyValue(imageViewNode.translateYProperty(), endY - startY, Interpolator.EASE_IN);

        KeyFrame nodeKeyFrameX = new KeyFrame(Duration.seconds(timeStamp), nodeKeyValueX);
        KeyFrame nodeKeyFrameY = new KeyFrame(Duration.seconds(timeStamp), nodeKeyValueY);

        nodeTimelineX.getKeyFrames().add(nodeKeyFrameX);
        nodeTimelineY.getKeyFrames().add(nodeKeyFrameY);
        nodeTimelineX.setCycleCount(1);
        nodeTimelineY.setCycleCount(1);

        Thread threadX = new Thread(nodeTimelineX::play);
        Thread threadY = new Thread(nodeTimelineY::play);

        threadX.start();
        threadY.start();

        nodeTimelineX.setOnFinished(event -> {
            shakeEffect(attackedNode, onFinishedFunc);
            ((GridPane) imageViewNode.getParent()).getChildren().remove(imageViewNode);
        });
    }


}
