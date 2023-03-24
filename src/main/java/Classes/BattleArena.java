package Classes;

import AbstractClasses.AbstractCharacter;
import Enums.MoveType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Classes.Color.*;
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

    public static void fight() throws CloneNotSupportedException {
        Enemy enemyVictim;
        Enemy enemyAttacker;
        Spell wizardChosenSpell;
        Spell enemyChosenSpell;
        MoveType wizardMoveType;
        MoveType enemyMoveType;
        MoveType attackMoveType = MoveType.ATTACK;
        int randomEnemyIndex;
        int randomEnemySpellIndex;
        double wizardCalculatedDamage;
        double enemyCalculatedDamage;

        boolean enemyDodgeOrParrySuccess = false;
        List<String> moveTypeList = MoveType.getMoveTypeList(new ArrayList<>(Arrays.asList(MoveType.ATTACK, MoveType.FOLLOW_UP)));

        printTitle(wizard.printAllStats());
        // CHOOSE THE ENEMY
        printTitle("Choose an enemy.");
        printEnemies();
        enemyVictim = enemiesHashMap.get(enemiesKeyList.get(returnChoiceInt(enemiesKeyList.size()) - 1));

        // CHOOSE THE SPELL
        printTitle("Choose the spell you want to use.");
        wizard.printTypedSpells(attackMoveType);
        wizardChosenSpell = wizard.getTypedSpellsFromInt(attackMoveType, returnChoiceInt(wizard.getTypedSpellsList(attackMoveType).size()) - 1);

        // IF THE SPELL IS READY IT WILL BE USED, OTHERWISE IT WILL PROMPT FOR ANOTHER SPELL
        while (!wizard.checkSpellReady(wizardChosenSpell)) {
            System.out.println(returnColoredText(wizardChosenSpell.getSpellName() + " will be ready in " + wizardChosenSpell.getSpellReadyIn() + " turns.", ANSI_RED));
            System.out.println(returnColoredText("Choose another spell.", ANSI_BLUE));
            wizardChosenSpell = wizard.getTypedSpellsFromInt(attackMoveType, returnChoiceInt(wizard.getTypedSpellsList(attackMoveType).size()) - 1);
        }

        // ACTIVATE POTIONS
        wizard.applyPotionEffect();

        // CALCULATE THE SPELL DAMAGE BASED ON OTHER BUFFS AND THEN ATTACK THE CHOSEN ENEMY
        wizardCalculatedDamage = wizard.getCalculatedDamage(wizardChosenSpell, enemyVictim);

        int generateRandomMoveType = (int) generateDoubleBetween(0, moveTypeList.size() - 1);
        enemyMoveType = MoveType.setMoveType(moveTypeList.get(generateRandomMoveType));

        // EXECUTE ACTION BASED ON RANDOM CHOICE
        if (enemyMoveType == MoveType.DODGE) {
            enemyDodgeOrParrySuccess = enemyVictim.dodge(wizardChosenSpell, wizard);
        } else if (enemyMoveType == MoveType.PARRY) {
            enemyDodgeOrParrySuccess = enemyVictim.parry(wizardChosenSpell, wizard, wizardCalculatedDamage);
        }

        // IF DODGE OR PARRY FAILED, THE WIZARD WILL ATTACK
        if(!enemyDodgeOrParrySuccess) {
            wizard.attack(wizardChosenSpell, enemyVictim, wizardCalculatedDamage, true);
        }
        continuePromptExtra();

        // IF ENEMY LIST ISN'T EMPTY
        if(!enemiesHashMap.isEmpty()) {
            boolean wizardDodgeOrParrySuccess = false;

            // CHOOSE A RANDOM ENEMY FROM THE ENEMY LIST TO ATTACK BACK
            randomEnemyIndex = (int) generateDoubleBetween(0, enemiesKeyList.toArray().length - 1);
            enemyAttacker = enemiesHashMap.get(enemiesKeyList.get(randomEnemyIndex));

            // CHOOSE A RANDOM SPELL FROM THE ENEMY'S SPELL LIST
            System.out.println(enemyAttacker.getTypedSpellsList(attackMoveType).size());
            randomEnemySpellIndex = (int) generateDoubleBetween(0, enemyAttacker.getTypedSpellsList(attackMoveType).size() - 1);
            enemyChosenSpell = enemyAttacker.getTypedSpellsFromInt(attackMoveType, randomEnemySpellIndex);

            // CALCULATE THE ENEMY'S SPELL DAMAGE
            enemyCalculatedDamage = enemyAttacker.getCalculatedDamage(enemyChosenSpell, wizard);

            // NOTIFY THE PLAYER WHAT SPELL THE ENEMY WILL CHOOSE
            printTitle(returnColoredText(returnFormattedEnum(enemyAttacker.getEnemyName()), ANSI_PURPLE) +
                    returnColoredText(" Level " + (int) enemyAttacker.getLevel(), ANSI_YELLOW) + " will attack you with " +
                    returnColoredText(enemyChosenSpell.getSpellName(), enemyChosenSpell.getSpellColor()));

            // ASK THE PLAYER HIS ACTION
            printTitle("What will you do?");
            printChoices(moveTypeList);
            wizardMoveType = MoveType.setMoveType(moveTypeList.get(returnChoiceInt(moveTypeList.size()) - 1));

            // EXECUTE ACTION BASED ON PLAYER'S CHOICE
            if (wizardMoveType == MoveType.DODGE) {
                wizardDodgeOrParrySuccess = wizard.dodge(enemyChosenSpell, enemyAttacker);
            } else if (wizardMoveType == MoveType.PARRY) {
                wizardDodgeOrParrySuccess = wizard.parry(enemyChosenSpell, enemyAttacker, enemyCalculatedDamage);
            }

            // IF DODGE OR PARRY FAILED, THE ENEMY WILL ATTACK
            if(!wizardDodgeOrParrySuccess) {
                enemyAttacker.attack(enemyChosenSpell, wizard, enemyCalculatedDamage, true);
            }

            continuePromptExtra();
        }
    }

    public static void battleArena() throws CloneNotSupportedException {
        chooseEnemies();
        wizard.updateStats();
        wizard.restoreWizardHpDf();
        while (!enemiesHashMap.isEmpty() && wizard.getHealthPoints() > 0) {
            List<String> choiceList = new ArrayList<>(Arrays.asList("Check Stats", "Use Potion", "Fight!"));
            printColoredHeader("What would you like to do?");
            printChoices(choiceList);
            int choice = returnChoiceInt(1, choiceList.size());

            switch (choice) {
                case 1 -> wizard.checkStats();
                case 2 -> wizard.usePotion();
                case 3 -> fight();
            }

            clearConsole();
        }
        if (enemiesHashMap.isEmpty()) {
            printTitle(returnColoredText("Congrats, you defeated all the enemies.", ANSI_YELLOW));
        } else if (wizard.getHealthPoints() <= 0) {
            printTitle(returnColoredText("You died.", ANSI_RED));
        }
    }

}
