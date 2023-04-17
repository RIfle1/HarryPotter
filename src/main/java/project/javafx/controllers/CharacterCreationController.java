package project.javafx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.classes.Wand;
import project.enums.*;
import project.javafx.GuiMain;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static project.functions.CharacterCreation.characterInit;
import static project.functions.GeneralFunctions.checkInput;
import static project.javafx.GuiMain.createScene;
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

    public void createOnClick(ActionEvent actionEvent) {
        String firstName = firstNameTf.getText();
        String lastName = lastNameTf.getText();
        Gender gender = Gender.setGender(genderCb.getValue());
        Pet pet = Pet.setPet(petCb.getValue());
        Wand wand = new Wand(Core.setCore(wandCoreCb.getValue()), wandSizeCb.getValue());
        HouseName houseName = HouseName.setHouseName(houseCb.getValue());
        Difficulty difficulty = Difficulty.setDifficulty(difficultyCb.getValue());

        if(checkInput(firstName) && checkInput(lastName)) {
            ccErrorT.setVisible(false);
            characterInit(firstName, lastName, gender, pet, houseName, wand, difficulty);
            gameMenuScene(actionEvent);
        }
        else {
            ccErrorT.setVisible(true);
        }
    }

    public void backOnClick(ActionEvent actionEvent) {
        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        mainMenuScene(stage);
    }

    public static void characterCreationScene(MouseEvent event) {
        FXMLLoader characterCreationFxmlLoader = new FXMLLoader(GuiMain.class.getResource("CharacterCreation.fxml"));
        createScene(event, characterCreationFxmlLoader, "generalStyles.css");
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

