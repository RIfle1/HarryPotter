package Main;

import java.io.IOException;
import java.util.*;

public class ConsoleFunctions {
    static Scanner scanner = new Scanner(System.in);
    public static final String[] yesOrNo = {"Yes", "No"};

    // Read the user's choice

    public static String returnChoiceString() {
        System.out.println("-> ");
        return scanner.next();
    }

    public static int returnChoiceInt() {
        int input;

        do{
            System.out.println("-> ");
            try{
                input = Integer.parseInt(scanner.next());
            }
            catch(Exception e) {
                input = -1;
                System.out.println("Input must be an integer");
            }
        }
        while(input < 1);
        return input;
    }

    public static int returnChoiceInt(int min, int max) {
        int input;

        do{
            System.out.println("-> ");
            try{
                input = Integer.parseInt(scanner.next());
            }
            catch(Exception e) {
                input = -1;
                System.out.println("Input must be an integer");
            }
            if(input < min || input > max) {
                input = -1;
                System.out.println("Input must be between " + min + " and " + max);
            }
        }
        while(input < 1);
        return input;
    }


    public static void printChoices(List<String> choicesList) {
        for(int i = 0; i < choicesList.toArray().length; i++) {
            System.out.printf("(%d) %s\n", i + 1, choicesList.get(i));
        }
    }

    public static void printChoices(String[] choicesList) {
        for(int i = 0; i < choicesList.length; i++) {
            System.out.printf("(%d) %s\n", i + 1, choicesList[i]);
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
    public static void printHeader(String title){
        printSeparator(title.length());
        System.out.println(title);
        printSeparator(title.length());
    }

    // Method to continue
    public static void continuePrompt(){
        System.out.println("\nType anything and then enter to continue...");
        System.out.println(scanner.nextLine());


    }

    public static void gameCredits() {
        String welcomeText = "Welcome to Harry Potter Text RPG, Made by Filips Barakats";
        printHeader(welcomeText);
        continuePrompt();
        clearConsole();
    }
}
