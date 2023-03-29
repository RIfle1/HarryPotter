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
            String string1 = string.replace("get", "");
            String string2 = string1.replaceFirst(string1.substring(0, 1), string1.substring(0, 1).toLowerCase());
            jsonObject.put(string2, runGetterMethod(c, object, string));
        });
        return jsonObject;
    }


    public static JSONObject getJSONEnum(Object enumList) {
        JSONObject jsonObject = new JSONObject();
        Class<?> e = ((List<?>) enumList).get(0).getClass();
        AtomicInteger counter = new AtomicInteger();

        ((List<?>) enumList).forEach(o -> {
            JSONObject jsonObject1 = new JSONObject();
            returnStringGettersList(e).forEach(string -> {
                String string1 = string.replace("get", "");
                String string2 = string1.replaceFirst(string1.substring(0, 1), string1.substring(0, 1).toLowerCase());
                jsonObject1.put(string2, runGetterMethod(e, o, string));
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

//        System.out.println(returnStringSettersList(Wizard.class));

        loadClass(Wizard.class, wizard, (JSONObject) wizardJSONObject);
//        JSONObject jsonObject = (JSONObject) jsonObj;
    }

    public static void loadClass(Class<?> c, Object object, JSONObject jsonObject) {
        returnStringSettersList(c).forEach(methodString -> {
            String fieldString1 = methodString.replace("set", "");
            String fieldString2 = fieldString1.replaceFirst(fieldString1.substring(0, 1), fieldString1.substring(0, 1).toLowerCase());

            runSetterMethod(c, object, methodString, jsonObject.get(fieldString2));
        });
    }
}
