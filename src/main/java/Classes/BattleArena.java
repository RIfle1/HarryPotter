package Classes;

import AbstractClasses.AbstractCharacter;

import java.util.List;

import static Classes.Wizard.wizard;
import static Main.ConsoleFunctions.*;
import static Classes.Enemy.*;
import static Main.MechanicsFunctions.generateDoubleBetween;


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

    public static void fight() throws CloneNotSupportedException {
        Enemy enemyVictim;
        Enemy enemyAttacker;
        Spell wizardSpellChosen;
        Spell enemySpellChosen;
        int randomEnemyIndex;
        int randomEnemySpellIndex;

        printTitle(wizard.printAllStats());

        printTitle("Choose the enemy you want to attack.");
        printEnemies();
        enemyVictim = enemiesHashMap.get(enemiesKeyList.get(returnChoiceInt() - 1));

        printTitle("Choose the spell you want to use.");
        printChoices(wizard.getAllCharacterSpells());
        wizardSpellChosen = wizard.getSpellFromInt(returnChoiceInt() - 1);

        wizard.attack(wizardSpellChosen, enemyVictim);

        randomEnemyIndex = (int) generateDoubleBetween(0, enemiesKeyList.toArray().length - 1);

        enemyAttacker = enemiesHashMap.get(enemiesKeyList.get(randomEnemyIndex));
        randomEnemySpellIndex = (int) generateDoubleBetween(0, enemyAttacker.getSpellsKeyList().toArray().length - 1);
        enemySpellChosen = enemyAttacker.getSpellFromInt(randomEnemySpellIndex);

        printTitle(enemyAttacker.getName() + " will attack you.");
        enemyAttacker.attack(enemySpellChosen, wizard);
    }

    public static void battleArena() throws CloneNotSupportedException {
        chooseEnemies();
        while(!enemiesHashMap.isEmpty() && wizard.getHealthPoints() > 0) {
            fight();
            continuePrompt();
        }
        if(enemiesHashMap.isEmpty()) {
            printTitle("Congrats, you defeated all the enemies.");
        }
        else if(wizard.getHealthPoints() <= 0) {
            printTitle("You died.");
        }

    }

}
