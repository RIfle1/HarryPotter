package Main;

import Classes.Potion;
import Functions.CharacterCreation;

import java.util.Arrays;

import static Classes.Wizard.wizard;
import static Functions.ConsoleFunctions.chooseAction;
import static Functions.SaveFunctions.*;
import static java.util.Arrays.stream;

public class Main {
    public static void main(String[] args) {
//        gameCredits();
//        checkSaves();
//        CharacterCreation.characterInit();
//        saveWizard();
        try {
            wizard.setPotionList(Arrays.asList(Potion.minorDefensePotion.clone(), Potion.minorDefensePotion.clone()));
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
//        System.out.println(wizard.getPotionList());
        wizard.setSpecPoints(12);
        chooseAction();
//        System.out.println(enemiesHashMap.get(enemiesKeyList.get(0)).getExperiencePoints());
//        unlockNextLevel(Level.The_Philosophers_Stone);
//        wizard.updateStats();
//        System.out.println(getStringGettersList(AbstractCharacter.class));
//        System.out.println(getStringGettersList(Wizard.class));
//        saveObject(Wizard.class, wizard);
//        wizard.updateStats();
//        System.out.println(findAllClasses("AbstractClasses"));
//        wizard.setPotionList(Arrays.asList(Potion.highCooldownPotion, Potion.highCooldownPotion,Potion.highCooldownPotion, Potion.highCooldownPotion,Potion.highCooldownPotion, Potion.highCooldownPotion));

//        saveProgress("save1");
//        loadProgress("save1");

//        System.out.println(wizard.getItemList().get(0).getItemName());

//        System.out.println(wizard.getSpellsKeyList());
//        System.out.println(getJSONEnum(Level.returnAllLevels()).toJSONString());
//        System.out.println(returnSaveFiles("saves"));
    }
}