package Main;

import static Main.ConsoleFunctions.gameCredits;
import static Main.SaveChecker.checkSaves;

public class Main {
    public static void main(String[] args) {
        gameCredits();
        checkSaves();
        CharacterCreation.characterInit();
    }
}