package project.Main;

import project.Functions.ConsoleFunctions;
import project.Functions.SaveFunctions;

import static project.Classes.Color.returnColoredText;
import static java.util.Arrays.stream;
import static project.Functions.GeneralFunctions.findAllClasses;

public class Main {
    public static void main(String[] args) {
//        System.out.println(findAllClasses("project/Classes"));
        ConsoleFunctions.printMainScreen();
        SaveFunctions.checkSaves();
        ConsoleFunctions.chooseAction();
    }
}