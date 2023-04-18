package project.functions;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import project.classes.Wizard;
import project.enums.Level;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static project.javafx.JavaFxFunctions.createPopup;
import static project.javafx.controllers.GameMenuController.gameMenuScene;

public class SaveFunctions {
    private static void createDir() {
        File f = new File("saves");
        if (f.mkdir()) {
            System.out.println("Directory has been created successfully");
        }
    }



    public static void checkSaves() {
        createDir();
        List<String> saveFiles = returnFormattedSaveFiles();

        if(saveFiles.size() > 0) {
            ConsoleFunctions.printColoredHeader("Saved games have been found, would you like to load one?");
            if(ConsoleFunctions.returnYesOrNo()) {
                loadGamePrompt();
            }
            else {
                ConsoleFunctions.gameCredits();
                CharacterCreation.characterInitPrompts();
            }
        }
        else {
            ConsoleFunctions.gameCredits();
            ConsoleFunctions.printColoredHeader("No saved characters have been found, new character creation will now proceed.");
            ConsoleFunctions.continuePrompt();
            CharacterCreation.characterInitPrompts();
        }
    }

    public static void continueGame(MouseEvent event) {
        createDir();
        List<String> saveFiles = returnFormattedSaveFiles();

        try {
            loadProgress(saveFiles.get(0));
            ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());
            gameMenuScene(actionEvent);
        }
        catch (IndexOutOfBoundsException e) {
            ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());
            createPopup(actionEvent, Alert.AlertType.WARNING, "No save files found");
        }
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

        for(Class<?> c1 : GeneralFunctions.findAllClasses("project/abstractClasses")) {
            if(c.getSuperclass().equals(c1)) {
                JSONObject jsonObject2 = getJSONObjectSub(c.getSuperclass(), object);
                jsonObject.putAll(jsonObject2);
            }
        }
        return jsonObject;
    }

    private static JSONObject getJSONObjectSub(Class<?> c, Object object) {
        JSONObject jsonObject = new JSONObject();
        GeneralFunctions.returnStringGettersList(c).forEach(string -> {
            String string2 = returnFieldFromMethod(string, "get");
            jsonObject.put(string2, GeneralFunctions.runGetterMethod(c, object, string));
        });
        return jsonObject;
    }


    public static JSONObject getJSONEnum(Object enumList) {
        JSONObject jsonObject = new JSONObject();
        Class<?> enuM = ((List<?>) enumList).get(0).getClass();

        ((List<?>) enumList).forEach(o -> {
            JSONObject jsonObject1 = new JSONObject();
            GeneralFunctions.returnStringGettersList(enuM).stream().filter(string -> !string.equals("getRequiredSpellList")).forEach(string -> {
                String string2 = returnFieldFromMethod(string, "is");
                jsonObject1.put(string2, GeneralFunctions.runGetterMethod(enuM, o, string));
            });
            jsonObject.put(o.toString(), jsonObject1);
        });

        return jsonObject;
    }

    // OTHER METHOD OF SAVING OBJECT
    public static void saveObject(String filename, Object object) {
        XStream xstream = new XStream();
        xstream.addPermission(AnyTypePermission.ANY);

        File xmlFile = new File("saves/" + filename + ".xml");
        try (OutputStream out = new FileOutputStream(xmlFile)) {
            xstream.toXML(object, out);
        } catch (IOException e) {
            System.err.println("Failed to write using xstream");
            e.printStackTrace();
        }
    }

    // OTHER METHOD OF LOADING OBJECT
    public static void loadWizard(String filename) {
        XStream xstream = new XStream();
        xstream.addPermission(AnyTypePermission.ANY);

        File xmlFile = new File("saves/" + filename + ".xml");
        try {
            System.out.println(Wizard.class.getName());

            Wizard.wizard = (Wizard) xstream.fromXML(xmlFile);

        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void saveWizard(String filename) {
        JSONObject wizardJSONObject =  getJSONObject(Wizard.class, Wizard.wizard);
        saveObject(wizardJSONObject.toJSONString(), "wizard-" + filename);
    }

    public static void saveLevel(String filename) {

        JSONObject levelJSONObject =  getJSONEnum(Level.returnAllLevels());
        saveObject(levelJSONObject.toJSONString(), "level-" + filename);
    }

    public static void saveProgress(String filename) {
        saveWizard(filename);
        saveLevel(filename);
    }

    public static List<String> returnSaveFiles() {
        File[] files = new File("saves").listFiles();

        assert files != null;
        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

        return Stream.of(files)
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toList());
    }

    public static List<String> returnFormattedSaveFiles() {

        return returnSaveFiles().stream()
                .map(s -> s.replace("level-", "")
                        .replace("wizard-","")
                        .replace(".json", ""))
                .distinct().toList();
    }

    public static List<String> returnSaves(String filename) {
        List<String> saves = returnSaveFiles().stream().filter(s -> s.contains(filename)).toList();
        String wizardString = saves.stream().filter(s -> s.contains("wizard")).toList().get(0);
        String levelString = saves.stream().filter(s -> s.contains("level")).toList().get(0);

        return Arrays.asList(wizardString, levelString);
    }

    public static void loadProgress(String filename) {
        JSONParser parser = new JSONParser();
        List<String> saves = returnSaves(filename);

        JSONObject wizardJSONObject = null;
        JSONObject levelJSONObject = null;

        try {
            wizardJSONObject = (JSONObject) parser.parse(new FileReader("saves/" + saves.get(0)));
            levelJSONObject = (JSONObject) parser.parse(new FileReader("saves/" + saves.get(1)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadClass(Wizard.class, Wizard.wizard, (JSONObject) wizardJSONObject);
        loadEnum(Level.class, (JSONObject) levelJSONObject);
    }

    public static void loadClass(Class<?> c, Object object, JSONObject jsonObject) {
        GeneralFunctions.returnStringSettersList(c).forEach(methodString -> {
            String fieldString = returnFieldFromMethod(methodString, "set");

            GeneralFunctions.runSetterMethod(c, object, methodString, jsonObject.get(fieldString));
        });
    }

    public static void loadEnum(Class<?> c, JSONObject jsonObject) {
        GeneralFunctions.returnStringSettersList(c).forEach(methodString -> {
            GeneralFunctions.runSetterMethod(c, c, methodString, jsonObject);
        });
    }

    private static String returnFieldFromMethod(String methodString, String set) {
        String fieldString1 = methodString.replace(set, "");
        return fieldString1.replaceFirst(fieldString1.substring(0, 1), fieldString1.substring(0, 1).toLowerCase());
    }

    public static void saveGame() {
        List<String> saves = returnFormattedSaveFiles();

        ConsoleFunctions.printTitle("Current Save Games: ");
        ConsoleFunctions.printChoices(saves);

        ConsoleFunctions.printTitle("Enter the name of the save file you wish to save to: ");
        String filename = ConsoleFunctions.returnChoiceString();

        if(saves.contains(filename)) {
            ConsoleFunctions.printTitle("This save file already exists, do you wish to overwrite it? (y/n)");
            if(ConsoleFunctions.returnYesOrNo()) {
                saveProgress(filename);
            }
        } else {
            saveProgress(filename);
        }
        ConsoleFunctions.printTitle("Save file " + filename + " has been saved.");
    }

    public static void loadGamePrompt() {
        List<String> saves = returnFormattedSaveFiles();

        ConsoleFunctions.printTitle("Select a game to load: ");
        ConsoleFunctions.printChoices(saves);

        int choiceInt = ConsoleFunctions.returnChoiceInt(1, saves.size(), true, ConsoleFunctions::chooseAction);
        String filename = saves.get(choiceInt - 1);
        if(choiceInt != -2) {
        ConsoleFunctions.printTitle("Are you sure you wish to load game " + filename + "? All unsaved progress will be lost.");
            if(ConsoleFunctions.returnYesOrNo()) {
                loadProgress(filename);
                ConsoleFunctions.printTitle("Game " + filename + " has been loaded.");
            }
        }
    }
}
