package project.Main;

import project.Functions.ConsoleFunctions;
import project.Functions.SaveFunctions;

import static project.Classes.Color.returnColoredText;
import static java.util.Arrays.stream;

public class Main {
    public static void main(String[] args) {
        ConsoleFunctions.printMainScreen();
        SaveFunctions.checkSaves();
        ConsoleFunctions.chooseAction();
    }
}