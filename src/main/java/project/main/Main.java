package project.main;

import project.functions.ConsoleFunctions;
import project.functions.GeneralFunctions;
import project.functions.SaveFunctions;

public class Main {
    public static void main(String[] args) {
        playGame();
    }

    public static void playGame() {
        ConsoleFunctions.printMainScreen();
        SaveFunctions.checkSaves();
        ConsoleFunctions.chooseAction();
    }



}