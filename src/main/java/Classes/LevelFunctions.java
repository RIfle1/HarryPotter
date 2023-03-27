package Classes;

import AbstractClasses.AbstractCharacter;
import Enums.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Classes.Color.*;
import static Classes.Wizard.wizard;
import static Enums.EnumMethods.returnFormattedEnum;
import static Enums.Level.unlockNextLevel;
import static Main.ConsoleFunctions.*;
import static Classes.Enemy.*;
import static Main.MechanicsFunctions.generateDoubleBetween;


public class LevelFunctions {

    public static void spawnEnemies() throws CloneNotSupportedException {
        Enemy.clearEnemies();

        int minLevel;
        int maxLevel;
        int enemyAmount;

        printColoredHeader("Select enemy minimum level (1-" + AbstractCharacter.maxLevel + "): ");
        minLevel = returnChoiceInt(1, AbstractCharacter.maxLevel, false);
        printColoredHeader("Select enemy maximum level (1-" + AbstractCharacter.maxLevel + "): ");
        maxLevel = returnChoiceInt(minLevel, AbstractCharacter.maxLevel, false);

        printColoredHeader("Select enemy amount: ");
        enemyAmount = returnChoiceInt(0, 100, false);

        generateEnemies(minLevel, maxLevel, enemyAmount, EnemyName.GOBLIN);
    }

    public static void fight() throws CloneNotSupportedException {
        MoveType attackMoveType = MoveType.ATTACK;
        List<String> moveTypeList = MoveType.getMoveTypeList(new ArrayList<>(Arrays.asList(MoveType.ATTACK, MoveType.FOLLOW_UP)));

        wizardTurn(attackMoveType, moveTypeList);
        enemyTurn(attackMoveType, moveTypeList);
    }

    private static void enemyTurn(MoveType attackMoveType, List<String> moveTypeList) {
        int randomEnemyIndex;
        Enemy attackingEnemy;
        double enemyCalculatedDamage = 0;
        Spell enemyChosenSpell = null;
        int randomEnemySpellIndex;
        MoveType wizardMoveType;
        String parriedAttackName = "";
        String chosenSpellName = "";
        double dodgeChance = 0;

        // IF ENEMY LIST ISN'T EMPTY
        if(!enemiesHashMap.isEmpty()) {
            boolean wizardDodgeOrParrySuccess = false;

            // CHOOSE A RANDOM ENEMY FROM THE ENEMY LIST TO ATTACK BACK
            randomEnemyIndex = (int) generateDoubleBetween(0, enemiesKeyList.toArray().length - 1);
            attackingEnemy = enemiesHashMap.get(enemiesKeyList.get(randomEnemyIndex));

            // CALCULATE THE ENEMY'S SPELL DAMAGE
            if(attackingEnemy.getEnemyName().getEnemyCombat() == EnemyCombat.SPELL) {
                // CHOOSE A RANDOM SPELL FROM THE ENEMY'S SPELL LIST
                randomEnemySpellIndex = (int) generateDoubleBetween(0, attackingEnemy.getTypedSpellsList(attackMoveType).size() - 1);
                enemyChosenSpell = attackingEnemy.getTypedSpellsFromInt(attackMoveType, randomEnemySpellIndex);

                enemyCalculatedDamage = attackingEnemy.getSpellCalculatedDamage(enemyChosenSpell, wizard);
                parriedAttackName = enemyChosenSpell.getSpellName();
                dodgeChance = attackingEnemy.getSpellChance(enemyChosenSpell);

                chosenSpellName = returnColoredText(enemyChosenSpell.getSpellName(), enemyChosenSpell.getSpellColor());
            }
            else if(attackingEnemy.getEnemyName().getEnemyCombat() == EnemyCombat.MELEE) {
                enemyCalculatedDamage = attackingEnemy.getMeleeCalculatedDamage(wizard);
                parriedAttackName = returnFormattedEnum(attackingEnemy.getEnemyName()) + " melee attack";
                dodgeChance = attackingEnemy.getEnemyName().getEnemyCombat().getCombatChance();

                chosenSpellName = returnColoredText("melee attack", ANSI_RED);
            }

            // NOTIFY THE PLAYER WHAT SPELL THE ENEMY WILL CHOOSE
            printTitle(returnColoredText(returnFormattedEnum(attackingEnemy.getEnemyName()), ANSI_PURPLE) +
                    returnColoredText(" Level " + (int) attackingEnemy.getLevel(), ANSI_YELLOW) + " will attack you with " +
                    chosenSpellName);

            // ASK THE PLAYER HIS ACTION
            printTitle("What will you do?");
            printChoices(moveTypeList);
            wizardMoveType = MoveType.setMoveType(moveTypeList.get(returnChoiceInt(moveTypeList.size(), false) - 1));

            // EXECUTE ACTION BASED ON PLAYER'S CHOICE
            if (wizardMoveType == MoveType.DODGE) {
                wizardDodgeOrParrySuccess = wizard.dodgeSpell(dodgeChance, attackingEnemy);
            } else if (wizardMoveType == MoveType.PARRY) {
                wizardDodgeOrParrySuccess = wizard.parry(parriedAttackName, attackingEnemy, enemyCalculatedDamage);
            }

            // IF DODGE OR PARRY FAILED, THE ENEMY WILL ATTACK
            if(!wizardDodgeOrParrySuccess) {
                if(attackingEnemy.getEnemyName().getEnemyCombat() == EnemyCombat.SPELL) {
                    assert enemyChosenSpell != null;
                    attackingEnemy.spellAttack(enemyChosenSpell, wizard, enemyCalculatedDamage, true);
                }
                else if(attackingEnemy.getEnemyName().getEnemyCombat() == EnemyCombat.MELEE) {
                    attackingEnemy.meleeAttack(wizard, enemyCalculatedDamage, true);
                }
            }

            continuePromptExtra();
        }
    }

    private static void wizardTurn(MoveType attackMoveType, List<String> moveTypeList) throws CloneNotSupportedException {
        Spell wizardChosenSpell;
        Enemy enemyVictim;
        double wizardCalculatedDamage;
        MoveType enemyMoveType;
        boolean enemyDodgeOrParrySuccess = false;

        // ACTIVATE POTIONS
        wizard.applyPotionEffect();

        // PRINT WIZARD STATS
        printTitle(wizard.returnAllStringStats());

        // CHOOSE THE ENEMY
        printTitle("Choose an enemy.");
        printEnemies();
        enemyVictim = enemiesHashMap.get(enemiesKeyList.get(returnChoiceInt(enemiesKeyList.size(), false) - 1));

        // TELL THE PLAYER THE CHANGES IN THE ENEMY
        enemyVictim.checkHpRatio();

        // CHOOSE THE SPELL
        printTitle("Choose the spell you want to use.");
        wizard.printTypedSpells(attackMoveType);
        wizardChosenSpell = wizard.getTypedSpellsFromInt(attackMoveType, returnChoiceInt(wizard.getTypedSpellsList(attackMoveType).size(), false) - 1);

        // IF THE SPELL IS READY IT WILL BE USED, OTHERWISE IT WILL PROMPT FOR ANOTHER SPELL
        while (!wizard.checkSpellReady(wizardChosenSpell)) {
            System.out.println(returnColoredText(wizardChosenSpell.getSpellName() + " will be ready in " + wizardChosenSpell.getSpellReadyIn() + " turn(s).", ANSI_RED));
            System.out.println(returnColoredText("Choose another spell.", ANSI_BLUE));
            wizardChosenSpell = wizard.getTypedSpellsFromInt(attackMoveType, returnChoiceInt(wizard.getTypedSpellsList(attackMoveType).size(), false) - 1);
        }

        // CALCULATE THE SPELL DAMAGE BASED ON OTHER BUFFS AND THEN ATTACK THE CHOSEN ENEMY
        wizardCalculatedDamage = wizard.getSpellCalculatedDamage(wizardChosenSpell, enemyVictim);

        int generateRandomMoveType = (int) generateDoubleBetween(0, moveTypeList.size() - 1);
        enemyMoveType = MoveType.setMoveType(moveTypeList.get(generateRandomMoveType));

        // CHECK IF ENEMY IS A BOSS AND HIS HP LIMIT HAS BEEN REACHED AND RETURN IF CHOSEN SPELL CAN AFFECT THE ENEMY OR NOT
        boolean isVulnerableSpell = enemyVictim.vulnerabilityChecker(enemyVictim.getEnemyName().getEnemyHpLimitRatio(), wizardChosenSpell);

        if(isVulnerableSpell) {
            // EXECUTE ACTION BASED ON RANDOM CHOICE
            if (enemyMoveType == MoveType.DODGE) {
                enemyDodgeOrParrySuccess = enemyVictim.dodgeSpell(wizard.getSpellChance(wizardChosenSpell), wizard);
            } else if (enemyMoveType == MoveType.PARRY) {
                enemyDodgeOrParrySuccess = enemyVictim.parry(wizardChosenSpell.getSpellName(), wizard, wizardCalculatedDamage);
            }

            // IF DODGE OR PARRY FAILED, THE WIZARD WILL ATTACK
            if(!enemyDodgeOrParrySuccess) {
                wizard.spellAttack(wizardChosenSpell, enemyVictim, wizardCalculatedDamage, true);
            }
        }
        continuePromptExtra();
    }

    public static void chooseAction() throws CloneNotSupportedException {
        List<String> actionList = new ArrayList<>(Arrays.asList("Fight!", "Check Stats", "Use Potion"));
        printColoredHeader("What would you like to do?");
        printChoices(actionList);
        int choice = returnChoiceInt(1, actionList.size(), false);

        switch (choice) {
            case 1 -> fight();
            case 2 -> wizard.usePotion();
            case 3 -> wizard.returnStringStats();
        }
    }

    public static void levelRepetition(Level level, EnemyName enemyName,
                                       String objective, int enemyMinLevel,
                                       int enemyMaxLevel, int enemyAmount,
                                       String enemyDeathLine, String graduationLine, Level nextLevel) throws CloneNotSupportedException {
        String levelName = returnFormattedEnum(level);

        printColoredHeader(levelName);
        printTitle(objective);
        continuePromptExtra();

        generateEnemies(enemyMinLevel, enemyMaxLevel, enemyAmount, enemyName);
        while (!enemiesHashMap.isEmpty() && wizard.getHealthPoints() > 0) {
            chooseAction();
            clearConsole();
        }

        if (enemiesHashMap.isEmpty()) {
            printTitle(enemyDeathLine);
            printTitle(returnColoredText(graduationLine, ANSI_YELLOW));
            unlockNextLevel(nextLevel);
            continuePrompt();
        } else if (wizard.getHealthPoints() <= 0) {
            printTitle(returnColoredText("You died.", ANSI_RED));
        }
        EnemyName.resetAllVulnerableSpellsList();
    }

    public static void level1() throws CloneNotSupportedException {
        Level level = Level.The_Philosophers_Stone;
        EnemyName enemyName = EnemyName.TROLL;
        String objective = "Your objective is to kill the troll by using Wingardium Leviosa.";
        int enemyMinLevel = 1;
        int enemyMaxLevel = 1;
        int enemyAmount = 1;
        String enemyDeathLine = enemyName.getEnemyDeathLine().get(0);
        String graduationLine = "You graduated Hogwarts's first year, you are now a second year student.";
        Level nextLevel = Level.The_Chamber_of_Secrets;

        levelRepetition(level, enemyName, objective, enemyMinLevel, enemyMaxLevel, enemyAmount, enemyDeathLine, graduationLine, nextLevel);
    }

    public static void level2() throws CloneNotSupportedException {
        Level level = Level.The_Chamber_of_Secrets;
        EnemyName enemyName = EnemyName.BASILISK;
        String objective;
        String enemyDeathLine;
        boolean gryffindorHouse = wizard.getHouse().getHouseName() == HouseName.GRYFFINDOR;
        if(gryffindorHouse) {
            objective = "Your Objective is to kill the Basilisk with Godric Gryffindor's legendary Sword.";
            enemyDeathLine = enemyName.getEnemyDeathLine().get(0);
        }
        else {
            objective = "Your Objective is to kill the Basilisk by removing one of his teeth with Accio and then stabbing Tom Riddle's Journal.";
            enemyDeathLine = enemyName.getEnemyDeathLine().get(1);

        }
        int enemyMinLevel = 2;
        int enemyMaxLevel = 2;
        int enemyAmount = 1;
        String graduationLine = "You graduated Hogwarts's first year, you are now a second year student.";
        Level nextLevel = Level.The_Prisoner_of_Azkaban;

        levelRepetition(level, enemyName, objective, enemyMinLevel, enemyMaxLevel, enemyAmount, enemyDeathLine, graduationLine, nextLevel);
    }

    public static void level3() {}
    public static void level4() {}
    public static void level5() {}
    public static void level6() {}
    public static void level7() {}

    public static void battleArena() throws CloneNotSupportedException {
        spawnEnemies();
        wizard.updateStats();
        wizard.restoreWizardHpDf();
        while (!enemiesHashMap.isEmpty() && wizard.getHealthPoints() > 0) {
            chooseAction();
            clearConsole();
        }
        if (enemiesHashMap.isEmpty()) {
            printTitle(returnColoredText("Congrats, you defeated all the enemies.", ANSI_YELLOW));
        } else if (wizard.getHealthPoints() <= 0) {
            printTitle(returnColoredText("You died.", ANSI_RED));
        }
    }

}
