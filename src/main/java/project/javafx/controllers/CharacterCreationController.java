package project.javafx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.classes.Wand;
import project.enums.*;
import project.javafx.GuiMain;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static project.functions.CharacterCreation.characterInit;
import static project.functions.GeneralFunctions.checkString;
import static project.javafx.functions.JavaFxFunctions.sendToScene;
import static project.javafx.controllers.GameMenuController.gameMenuScene;
import static project.javafx.controllers.MainMenuController.mainMenuScene;

public class CharacterCreationController implements Initializable {
    @FXML
    private TextField firstNameTf;
    @FXML
    private TextField lastNameTf;
    @FXML
    private ChoiceBox<String> genderCb;
    @FXML
    private ChoiceBox<String> petCb;
    @FXML
    private ChoiceBox<String> wandCoreCb;
    @FXML
    private ChoiceBox<Integer> wandSizeCb;
    @FXML
    private ChoiceBox<String> houseCb;
    @FXML
    private ChoiceBox<String> difficultyCb;
    @FXML
    private Text ccErrorT;


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

    @FXML
    void createOnClick(ActionEvent actionEvent) {
        String firstName = firstNameTf.getText();
        String lastName = lastNameTf.getText();
        Gender gender = Gender.setGender(genderCb.getValue());
        Pet pet = Pet.setPet(petCb.getValue());
        Wand wand = new Wand(Core.setCore(wandCoreCb.getValue()), wandSizeCb.getValue());
        HouseName houseName = HouseName.setHouseName(houseCb.getValue());
        Difficulty difficulty = Difficulty.setDifficulty(difficultyCb.getValue());

        if(checkString(firstName) && checkString(lastName)) {
            ccErrorT.setVisible(false);
            characterInit(firstName, lastName, gender, pet, houseName, wand, difficulty);
            gameMenuScene(actionEvent);
        }
        else {
            ccErrorT.setVisible(true);
        }
    }

    public static void characterCreationScene(MouseEvent event) {
        FXMLLoader characterCreationFxmlLoader = new FXMLLoader(GuiMain.class.getResource("CharacterCreation.fxml"));
        ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());
        sendToScene(actionEvent, characterCreationFxmlLoader);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genderCb.getItems().addAll(Gender.getGenderList());
        genderCb.setValue(Gender.getGenderList().get(0));

        petCb.getItems().addAll(Pet.getPetList());
        petCb.setValue(Pet.getPetList().get(0));

        wandCoreCb.getItems().addAll(Core.getCoreList());
        wandCoreCb.setValue(Core.getCoreList().get(0));


        List<Integer> wandSizeList = new ArrayList<>();
        for(int i = 10; i <= 27; i++) {
            wandSizeList.add(i);
        }
        wandSizeCb.getItems().addAll(wandSizeList);
        wandSizeCb.setValue(wandSizeList.get(0));

        houseCb.getItems().addAll(HouseName.getHouseNameList());
        houseCb.setValue(HouseName.getHouseNameList().get(0));

        difficultyCb.getItems().addAll(Difficulty.getDifficultyList());
        difficultyCb.setValue(Difficulty.getDifficultyList().get(0));
    }
}

