package Functions;

import Classes.Wizard;
import Enums.Level;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static Classes.Wizard.wizard;
import static Functions.ConsoleFunctions.*;
import static Functions.GeneralFunctions.*;

public class SaveFunctions {
    public static void checkSaves() {
        // Check if there are any previous characters
        String newCharacterText = "No saved characters have been found, new character creation will now proceed.";
        printColoredHeader(newCharacterText);
        continuePrompt();
    }

    public static void saveObject(String JSONString, String fileName) {

        try {
            FileWriter file = new FileWriter("saves/" + fileName + ".json");
            file.write(JSONString);
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject getJSONObject(Class<?> c, Object object) {
        JSONObject jsonObject = getJSONObjectSub(c, object);

        for(Class<?> c1 : findAllClasses("AbstractClasses")) {
            if(c.getSuperclass().equals(c1)) {
                JSONObject jsonObject2 = getJSONObjectSub(c.getSuperclass(), object);
                jsonObject.putAll(jsonObject2);
            }
        }
        return jsonObject;
    }

    private static JSONObject getJSONObjectSub(Class<?> c, Object object) {
        JSONObject jsonObject = new JSONObject();
        returnStringGettersList(c).forEach(string -> {
            String string2 = returnFieldFromMethod(string, "get");
            jsonObject.put(string2, runGetterMethod(c, object, string));
        });
        return jsonObject;
    }


    public static JSONObject getJSONEnum(Object enumList) {
        JSONObject jsonObject = new JSONObject();
        Class<?> enuM = ((List<?>) enumList).get(0).getClass();

        ((List<?>) enumList).forEach(o -> {
            JSONObject jsonObject1 = new JSONObject();
            returnStringGettersList(enuM).stream().filter(string -> !string.equals("getRequiredSpellList")).forEach(string -> {
                String string2 = returnFieldFromMethod(string, "is");
                jsonObject1.put(string2, runGetterMethod(enuM, o, string));
            });
            jsonObject.put(o.toString(), jsonObject1);
        });

        return jsonObject;
    }

    public static void saveWizard(String filename) {
        JSONObject wizardJSONObject =  getJSONObject(Wizard.class, wizard);
        saveObject(wizardJSONObject.toJSONString(), "wizard" + filename);
    }

    public static void saveLevel(String filename) {

        JSONObject levelJSONObject =  getJSONEnum(Level.returnAllLevels());
        saveObject(levelJSONObject.toJSONString(), "level" + filename);
    }

    public static void saveProgress(String filename) {
        saveWizard(filename);
        saveLevel(filename);
    }

    public static List<String> returnSaveFiles(String dir) {
        return Stream.of(Objects.requireNonNull(new File(dir).listFiles()))
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toList());
    }

    public static List<String> returnFormattedSaveFiles(String dir) {
        return returnSaveFiles("saves").stream()
                .map(s -> s.replace("level", "")
                        .replace("wizard","")
                        .replace(".json", ""))
                .distinct().toList();
    }

    public static void loadProgress(String filename) {
        List<String> saves = returnSaveFiles("saves").stream().filter(s -> s.contains(filename)).toList();
        JSONParser parser = new JSONParser();
        String wizardString = saves.stream().filter(s -> s.contains("wizard")).toList().get(0);
        String levelString = saves.stream().filter(s -> s.contains("level")).toList().get(0);

        JSONObject wizardJSONObject = null;
        JSONObject levelJSONObject = null;

        try {
            wizardJSONObject = (JSONObject) parser.parse(new FileReader("saves/" + wizardString));
            levelJSONObject = (JSONObject) parser.parse(new FileReader("saves/" + levelString));
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadClass(Wizard.class, wizard, (JSONObject) wizardJSONObject);
        loadEnum(Level.class, (JSONObject) levelJSONObject);
    }

    public static void loadClass(Class<?> c, Object object, JSONObject jsonObject) {
        returnStringSettersList(c).forEach(methodString -> {
            String fieldString = returnFieldFromMethod(methodString, "set");

            runSetterMethod(c, object, methodString, jsonObject.get(fieldString));
        });
    }

    public static void loadEnum(Class<?> c, JSONObject jsonObject) {
        returnStringSettersList(c).forEach(methodString -> {
            runSetterMethod(c, c, methodString, jsonObject);
        });
    }

    private static String returnFieldFromMethod(String methodString, String set) {
        String fieldString1 = methodString.replace(set, "");
        return fieldString1.replaceFirst(fieldString1.substring(0, 1), fieldString1.substring(0, 1).toLowerCase());
    }

    public static void saveGame() {
        List<String> saves = returnFormattedSaveFiles("saves");

        printTitle("Current Save Games: ");
        printChoices(saves);

        printTitle("Enter the name of the save file you wish to save to: ");
        String filename = returnChoiceString();

        if(saves.contains(filename)) {
            printTitle("This save file already exists, do you wish to overwrite it? (y/n)");
            if(returnYesOrNo()) {
                saveProgress(filename);
            }
        } else {
            saveProgress(filename);
        }
        printTitle("Save file " + filename + " has been saved.");
    }

    public static void loadGame() {
        List<String> saves = returnFormattedSaveFiles("saves");

        printTitle("Select a game to load: ");
        printChoices(saves);

        String filename = saves.get(returnChoiceInt(1, saves.size(), false, null) - 1);
        loadProgress(filename);
        printTitle("Game " + filename + " has been loaded.");
    }
}
