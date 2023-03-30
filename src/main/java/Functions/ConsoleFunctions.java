package Functions;

import Classes.Color;
import Enums.Level;

import java.util.*;

import static Classes.Color.*;
import static Classes.Color.returnColoredText;
import static Classes.Enemy.clearEnemies;
import static Classes.Spell.returnAllSpells;
import static Functions.LevelFunctions.levelHashMap;
import static Classes.Wizard.wizard;
import static Enums.Level.*;
import static Functions.SaveFunctions.loadGame;
import static Functions.SaveFunctions.saveGame;
import static java.lang.System.exit;

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

    public static void printLineSeparator(int n) {
        for(int i=0; i<n; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    public static String printColumnSeparator(String separator) {
        return returnColoredText("\t"+separator+"\t", ANSI_BLACK);
    }

    public static void printColoredHeader(String header){
        clearConsole();
        printLineSeparator(header.length());
        System.out.println(Color.returnColoredText(header, ANSI_GREEN));
        printLineSeparator(header.length());
    }

    public static void printHeader(String header) {
        clearConsole();
        printLineSeparator(header.length());
        System.out.println(header);
        printLineSeparator(header.length());
    }

    public static void printTitle(String title) {
        printLineSeparator(title.length());
        System.out.println(title);
        printLineSeparator(title.length());
    }

    // Method to continue
    public static void continuePrompt(){
        String continuePromptText = "Press 'Enter' to continue...";

        printLineSeparator(continuePromptText.length());
        System.out.println(returnColoredText(continuePromptText, ANSI_BLUE));
        printLineSeparator(continuePromptText.length());
        System.out.println(returnColoredText("->", ANSI_YELLOW));

        System.out.print(scanner.nextLine());
    }

    public static void continuePromptExtra(){
        String continuePromptText = "Press 'Enter' to continue...";

        printLineSeparator(continuePromptText.length());
        System.out.println(returnColoredText(continuePromptText, ANSI_BLUE));
        printLineSeparator(continuePromptText.length());
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
        clearEnemies();

        printColoredHeader("Choose your level: ");
        printChoices(returnUnlockedLevelsList());

        String chosenLevelString = returnUnlockedLevelsList().get(returnChoiceInt(1, returnUnlockedLevelsList().size(), true, ConsoleFunctions::chooseAction) - 1);
        Level chosenLevel = setLevel(chosenLevelString);
        levelHashMap.get(chosenLevel).run();

        wizard.reduceSpellsCooldown();
        ConsoleFunctions.chooseAction();
    }

    public static void chooseAction() {
        wizard.updateStats();
        wizard.restoreWizardHpDf();

        String[] actionList = {
                "Choose Level (" + Level.returnUnlockedLevelsList().size() + "/" + returnAllLevels().size() + " unlocked)",
                "Upgrade Specs" + (wizard.getSpecPoints() > 0 ? " (" + (int) wizard.getSpecPoints() + " points available)" : " (No points available)"),
                "Check Stats",
                "Check Available Spells (" + wizard.getSpellsKeyList().size() + "/" + returnAllSpells().size() + " available)",
                "Save Game",
                "Load Game"
        };

        printColoredHeader("Choose your action: ");
        printChoices(actionList);

        switch (returnChoiceInt(1, actionList.length, false, null)) {
            case 1 -> chooseLevel();
            case 2 -> wizard.upgradeSpec(ConsoleFunctions::chooseAction);
            case 3 -> System.out.println(wizard.returnAllStringStats(0));
            case 4 -> wizard.printAllCharacterSpells();
            case 5 -> saveGame();
            case 6 -> loadGame();
        }
        continuePromptExtra();
        chooseAction();
    }
}
