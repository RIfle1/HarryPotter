package Main;

import Classes.Color;

import java.util.*;

import static Classes.LevelFunctions.battleArena;
import static Classes.Color.*;
import static Classes.Color.returnColoredText;
import static Classes.LevelFunctions.level1;
import static Classes.Wizard.wizard;
import static Enums.Level.getLevelList;
import static Enums.Level.getUnlockedLevelList;
import static java.lang.System.exit;

public class ConsoleFunctions {
    static Scanner scanner = new Scanner(System.in);
    public static final String[] yesOrNo = {"Yes", "No"};

    // Read the user's choice

    public static String returnChoiceString() {
        System.out.println(Color.returnColoredText("-> ", ANSI_YELLOW));
        return scanner.next();
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
        printColoredHeader("Choose your level: ");
        printChoices(getUnlockedLevelList());

        switch (returnChoiceInt(getUnlockedLevelList().size(), true)) {
            case 1 -> level1();
            case  2, 3, 4, 5, 6, 7, 8 -> battleArena();
            case -2 -> func.run();
        }
    }

    public static void chooseAction() throws CloneNotSupportedException {
        String[] actionList = {
                "Choose Level",
                "Upgrade Specs"
        };

        printColoredHeader("Choose your action: ");
        printChoices(actionList);

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
        }
    }
}
