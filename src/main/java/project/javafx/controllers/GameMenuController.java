package project.javafx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.classes.Spell;
import project.enums.Level;
import project.javafx.GuiMain;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import static javafx.scene.layout.GridPane.setHalignment;
import static project.classes.Wizard.wizard;
import static project.enums.EnumMethods.returnFormattedEnum;
import static project.functions.GeneralFunctions.checkPositiveInt;
import static project.javafx.JavaFxFunctions.createPopup;
import static project.javafx.JavaFxFunctions.sendToScene;
import static project.javafx.controllers.MainMenuController.mainMenuScene;

public class GameMenuController implements Initializable {
    @FXML
    private Text charismaT;

    @FXML
    private Text defenseT;

    @FXML
    private Text healthT;

    @FXML
    private Text houseT;

    @FXML
    private Text intelligenceT;

    @FXML
    private Text levelT;

    @FXML
    private Text luckT;

    @FXML
    private Text nameT;

    @FXML
    private Text strengthT;

    @FXML
    private Text specPointsT;

    @FXML
    private GridPane chooseLevelGrid;

    @FXML
    private GridPane spellGrid;

    @FXML
    private ChoiceBox<String> specCb;

    @FXML
    private TextField specTf;

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
    void upgradeOnClick(ActionEvent event) {
        String specPoints = specTf.getText();
        String spec = specCb.getValue();

        if(checkPositiveInt(specPoints)) {
            if(wizard.setWizardSpec(spec.toLowerCase(), Integer.parseInt(specPoints))) {
                updateWizardStats();
                specTf.clear();
                createPopup(event, Alert.AlertType.CONFIRMATION, spec + " has been upgraded by " + specPoints + " spec points.");
            }
            else {
                createPopup(event, Alert.AlertType.WARNING, "You have " + (int) wizard.getSpecPoints() + " spec points.");
            }

        }
        else {
            createPopup(event, Alert.AlertType.ERROR, "Input must be a positive integer.");
        }
    }

    @FXML
    void saveProgressOnClick(ActionEvent event) {
        FXMLLoader FXMLLoader = new FXMLLoader(GuiMain.class.getResource("SaveProgress.fxml"));
        sendToScene(event, FXMLLoader);
    }

    public static void gameMenuScene(ActionEvent event) {
        FXMLLoader FXMLLoader = new FXMLLoader(GuiMain.class.getResource("GameMenu.fxml"));
        sendToScene(event, FXMLLoader);
    }

    public void updateWizardStats() {
        // WIZARD STATS
        charismaT.setText(String.valueOf((int)wizard.getCharisma()));
        defenseT.setText(String.valueOf((int) wizard.getMaxDefensePoints()));
        healthT.setText(String.valueOf((int) wizard.getMaxHealthPoints()));
        houseT.setText(returnFormattedEnum(wizard.getHouseName()));
        intelligenceT.setText(String.valueOf((int) wizard.getIntelligence()));
        levelT.setText(String.valueOf((int) wizard.getLevel()));
        luckT.setText(String.valueOf((int) wizard.getLuck()));
        nameT.setText(wizard.getName());
        strengthT.setText(String.valueOf((int) wizard.getStrength()));
        specPointsT.setText(String.valueOf((int) wizard.getSpecPoints()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateWizardStats();

        // UNLOCKED LEVELS
        AtomicInteger index1 = new AtomicInteger();
        Level.returnAllUnlockedLevelsList().forEach(level -> {
            Text levelText = new Text(returnFormattedEnum(level));
            levelText.getStyleClass().add("infoItemTextHover");

            chooseLevelGrid.add(levelText, 0, index1.get() + 1);
            setHalignment(levelText, HPos.CENTER);

            index1.getAndIncrement();
        });
        chooseLevelGrid.setVgap(40);

        // SPELLS INFO
        HashMap<String, Spell> wizardSpells = wizard.getSpellsHashMap();
        AtomicInteger index2 = new AtomicInteger();
        wizardSpells.forEach((spellName, spell) -> {
            Text spellText = new Text(spellName);
            Text spellDamage = new Text( (int) spell.getSpellDamage()[0] +"~"+ (int) spell.getSpellDamage()[1]);
            Text spellChance = new Text(((int) spell.getSpellChance() * 100) + "%");
            Text spellCooldown = new Text(String.valueOf(spell.getSpellCooldown()));

            spellText.getStyleClass().add("infoItemText");
            spellDamage.getStyleClass().add("infoItemText");
            spellChance.getStyleClass().add("infoItemText");
            spellCooldown.getStyleClass().add("infoItemText");

            spellGrid.add(spellText, 0, index2.get() + 1);
            spellGrid.add(spellDamage, 1, index2.get() + 1);
            spellGrid.add(spellChance, 2, index2.get() + 1);
            spellGrid.add(spellCooldown, 3, index2.get() + 1);

            setHalignment(spellText, HPos.CENTER);
            setHalignment(spellDamage, HPos.CENTER);
            setHalignment(spellChance, HPos.CENTER);
            setHalignment(spellCooldown, HPos.CENTER);

            index2.getAndIncrement();
        });
        spellGrid.setVgap(40);

        //SPECS LIST
        List<String> specsList = wizard.returnSpecList();
        specCb.getItems().addAll(specsList);
        specCb.setValue(specsList.get(0));
    }
}
