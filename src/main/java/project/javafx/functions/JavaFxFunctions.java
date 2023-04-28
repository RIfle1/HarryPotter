package project.javafx.functions;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import project.abstractClasses.AbstractCharacter;
import project.classes.Enemy;
import project.classes.Wizard;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

import static project.enums.EnumMethods.returnFormattedEnum;
import static project.functions.GeneralFunctions.returnFileAttribute;
import static project.functions.SaveFunctions.createWizardInstance;

public class JavaFxFunctions {

    public static void sendToScene(ActionEvent event, FXMLLoader FXMLLoader) {
        Scene scene;

        try {
            scene = new Scene(FXMLLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    public static Optional<ButtonType> createPopup(ActionEvent event, Alert.AlertType alertType, String popUpMsg) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

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

        Text wizardNameText = new Text(wizardName);
        Text wizardLevelText = new Text("Level " + wizardLevel);
        Text wizardHouseText = new Text("House of " + wizardHouse);
        Text fileCreationTimeText = new Text("Creation Time: " + fileCreationTime);
        Text fileModifiedTimeText = new Text("Last Modified Time: " + fileModifiedTime);

        saveInfoGridPane.add(wizardNameText, 0, 0);
        saveInfoGridPane.add(wizardLevelText, 0, 1);
        saveInfoGridPane.add(wizardHouseText, 0, 2);
        saveInfoGridPane.add(fileCreationTimeText, 0, 3);
        saveInfoGridPane.add(fileModifiedTimeText, 0, 4);

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
            throw new RuntimeException(e);
        }
        return new Image(imgInputStream);
    }

    public static GridPane returnCharacterGridPane(AbstractCharacter abstractCharacter) {
        GridPane horizontalGridPane = new GridPane();
        horizontalGridPane.setAlignment(Pos.CENTER);
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        horizontalGridPane.getColumnConstraints().addAll(column1, column2);

        GridPane verticalGridPane = new GridPane();
        verticalGridPane.setAlignment(Pos.CENTER);
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        verticalGridPane.getRowConstraints().addAll(row1, row2);

        verticalGridPane.add(horizontalGridPane, 0, 0);

        Text characterNameText = new Text(abstractCharacter.getName());
        ImageView characterImageView;
        if(abstractCharacter.getClass().equals(Wizard.class)) {
            characterImageView = returnObjectImageView("hagrid", 20, 20);
        }
        else {
            characterImageView = returnObjectImageView(returnFormattedEnum(((Enemy) abstractCharacter).getEnemyName()), 20, 20);
        }

        horizontalGridPane.add(characterImageView, 0, 0);
        horizontalGridPane.add(characterNameText, 1, 0);

        ProgressBar characterHealthBar = new ProgressBar();
        characterHealthBar.setProgress(abstractCharacter.getHealthPoints() / abstractCharacter.getMaxHealthPoints());

        verticalGridPane.add(characterHealthBar, 0, 1);
        verticalGridPane.setId(abstractCharacter.getName());

        return verticalGridPane;
    }

}
