package Main;

import static Main.ConsoleFunctions.continuePrompt;
import static Main.ConsoleFunctions.printHeader;

public class SaveChecker {
    public static void checkSaves() {
        // Check if there are any previous characters
        String newCharacterText = "No saved characters have been found, new character creation will now proceed.";
        printHeader(newCharacterText);
        continuePrompt();
    }
}
