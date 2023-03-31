package project.main;

import static project.classes.Color.returnColoredText;
import static java.util.Arrays.stream;
import static project.classes.Wizard.wizard;
import static project.functions.GeneralFunctions.findAllClasses;
import static project.functions.SaveFunctions.loadObject;
import static project.functions.SaveFunctions.saveObject;

public class Main {
    public static void main(String[] args) {
        wizard.setExperience(10000);
        wizard.updateStats();
//        saveObject("test", wizard);
        loadObject("test", wizard);

//        System.out.println(findAllClasses("project/classes"));

        System.out.println(wizard.getName());
//        loadObject("test", Level.The_Chamber_of_Secrets);
//        ConsoleFunctions.printMainScreen();
//        SaveFunctions.checkSaves();
//        ConsoleFunctions.chooseAction();
    }


}