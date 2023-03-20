package Classes;

import AbstractClasses.AbstractCharacter;
import Enums.EnemyName;

import static Classes.Wizard.testWizard;
import static Classes.Wizard.wizard;
import static Main.ConsoleFunctions.*;
import static Classes.Enemy.*;


public class BattleArena {

    public static void chooseEnemies() throws CloneNotSupportedException {
        Enemy.clearEnemies();

        int minLevel;
        int maxLevel;
        int enemyAmount;

        EnemyName enemyName;

        printHeader("Select enemy minimum level (1-" + AbstractCharacter.maxLevel + "): ");
        minLevel = returnChoiceInt(1, AbstractCharacter.maxLevel);
        printHeader("Select enemy maximum level (1-" + AbstractCharacter.maxLevel + "): ");
        maxLevel = returnChoiceInt(minLevel, AbstractCharacter.maxLevel);

        printHeader("Select enemy amount: ");
        enemyAmount = returnChoiceInt(0, 100);

        printHeader("Select enemy type: ");
        printChoices(EnemyName.getEnemyNameList());
        enemyName = EnemyName.setEnemyName(EnemyName.getEnemyNameList().get(returnChoiceInt() - 1));

        enemiesHashMap = generateEnemies(minLevel, maxLevel, enemyAmount, enemyName, testWizard.getDifficulty());
    }

    public static void startFight() throws CloneNotSupportedException {
        printSeparator(200);
        wizard.printStats();
        printSeparator(200);
        printEnemies();
    }

    public static void battleArena() throws CloneNotSupportedException {
        chooseEnemies();
        startFight();

    }

}
