package Main;
import java.util.Scanner;

public class ConsoleLogic {
    static Scanner scanner = new Scanner(System.in);

    public static int readInt(String prompt, int userChoice) {
        int input;

        do{
            System.out.println(prompt);
            try{
                input = Integer.parseInt(scanner.next());
            }
            catch(Exception e) {
                input = -1;
                System.out.println("Input must be an integer");
            }
        }
        while(input < 1 || input > userChoice);
        return input;
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
        printSeparator(30);
        System.out.println(title);
        printSeparator(30);
    }

    // Method to continue
    public static void continueGame(){
        System.out.println("\nPress anything to continue...");
        scanner.next();
    }

}
