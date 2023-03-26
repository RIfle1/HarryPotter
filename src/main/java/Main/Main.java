package Main;

import Enums.Level;

import static Classes.Wizard.testWizard;
import static Classes.Wizard.wizard;
import static Enums.Level.unlockNextLevel;
import static Main.ConsoleFunctions.chooseAction;
import static Main.ConsoleFunctions.chooseLevel;

public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {
//        gameCredits();
//        checkSaves();
//        CharacterCreation.characterInit();
        wizard.setSpecPoints(10);
        chooseAction();
//        unlockNextLevel(Level.The_Philosophers_Stone);
    }
}