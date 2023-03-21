package Classes;

import AbstractClasses.AbstractCharacter;
import Enums.SpellType;

import java.util.List;

import static Classes.Wizard.wizard;
import static Enums.EnumMethods.returnFormattedEnum;
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

    public static void fight(boolean firstMove) throws CloneNotSupportedException {
        Enemy enemyVictim;
        Enemy enemyAttacker;
        Spell wizardChosenSpell;
        Spell enemyChosenSpell;
        SpellType wizardChosenSpellType = SpellType.ATTACK;
        int randomEnemyIndex;
        int randomEnemySpellIndex;

        printTitle(wizard.printAllStats());

        printTitle("Choose the enemy you want to attack.");
        printEnemies();
        enemyVictim = enemiesHashMap.get(enemiesKeyList.get(returnChoiceInt(enemiesKeyList.size()) - 1));

        if (!firstMove) {
            List<String> spellTypeList = SpellType.getSpellTypeList();
            spellTypeList.remove(returnFormattedEnum(SpellType.FOLLOW_UP));
            printTitle("What is your move?");
            printChoices(spellTypeList);
            wizardChosenSpellType = SpellType.setSpellType(spellTypeList.get(returnChoiceInt(spellTypeList.size()) - 1));
        }

        printTitle("Choose the spell you want to use.");
        wizard.printTypedSpells(wizardChosenSpellType);
        wizardChosenSpell = wizard.getTypedSpellsFromInt(wizardChosenSpellType, returnChoiceInt(wizard..size()) - 1);

        while (!wizard.checkSpellReady(wizardChosenSpell)) {
            System.out.println(wizardChosenSpell.getSpellName() + " is on cooldown.");
            wizardChosenSpell = wizard.getTypedSpellsFromInt(wizardChosenSpellType, returnChoiceInt(wizard.getSpellsKeyList().size()) - 1);
        }

        if (wizardChosenSpellType == SpellType.ATTACK) {
            wizard.attack(wizardChosenSpell, enemyVictim);
        } else if (wizardChosenSpellType == SpellType.PARRY) {
            wizard.parry(wizardChosenSpell, enemyVictim);
        }


        randomEnemyIndex = (int) generateDoubleBetween(0, enemiesKeyList.toArray().length - 1);

        enemyAttacker = enemiesHashMap.get(enemiesKeyList.get(randomEnemyIndex));
        randomEnemySpellIndex = (int) generateDoubleBetween(0, enemyAttacker.getSpellsKeyList().toArray().length - 1);
        enemyChosenSpell = enemyAttacker.getSpellFromInt(randomEnemySpellIndex);

        printTitle(enemyAttacker.getName() + " will attack you.");
        enemyAttacker.attack(enemyChosenSpell, wizard);
    }

    public static void battleArena() throws CloneNotSupportedException {
        chooseEnemies();
        boolean firstMove = true;
        while (!enemiesHashMap.isEmpty() && wizard.getHealthPoints() > 0) {
            fight(firstMove);
            firstMove = false;
        }
        if (enemiesHashMap.isEmpty()) {
            printTitle("Congrats, you defeated all the enemies.");
        } else if (wizard.getHealthPoints() <= 0) {
            printTitle("You died.");
        }
        wizard.updateWizardHpDf();

    }

}
