package Main;

import Classes.Color;

import java.util.*;

import static Classes.BattleArena.battleArena;
import static Classes.Color.*;
import static Classes.Color.returnColoredText;

public class ConsoleFunctions {
    static Scanner scanner = new Scanner(System.in);
    public static final String[] yesOrNo = {"Yes", "No"};

    // Read the user's choice

    public static String returnChoiceString() {
        System.out.println(Color.returnColoredText("-> ", ANSI_YELLOW));
        return scanner.next();
    }

    public static int returnChoiceInt(int max) {
        int input;

        do{
            System.out.println(Color.returnColoredText("-> ", ANSI_YELLOW));
            try{
                input = Integer.parseInt(scanner.next());
            }
            catch(Exception e) {
                input = -1;
                System.out.println(Color.returnColoredText("Input must be an integer", ANSI_RED));
            }
            if(input > max) {
                input = -1;
                System.out.println(Color.returnColoredText("Input must be between 1 and " + max, ANSI_RED));
            }
        }
        while(input < 1);
        return input;
    }

    public static int returnChoiceInt(int min, int max) {
        int input;

        do{
            System.out.println(Color.returnColoredText("-> ", ANSI_YELLOW));
            try{
                input = Integer.parseInt(scanner.next());
            }
            catch(Exception e) {
                input = -1;
                System.out.println(Color.returnColoredText("Input must be an integer", ANSI_RED));
            }
            if(input < min || input > max) {
                input = -1;
                System.out.println(Color.returnColoredText("Input must be between " + min + " and " + max, ANSI_RED));
            }
        }
        while(input < 1);
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

    // Method to clear the console
    public static void clearConsole() {
        for(int i=0; i< 100; i++) {
            System.out.println();
        }
    }

    // Method to print a separator
    public static void printSeparator(int n) {
        for(int i=0; i<n; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    // Method to print a heading
    public static void printColoredHeader(String header){
        clearConsole();
        printSeparator(header.length());
        System.out.println(Color.returnColoredText(header, ANSI_GREEN));
        printSeparator(header.length());
    }

    public static void printHeader(String header) {
        clearConsole();
        printSeparator(header.length());
        System.out.println(header);
        printSeparator(header.length());
    }

    public static void printTitle(String title) {
        printSeparator(title.length());
        System.out.println(title);
        printSeparator(title.length());
    }

    // Method to continue
    public static void continuePrompt(){
        String continuePromptText = "Press 'Enter' to continue...";

        printSeparator(continuePromptText.length());
        System.out.println(returnColoredText(continuePromptText, ANSI_BLUE));
        printSeparator(continuePromptText.length());
        System.out.println(returnColoredText("->", ANSI_YELLOW));

        System.out.print(scanner.nextLine());
    }

    public static void continuePromptExtra(){
        String continuePromptText = "Press 'Enter' to continue...";

        printSeparator(continuePromptText.length());
        System.out.println(returnColoredText(continuePromptText, ANSI_BLUE));
        printSeparator(continuePromptText.length());
        System.out.println(returnColoredText("->", ANSI_YELLOW));

        System.out.print(scanner.nextLine());
        System.out.print(scanner.nextLine());
    }

    public static void gameCredits() {
        String welcomeText = "Welcome to Harry Potter Text RPG, Made by Filips Barakats";
        printColoredHeader(welcomeText);
        continuePrompt();
    }

    public static void chooseLevel() throws CloneNotSupportedException {
        String[] optionsList = {
                "The Philosopher's Stone",
                "The Chamber of Secrets",
                "The Prisoner of Azkaban",
                "The Goblet of Fire",
                "The Order of the Phoenix",
                "The Half-Blooded Prince",
                "The Deathly Hallows",
                "Battle Arena",
        };

        printColoredHeader("Choose your level: ");
        printChoices(optionsList);

        switch (returnChoiceInt(optionsList.length)) {
            case 1, 2, 3, 4, 5, 6, 7, 8 -> battleArena();
        }
    }
}
