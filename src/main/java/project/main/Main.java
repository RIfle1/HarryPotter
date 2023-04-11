package project.main;

import project.functions.ConsoleFunctions;
import project.functions.SaveFunctions;

import static project.classes.Color.returnColoredText;
import static java.util.Arrays.stream;
import static project.classes.Wizard.wizard;
import static project.functions.GeneralFunctions.findAllClasses;
import static project.functions.SaveFunctions.loadObject;
import static project.functions.SaveFunctions.saveObject;

public class Main {
    public static void main(String[] args) {
        ConsoleFunctions.printMainScreen();
        SaveFunctions.checkSaves();
        ConsoleFunctions.chooseAction();
    }


}