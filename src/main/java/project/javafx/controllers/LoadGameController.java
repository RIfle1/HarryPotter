package project.javafx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import project.classes.Wizard;
import project.functions.SaveFunctions;
import project.javafx.GuiMain;

import java.io.FileReader;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import static javafx.scene.layout.GridPane.setHalignment;
import static project.enums.EnumMethods.returnFormattedEnum;
import static project.functions.GeneralFunctions.returnFileDate;
import static project.functions.SaveFunctions.loadClass;
import static project.functions.SaveFunctions.returnSaves;
import static project.javafx.JavaFxFunctions.sendToScene;
import static project.javafx.controllers.GameMenuController.gameMenuScene;
import static project.javafx.controllers.MainMenuController.mainMenuScene;

public class LoadGameController implements Initializable {
    @FXML
    private GridPane loadGameGrid;

    @FXML
    void onKeyPressed(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ESCAPE)) {
            backOnClick(new ActionEvent(event.getSource(), event.getTarget()));
        }
    }

    @FXML
    void backOnClick(ActionEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        mainMenuScene(stage);
    }

    public static void loadGameScene(MouseEvent event) {
        FXMLLoader FXMLLoader = new FXMLLoader(GuiMain.class.getResource("LoadGame.fxml"));
        ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());
        sendToScene(actionEvent, FXMLLoader);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> saveFiles = SaveFunctions.returnFormattedSaveFiles();
        AtomicInteger index = new AtomicInteger();

        saveFiles.forEach(saveFile -> {
            GridPane saveInfoGridPane = returnSaveInfoGridPane(saveFile);
            
            saveInfoGridPane.onMouseReleasedProperty().set(mouseEvent -> {
                SaveFunctions.loadProgress(saveFile);
                ActionEvent actionEvent = new ActionEvent(mouseEvent.getSource(), mouseEvent.getTarget());
                gameMenuScene(actionEvent);
            });

            loadGameGrid.add(saveInfoGridPane, 0, index.get());
            index.getAndIncrement();

            loadGameGrid.setVgap(40);
        });
    }

    public static void createWizardInstance(String filename, Wizard saveWizard) {
        JSONParser parser = new JSONParser();
        JSONObject wizardJSONObject = null;
        List<String> saves = returnSaves(filename);

        try {
            wizardJSONObject = (JSONObject) parser.parse(new FileReader("saves/" + saves.get(0)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadClass(Wizard.class, saveWizard, (JSONObject) wizardJSONObject);
    }

    public static GridPane returnSaveInfoGridPane(String filename) {
        GridPane saveInfoGridPane = new GridPane();


        Wizard saveWizard = Wizard.builder().build();
        createWizardInstance(filename, saveWizard);

        String fileDate = returnFileDate("saves", filename);
        String wizardName = saveWizard.getName();
        String wizardLevel = String.valueOf(saveWizard.getLevel());
        String wizardHouse = returnFormattedEnum(saveWizard.getHouseName());

        Text fileDateText = new Text(fileDate);
        Text wizardNameText = new Text(wizardName);
        Text wizardLevelText = new Text(wizardLevel);
        Text wizardHouseText = new Text(wizardHouse);

        saveInfoGridPane.add(fileDateText, 0, 0);
        saveInfoGridPane.add(wizardNameText, 0, 1);
        saveInfoGridPane.add(wizardLevelText, 0, 2);
        saveInfoGridPane.add(wizardHouseText, 0, 3);

        saveInfoGridPane.getStyleClass().add("saveInfoItemText");

        setHalignment(saveInfoGridPane, HPos.CENTER);

        return saveInfoGridPane;

    }
}
