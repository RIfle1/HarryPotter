package Classes;

import AbstractClasses.AbstractCharacter;
import Enums.*;
import Main.ConsoleFunctions;

import java.util.*;

import static Classes.Color.*;
import static Classes.Wizard.wizard;
import static Enums.EnumMethods.returnFormattedEnum;
import static Enums.Level.unlockNextLevel;
import static Main.ConsoleFunctions.*;
import static Classes.Enemy.*;
import static Main.MechanicsFunctions.generateDoubleBetween;


public class LevelFunctions {
    public static HashMap<Level, Runnable> levelHashMap = new HashMap<>() {{
        put(Level.The_Philosophers_Stone, () -> {
            try {
                level1();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        });
        put(Level.The_Chamber_of_Secrets,  () -> {
            try {
                level2();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        });
        put(Level.The_Prisoner_of_Azkaban, () -> {
            try {
                level3();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        });
        put(Level.The_Goblet_of_Fire, () -> {
            try {
                level4();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        });
        put(Level.The_Order_of_the_Phoenix, () -> {
            try {
                level5();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        });
        put(Level.The_Half_Blooded_Prince, () -> {
            try {
                level6();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        });
        put(Level.The_Deathly_Hallows, () -> {
            try {
                level7();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        });
        put(Level.Battle_Arena, () -> {
            try {
                battleArena();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        });
    }};



    public static void spawnEnemies() throws CloneNotSupportedException {
        int minLevel;
        int maxLevel;
        int enemyAmount;

        printColoredHeader("Select enemy minimum level (1-" + AbstractCharacter.maxLevel + "): ");
        minLevel = returnChoiceInt(1, AbstractCharacter.maxLevel, false);
        printColoredHeader("Select enemy maximum level (1-" + AbstractCharacter.maxLevel + "): ");
        maxLevel = returnChoiceInt(minLevel, AbstractCharacter.maxLevel, false);

        printColoredHeader("Select enemy amount: ");
        enemyAmount = returnChoiceInt(0, 100, false);

        generateEnemies(minLevel, maxLevel, enemyAmount);
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

        // CHOOSE THE ENEMY
        if(enemiesHashMap.size() > 1) {
            // PRINT WIZARD STATS
            printTitle(wizard.returnAllStringStats(5));

            printTitle("Choose an enemy.");
            printEnemies();
            enemyVictim = enemiesHashMap.get(enemiesKeyList.get(returnChoiceInt(enemiesKeyList.size(), false) - 1));
        }
        else {
            // PRINT WIZARD STATS
            printTitle(wizard.returnAllStringStats(0));

            enemyVictim = enemiesHashMap.get(enemiesKeyList.get(0));
            System.out.println(enemyVictim.returnStringStats(0));
        }

        // WARN THE PLAYER ABOUT ENEMY CHANGES
        checkEnemiesHpRatio();

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
            case 3 -> wizard.returnStringStats(0);
        }
    }

    public static boolean wizardHasRequiredSpell(Level level) {
        boolean hasRequiredSpell = true;
        if(level.getRequiredSpellList().size() > 0) {
            for (Spell spell : level.getRequiredSpellList()) {
                if(!wizard.getSpellsKeyList().contains(spell.getSpellName()) && spell != Spell.legendarySword) {
                    printTitle(returnColoredText("You need to learn " + spell.getSpellName() + " before trying this level.", ANSI_RED));
                    hasRequiredSpell = false;
                }
            }
        }

        return hasRequiredSpell;
    }

    public static void levelRepetition(Level level, String objective,
                                       List<String> enemyDeathLine, String graduationLine,
                                       Level nextLevel, int combatTimeout) throws CloneNotSupportedException {

        boolean supposedTimeout = true;
        List<String> enemyDeathLineCopy = new ArrayList<>(enemyDeathLine);

        if(enemyDeathLine.size() < 2) {
            enemyDeathLineCopy.add("You Took Too Long To Defeat The Enemies. Just go to the next level already...");
            supposedTimeout = false;
        }

        if (wizardHasRequiredSpell(level)) {
            String levelName = returnFormattedEnum(level);
            printColoredHeader(levelName);
            printTitle(objective);
            checkEnemiesHpRatio();

            printTitle(returnColoredText("Do you accept the challenge?", ANSI_BLUE));
            boolean answer = returnYesOrNo();
            if(answer) {
                while (!enemiesHashMap.isEmpty() && wizard.getHealthPoints() > 0 && combatTimeout > 0) {
                    chooseAction();
                    clearConsole();
                    combatTimeout--;
                }

                levelOutcome(enemyDeathLineCopy, graduationLine, nextLevel, combatTimeout, supposedTimeout);
            }
            else {
                printTitle(returnColoredText("You coward!", ANSI_RED));
            }
        }

        continuePromptExtra();
        ConsoleFunctions.chooseAction();
        EnemyName.resetBossVulnerableSpellsList();
    }

    private static void levelOutcome(List<String> enemyDeathLine, String graduationLine, Level nextLevel, int combatTimeout, boolean supposedTimeout) {
        if(combatTimeout == 0) {
            printTitle(enemyDeathLine.get(1));
        }
        else if (enemiesHashMap.isEmpty()) {
            printTitle(enemyDeathLine.get(0));
        } else if (wizard.getHealthPoints() <= 0) {
            printTitle(returnColoredText("You died.", ANSI_RED));
        }
        if((enemiesHashMap.isEmpty() || supposedTimeout) && wizard.getHealthPoints() > 0) {
            printTitle(returnColoredText(graduationLine, ANSI_YELLOW));
            unlockNextLevel(nextLevel);
        }
    }

    public static void battleArena() throws CloneNotSupportedException {
        Level level = Level.Battle_Arena;
        String objective = "Your objective is to defeat all the enemies in the arena.";
        List<String> enemyDeathLine = Collections.singletonList("All the enemies in the arena have been defeated.");
        String graduationLine = "You have graduated from the Battle Arena!";

        spawnEnemies();
        wizard.updateStats();

        levelRepetition(level, objective, enemyDeathLine, graduationLine, null, 100);
    }

    public static void level1() throws CloneNotSupportedException {
        Level level = Level.The_Philosophers_Stone;
        EnemyName enemyName = EnemyName.TROLL;
        String objective = "Your objective is to kill the troll by using Wingardium Leviosa.";
        int enemyMinLevel = 1;
        int enemyMaxLevel = (int) wizard.getLevel();
        int enemyAmount = 1;
        List<String> enemyDeathLine = Collections.singletonList(enemyName.getEnemyDeathLine().get(0));
        String graduationLine = "You graduated Hogwarts's first year, you are now a second year student.";
        Level nextLevel = Level.The_Chamber_of_Secrets;

        generateEnemies(enemyMinLevel, enemyMaxLevel, enemyAmount, enemyName);
        levelRepetition(level, objective, enemyDeathLine, graduationLine, nextLevel, 100);

    }

    public static void level2() throws CloneNotSupportedException {
        Level level = Level.The_Chamber_of_Secrets;
        EnemyName enemyName = EnemyName.BASILISK;
        String objective;
        List<String> enemyDeathLine = new ArrayList<>();
        boolean gryffindorHouse = wizard.getHouse().getHouseName() == HouseName.GRYFFINDOR;
        if(gryffindorHouse) {
            objective = "Your Objective is to kill the Basilisk with Godric Gryffindor's legendary Sword.";
            enemyDeathLine.add(enemyName.getEnemyDeathLine().get(0));
        }
        else {
            objective = "Your Objective is to kill the Basilisk by removing one of his teeth with Accio and then stabbing Tom Riddle's Journal.";
            enemyDeathLine.add(enemyName.getEnemyDeathLine().get(1));
        }
        int enemyMinLevel = 2;
        int enemyMaxLevel = (int) wizard.getLevel();
        int enemyAmount = 1;
        String graduationLine = "You graduated Hogwarts's second year, you are now a third year student.";
        Level nextLevel = Level.The_Prisoner_of_Azkaban;

        generateEnemies(enemyMinLevel, enemyMaxLevel, enemyAmount, enemyName);
        levelRepetition(level, objective, enemyDeathLine, graduationLine, nextLevel, 100);
    }

    public static void level3() throws CloneNotSupportedException {
        Level level = Level.The_Prisoner_of_Azkaban;
        EnemyName enemyName = EnemyName.DEMENTOR;
        String objective = "The dementors are everywhere! Your objective is to use Expectro Patronum to eliminate them.";
        List<String> enemyDeathLine = Collections.singletonList("All Dementors have been sent back to hell.");
        int enemyMinLevel = 3;
        int enemyMaxLevel = (int) wizard.getLevel();
        int enemyAmount = 5;
        String graduationLine = "You graduated Hogwarts's third year, you are now a fourth year student.";
        Level nextLevel = Level.The_Goblet_of_Fire;

        generateEnemies(enemyMinLevel, enemyMaxLevel, enemyAmount, enemyName);
        levelRepetition(level, objective, enemyDeathLine, graduationLine, nextLevel, 100);

    }
    public static void level4() throws CloneNotSupportedException {
        Level level = Level.The_Goblet_of_Fire;
        EnemyName enemyName = EnemyName.PETER_PETTIGREW;
        String objective = "You're in a cemetery where you see Voldemort and Peter Pettigrew.\n" +
                "Your only hope of escape is to defeat Peter Pettigrew by using accio.";
        List<String> enemyDeathLine = Collections.singletonList(enemyName.getEnemyDeathLine().get(0));
        int enemyMinLevel = 4;
        int enemyMaxLevel = (int) wizard.getLevel();
        int enemyAmount = 1;
        String graduationLine = "You graduated Hogwarts's fourth year, you are now a fifth year student.";
        Level nextLevel = Level.The_Order_of_the_Phoenix;

        generateEnemies(enemyMinLevel, enemyMaxLevel, enemyAmount, enemyName);
        levelRepetition(level, objective, enemyDeathLine, graduationLine, nextLevel, 100);
    }
    public static void level5() throws CloneNotSupportedException {
        Level level = Level.The_Order_of_the_Phoenix;
        EnemyName enemyName = EnemyName.DOLORES_UMBRIDGE;
        String objective = "It's time for your exams! Your objective is to distract Dolores Umbridge until the fireworks are ready.\n" +
                "Don't worry, your spells won't kill her (Or will they?)";
        List<String> enemyDeathLine = Arrays.asList(enemyName.getEnemyDeathLine().get(1), enemyName.getEnemyDeathLine().get(0));
        int enemyMinLevel = 6;
        int enemyMaxLevel = (int) wizard.getLevel();
        int enemyAmount = 1;
        String graduationLine = "You graduated Hogwarts's fifth year, you are now a sixth year student.";
        Level nextLevel = Level.The_Half_Blooded_Prince;

        generateEnemies(enemyMinLevel, enemyMaxLevel, enemyAmount, enemyName);
        levelRepetition(level, objective, enemyDeathLine, graduationLine, nextLevel, 6);
    }
    public static void level6() throws CloneNotSupportedException {
        // TODO THIS LEVEL IS NOT FINISHED
        Level level = Level.The_Half_Blooded_Prince;
        EnemyName enemyName = EnemyName.DEATH_EATER;
        String objective = "It's time for your exams! Your objective is to distract Dolores Umbridge until the fireworks are ready.\n" +
                "Don't worry, your spells won't kill her (Or will they?)";
        List<String> enemyDeathLine = Arrays.asList(enemyName.getEnemyDeathLine().get(1), enemyName.getEnemyDeathLine().get(0));
        int enemyMinLevel = 6;
        int enemyMaxLevel = (int) wizard.getLevel();
        int enemyAmount = 1;
        String graduationLine = "You graduated Hogwarts's fifth year, you are now a sixth year student.";
        Level nextLevel = Level.The_Deathly_Hallows;

        generateEnemies(enemyMinLevel, enemyMaxLevel, enemyAmount, enemyName);
        levelRepetition(level, objective, enemyDeathLine, graduationLine, nextLevel, 6);
    }
    public static void level7() throws CloneNotSupportedException {

    }

}
