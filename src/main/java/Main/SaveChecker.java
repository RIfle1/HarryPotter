package Main;

import static Main.ConsoleFunctions.*;

public class SaveChecker {
    public static void checkSaves() {
        // Check if there are any previous characters
        String newCharacterText = "No saved characters have been found, new character creation will now proceed.";
        printColoredHeader(newCharacterText);
        continuePrompt();
    }
}
