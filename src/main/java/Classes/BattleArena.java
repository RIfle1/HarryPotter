package Classes;

import AbstractClasses.AbstractCharacter;

import static Classes.Wizard.wizard;
import static Main.ConsoleFunctions.*;
import static Classes.Enemy.*;


public class BattleArena {

    public static void chooseEnemies() throws CloneNotSupportedException {
        Enemy.clearEnemies();

        int minLevel;
        int maxLevel;
        int enemyAmount;

        printColoredHeader("Select enemy minimum level (1-" + AbstractCharacter.maxLevel + "): ");
        minLevel = returnChoiceInt(1, AbstractCharacter.maxLevel);
        printColoredHeader("Select enemy maximum level (1-" + AbstractCharacter.maxLevel + "): ");
        maxLevel = returnChoiceInt(minLevel, AbstractCharacter.maxLevel);

        printColoredHeader("Select enemy amount: ");
        enemyAmount = returnChoiceInt(0, 100);

        enemiesHashMap = generateEnemies(minLevel, maxLevel, enemyAmount);
    }

    public static void startFight() throws CloneNotSupportedException {
        printHeader(wizard.printAllStats());
        printEnemies();
    }

    public static void battleArena() throws CloneNotSupportedException {
        chooseEnemies();
        startFight();

    }

}
