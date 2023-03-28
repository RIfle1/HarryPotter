package Functions;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.FileWriter;
import java.io.IOException;

import static Classes.Wizard.wizard;
import static Functions.ConsoleFunctions.*;

public class SaveFunctions {
    public static void checkSaves() {
        // Check if there are any previous characters
        String newCharacterText = "No saved characters have been found, new character creation will now proceed.";
        printColoredHeader(newCharacterText);
        continuePrompt();
    }

    public static void saveWizard() {
        try {
            FileWriter file = new FileWriter("data.json");
            file.write(wizard.toJsonObject().toJSONString());
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
