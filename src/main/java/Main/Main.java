package Main;

import Classes.Potion;
import Classes.Wizard;

import java.util.Arrays;

import static Classes.Wizard.wizard;
import static Functions.GeneralFunctions.returnStringSettersList;
import static Functions.SaveFunctions.*;

public class Main {
    public static void main(String[] args) {
//        gameCredits();
//        checkSaves();
//        CharacterCreation.characterInit();
//        saveWizard();
//        chooseAction();
//        System.out.println(enemiesHashMap.get(enemiesKeyList.get(0)).getExperiencePoints());
//        unlockNextLevel(Level.The_Philosophers_Stone);
//        wizard.updateStats();
//        System.out.println(getStringGettersList(AbstractCharacter.class));
//        System.out.println(getStringGettersList(Wizard.class));
//        saveObject(Wizard.class, wizard);
        wizard.updateStats();
//        System.out.println(findAllClasses("AbstractClasses"));
        wizard.setItemList(Arrays.asList(Potion.highCooldownPotion, Potion.highCooldownPotion,Potion.highCooldownPotion, Potion.highCooldownPotion,Potion.highCooldownPotion, Potion.highCooldownPotion));
//        saveWizard();
//        saveProgress("save1");
        loadProgress("save1");
//        System.out.println(getJSONEnum(Level.returnAllLevels()).toJSONString());
//        System.out.println(returnSaveFiles("saves"));
    }
}