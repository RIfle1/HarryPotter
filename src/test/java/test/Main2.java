package test;

import java.util.HashMap;
import java.util.Map;

public class Main2 {
    public static Runnable test() {
        return () -> System.out.println("Test");

    }

    public static Runnable test2() {
        return () -> System.out.println("Teleport");

    }

    public static void main(String[] args) throws CloneNotSupportedException {
//        gameCredits();
//        checkSaves();
//        CharacterCreation.characterInit();
//        chooseAction();



        Map<Character, Runnable> commands = new HashMap<>();

        // Populate commands map
        commands.put('h', test());
        commands.put('t', test2());

        // Invoke some command
        char cmd = 't';
        commands.get(cmd).run();   // Prints "Teleport"

//        System.out.println(enemiesHashMap.get(enemiesKeyList.get(0)).getExperiencePoints());
//        unlockNextLevel(Level.The_Philosophers_Stone);
    }
}