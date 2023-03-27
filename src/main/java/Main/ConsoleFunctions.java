package Main;

import Classes.Color;
import Enums.Level;

import java.util.*;

import static Classes.Color.*;
import static Classes.Color.returnColoredText;
import static Classes.Enemy.clearEnemies;
import static Classes.LevelFunctions.levelHashMap;
import static Classes.Wizard.wizard;
import static Enums.Level.*;
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
        int answer = returnChoiceInt(yesOrNo.length, false) - 1;
        return answer == 0;
    }

    public static int returnChoiceInt(int max, boolean backSupport) {
        int input = -1;

        do{
            input = getInput(input, backSupport);
            if(input > max && input != -2) {
                input = -1;
                System.out.println(Color.returnColoredText("Input must be between 1 and " + max, ANSI_RED));
            }
        }
        while(input == -1);
        return input;
    }

    public static int returnChoiceInt(int min, int max, boolean backSupport) {
        int input = -1;

        do{
            input = getInput(input, backSupport);
            if((input < min || input > max) && input != -2) {
                input = -1;
                System.out.println(Color.returnColoredText("Input must be between " + min + " and " + max, ANSI_RED));
            }
        }
        while(input == -1);
        return input;
    }

    public static int getInput(int input, boolean backSupport) {
        String input2;
        System.out.println(Color.returnColoredText("-> ", ANSI_YELLOW));
        try{
            input2 = scanner.next();
            if(Objects.equals(input2, "exit")) {
                exit(0);

            }
            else if(Objects.equals(input2, "back") && backSupport) {
                return -2;
            }
            else {
                input = Integer.parseInt(input2);
            }
        }
        catch(Exception e) {
            input = -1;
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

    public static void chooseLevel(Runnable func) throws CloneNotSupportedException {
        clearEnemies();

        printColoredHeader("Choose your level: ");
        printChoices(getUnlockedLevelsList());

        String chosenLevelString = getUnlockedLevelsList().get(returnChoiceInt(getUnlockedLevelsList().size(), true) - 1);
        Level chosenLevel = setLevel(chosenLevelString);
        levelHashMap.get(chosenLevel).run();

        wizard.reduceSpellsCooldown();
        ConsoleFunctions.chooseAction();
    }

    public static void chooseAction() throws CloneNotSupportedException {
        String[] actionList = {
                "Choose Level",
                "Upgrade Specs",
                "Check Stats",
                "Check Available Spells"
        };

        printColoredHeader("Choose your action: ");
        printChoices(actionList);

        wizard.updateStats();
        wizard.restoreWizardHpDf();

        switch (returnChoiceInt(actionList.length, false)) {
            case 1 -> chooseLevel(() -> {
                try {
                    chooseAction();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            });
            case 2 -> wizard.upgradeSpec(() -> {
                    try {
                        chooseAction();
                    } catch (CloneNotSupportedException e) {
                        throw new RuntimeException(e);
                    }
            });
            case 3 -> System.out.println(wizard.returnAllStringStats(0));
            case 4 -> wizard.printAllCharacterSpells();
        }
        continuePromptExtra();
        chooseAction();
    }
}
