package project.functions;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
import project.classes.Levels;
import project.classes.Wizard;
import project.classes.Level;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static project.classes.Level.returnAllLevels;
import static project.functions.GeneralFunctions.generateRandomString;
import static project.functions.GeneralFunctions.returnFileAttribute;
import static project.fx.functions.JavaFxFunctions.createPopup;
import static project.fx.controllers.GameMenuController.gameMenuScene;

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
            String lastModifiedSaveFile = returnLastModifiedSaveFile(saveFiles);
            loadProgress(lastModifiedSaveFile);
            ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());
            gameMenuScene(actionEvent);
        }
        catch (IndexOutOfBoundsException e) {
            ActionEvent actionEvent = new ActionEvent(event.getSource(), event.getTarget());
            createPopup(actionEvent, Alert.AlertType.WARNING, "No save files found");
        }
    }

    private static String returnLastModifiedSaveFile(List<String> saveFiles) {
        Optional<String> lastModifiedSaveFile = saveFiles.stream().max((a, b) -> {
            String fileModifiedTimeA = returnFileAttribute("saves", a, "lastModifiedTime");
            String fileModifiedTimeB = returnFileAttribute("saves", b, "lastModifiedTime");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            LocalDate dateTimeA = LocalDate.parse(fileModifiedTimeA, formatter);
            LocalDate dateTimeB = LocalDate.parse(fileModifiedTimeB, formatter);

            return dateTimeA.compareTo(dateTimeB);
        });

        return lastModifiedSaveFile.orElseThrow(IndexOutOfBoundsException::new);
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

    public static void saveObject2(String filename, Object objects, Class<?> objectClass, List<String> fieldsToOmit) {
        XStream xstream = new XStream();
        xstream.addPermission(AnyTypePermission.ANY);

        for(String field : fieldsToOmit) {
            xstream.omitField(objectClass, field);
        }

        File xmlFile = new File("saves/" + filename + ".xml");
        try (OutputStream out = new FileOutputStream(xmlFile)) {
            xstream.toXML(objects, out);
        } catch (IOException e) {
            System.err.println("Failed to write using xstream");
            e.printStackTrace();
        }
    }

    // OTHER METHOD OF LOADING OBJECT
    public static Object returnLoadedObject(String filename) {
        XStream xstream = new XStream();
        xstream.addPermission(AnyTypePermission.ANY);

        File xmlFile = new File("saves/" + filename + ".xml");
        try {
            return xstream.fromXML(xmlFile);
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void loadWizard(String filename) {
        Wizard.wizard = (Wizard) returnLoadedObject("wizard-" + filename);
    }

    public static void loadLevel(String filename) {
        Levels levels = (Levels) returnLoadedObject("levels-" + filename);

        assert levels != null;
        levels.getLevels().forEach(l -> {
            returnAllLevels().forEach(l2 -> {
                if(l2.getLevelName().equals(l.getLevelName())) {
                    l2.setUnlocked(l.isUnlocked());
                }
            });
        });
    }


    public static void saveWizard(String filename) {
        saveObject("wizard-" + filename, Wizard.wizard);
    }

    public static void saveLevel(String filename) {
        Levels levels = new Levels();

        saveObject2("levels-" + filename, levels, Level.class, Level.returnFieldsToOmit());

    }

    public static void autoSaveProgress() {
        saveProgress("autoSave-" + generateRandomString(5));
    }

    public static void saveProgress(String filename) {
        saveWizard(filename);
        saveLevel(filename);
    }

    public static void saveProgress() {
        String randomString = generateRandomString(5);

        saveWizard("save-" + randomString);
        saveLevel("save-" + randomString);
    }

    public static void loadProgress(String filenameCompressed) {
        loadWizard(filenameCompressed);
        loadLevel(filenameCompressed);
    }

    public static List<String> returnLastModifiedFiles(String path) {
        File[] files = new File(path).listFiles();

        assert files != null;
        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

        return Stream.of(files)
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toList());
    }

    public static List<String> returnFiles(String path) {
        File[] files = new File(path).listFiles();

        assert files != null;
        return Stream.of(files)
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toList());
    }

    public static List<String> returnFormattedSaveFiles() {

        return returnLastModifiedFiles("saves").stream()
                .map(s -> s.replace("levels-", "")
                        .replace("wizard-","")
                        .replace(".xml", ""))
                .distinct().toList();
    }

    public static List<String> returnSaves(String filenameCompressed) {
        List<String> saves = returnLastModifiedFiles("saves").stream().filter(s -> s.contains(filenameCompressed)).toList();

        String wizardString = saves.stream().filter(s -> s.contains("wizard")).toList().get(0);
        String levelString = saves.stream().filter(s -> s.contains("level")).toList().get(0);

        return Arrays.asList(wizardString, levelString);
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

    public static Wizard returnWizardInstance(String filenameCompressed) {
        return (Wizard) returnLoadedObject("wizard-" + filenameCompressed);
    }

    public static void deleteSaveFile(String filenameCompressed) {
        List<String> saves = returnSaves(filenameCompressed);

        saves.forEach(s -> {
            File file = new File("saves/" + s);
            if(file.delete()) {
                System.out.println("File " + s + " has been deleted.");
            } else {
                System.out.println("Failed to delete file " + s);
            }
        });

    }
}
