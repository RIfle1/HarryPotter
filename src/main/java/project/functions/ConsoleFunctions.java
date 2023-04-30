package project.functions;

import project.classes.Color;
import project.classes.Enemy;
import project.classes.Wizard;
import project.enums.Level;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.System.exit;
import static project.classes.Color.*;
import static project.classes.Spell.returnAllSpells;
import static project.enums.Level.*;
import static project.functions.LevelFunctions.levelHashMap;
import static project.functions.SaveFunctions.loadGamePrompt;
import static project.functions.SaveFunctions.saveGame;

public class ConsoleFunctions {
    static Scanner scanner = new Scanner(System.in);
    public static final String[] yesOrNo = {"Yes", "No"};

    // Read the user's choice

    public static String returnChoiceString() {
        System.out.println(Color.returnColoredText("-> ", ANSI_YELLOW));
        return scanner.next();
    }

    public static boolean returnYesOrNo() {
        printChoices(yesOrNo);
        int answer = returnChoiceInt(1, yesOrNo.length, false, null) - 1;
        return answer == 0;
    }

    public static int returnChoiceInt(int min, int max, boolean backSupport, Runnable runnable) {
        int input = -1;

        do {
            input = checkInput(min, max, backSupport, runnable);
        }
        while(input == -1);
        return input;
    }

    public static int checkInput(int min, int max, boolean backSupport, Runnable runnable) {
        String input2;
        int input = -1;
        System.out.println(Color.returnColoredText("-> ", ANSI_YELLOW));
        try{
            input2 = scanner.next();
            // EXIT - BACK - STRING CHECK
            if(Objects.equals(input2, "exit")) {
                exit(0);
            }
            else if(Objects.equals(input2, "back") && backSupport) {
                runnable.run();
                return -2;
            }
            else {
                input = Integer.parseInt(input2);
            }

            // INPUT CHECK
            if(input < min || input > max) {
                input = -1;
                System.out.println(Color.returnColoredText("Input must be between " + min + " and " + max, ANSI_RED));
            }
        }
        catch(Exception e) {
            System.out.println(Color.returnColoredText("Input must be an integer", ANSI_RED));
        }

        return input;
    }


    public static void printChoices(List<String> choicesList) {
        for(int i = 0; i < choicesList.toArray().length; i++) {
            System.out.printf("(%s) %s\n", Color.returnColoredText(String.valueOf(i + 1), ANSI_YELLOW), Color.returnColoredText(choicesList.get(i), ANSI_PURPLE));
        }
    }

    public static void printChoices(String[] choicesList) {
        for(int i = 0; i < choicesList.length; i++) {
            System.out.printf("(%s) %s\n", Color.returnColoredText(String.valueOf(i + 1), ANSI_YELLOW), Color.returnColoredText(choicesList[i], ANSI_PURPLE));
        }
    }

    public static void clearConsole() {
        for(int i=0; i< 100; i++) {
            System.out.println();
        }
    }

    public static String returnLineSeparator(int n) {
        return "-".repeat(Math.max(0, n));
    }

    public static String printColumnSeparator(String separator) {
        return returnColoredText("\t"+separator+"\t", ANSI_BLACK);
    }

    public static void printColoredHeader(String header){
        clearConsole();
        System.out.println(returnLineSeparator(header.length()));
        System.out.println(Color.returnColoredText(header, ANSI_GREEN));
        System.out.println(returnLineSeparator(header.length()));
    }

    public static void printTitle(String title) {
        System.out.println(returnLineSeparator(title.length()));
        System.out.println(title);
        System.out.println(returnLineSeparator(title.length()));
    }

    public static void printTitleTop(String title) {
        System.out.println(returnLineSeparator(title.length()));
        System.out.println(title);
    }

    public static void printTitleBottom(String title) {
        System.out.println(title);
        System.out.println(returnLineSeparator(title.length()));
    }

    // Method to continue
    public static void continuePrompt(){
        String continuePromptText = "Press 'Enter' to continue...";

        System.out.println(returnLineSeparator(continuePromptText.length()));
        System.out.println(returnColoredText(continuePromptText, ANSI_BLUE));
        System.out.println(returnLineSeparator(continuePromptText.length()));
        System.out.println(returnColoredText("->", ANSI_YELLOW));

        System.out.print(scanner.nextLine());
    }

    public static void continuePromptExtra(){
        String continuePromptText = "Press 'Enter' to continue...";

        System.out.println(returnLineSeparator(continuePromptText.length()));
        System.out.println(returnColoredText(continuePromptText, ANSI_BLUE));
        System.out.println(returnLineSeparator(continuePromptText.length()));
        System.out.println(returnColoredText("->", ANSI_YELLOW));

        System.out.print(scanner.nextLine());
        System.out.print(scanner.nextLine());
    }

    public static void gameCredits() {
        String welcomeText = "Welcome to Harry Potter Text RPG, Made by Filips Barakats";
        printColoredHeader(welcomeText);
        continuePrompt();
    }

    public static void chooseLevel() {
        Enemy.clearEnemies();

        printColoredHeader("Choose your level: ");
        printChoices(returnUnlockedLevelsList());

        String chosenLevelString = returnUnlockedLevelsList().get(returnChoiceInt(1, returnUnlockedLevelsList().size(), true, ConsoleFunctions::chooseAction) - 1);
        Level chosenLevel = setLevel(chosenLevelString);
        levelHashMap.get(chosenLevel).run();

        Wizard.wizard.reduceSpellsCooldown();
        ConsoleFunctions.chooseAction();
    }

    public static void chooseAction() {
        Wizard.wizard.updateStats();
        Wizard.wizard.restoreWizardHpDf();

        String[] actionList = {
                "Choose Level (" + Level.returnUnlockedLevelsList().size() + "/" + returnAllLevels().size() + " unlocked)",
                "Upgrade Specs" + (Wizard.wizard.getSpecPoints() > 0 ? " (" + (int) Wizard.wizard.getSpecPoints() + " points available)" : " (No points available)"),
                "Check Stats",
                "Check Available Spells (" + Wizard.wizard.getSpellsKeyList().size() + "/" + returnAllSpells().size() + " available)",
                "Save Game",
                "Load Game"
        };

        printColoredHeader("Choose your action: ");
        printChoices(actionList);

        switch (returnChoiceInt(1, actionList.length, false, null)) {
            case 1 -> chooseLevel();
            case 2 -> Wizard.wizard.upgradeSpec(ConsoleFunctions::chooseAction);
            case 3 -> System.out.println(Wizard.wizard.returnAllStringStats(0));
            case 4 -> Wizard.wizard.printAllCharacterSpells();
            case 5 -> saveGame();
            case 6 -> loadGamePrompt();
        }
        continuePromptExtra();
        chooseAction();
    }

    public static String getTxtString(String filename) {
        StringBuilder sb = new StringBuilder();

        File file = new File(filename);
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine())
                sb.append(sc.nextLine()).append("\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static void printMainScreen() {
        System.out.println(returnColoredText(getTxtString("ascii/harryPotterV1.txt"), ANSI_RED));
        continuePrompt();
    }

}
