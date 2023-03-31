package Functions;

import AbstractClasses.AbstractCharacter;
import Classes.Enemy;
import Classes.Spell;
import Classes.Wizard;
import Enums.*;

import java.util.*;

import static Classes.Color.*;
import static Classes.Wizard.wizard;
import static Enums.EnumMethods.returnFormattedEnum;
import static Enums.Level.unlockNextLevel;
import static Functions.ConsoleFunctions.*;
import static Classes.Enemy.*;
import static Functions.GeneralFunctions.generateDoubleBetween;
import static java.lang.System.exit;


public class LevelFunctions {
    public static HashMap<Level, Runnable> levelHashMap = new HashMap<>() {{
        put(Level.The_Philosophers_Stone, LevelFunctions::level1);
        put(Level.The_Chamber_of_Secrets, LevelFunctions::level2);
        put(Level.The_Prisoner_of_Azkaban, LevelFunctions::level3);
        put(Level.The_Goblet_of_Fire, LevelFunctions::level4);
        put(Level.The_Order_of_the_Phoenix, LevelFunctions::level5);
        put(Level.The_Half_Blooded_Prince, LevelFunctions::level6);
        put(Level.The_Deathly_Hallows, LevelFunctions::level7);
        put(Level.Battle_Arena, LevelFunctions::battleArena);
    }};

    public static void spawnEnemies() {
        int minLevel;
        int maxLevel;
        int enemyAmount;

        printColoredHeader("Select enemy minimum level (1-" + AbstractCharacter.maxLevel + "): ");
        minLevel = returnChoiceInt(1, AbstractCharacter.maxLevel, false, null);
        printColoredHeader("Select enemy maximum level (1-" + AbstractCharacter.maxLevel + "): ");
        maxLevel = returnChoiceInt(minLevel, AbstractCharacter.maxLevel, false, null);

        printColoredHeader("Select enemy amount: ");
        enemyAmount = returnChoiceInt(0, 100, false, null);

        generateEnemies(minLevel, maxLevel, enemyAmount);
    }

    public static void fight() {
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
                randomEnemySpellIndex = (int) generateDoubleBetween(0, attackingEnemy.returnTypedSpellsList(attackMoveType).size() - 1);
                enemyChosenSpell = attackingEnemy.returnTypedSpellsFromInt(attackMoveType, randomEnemySpellIndex);

                enemyCalculatedDamage = attackingEnemy.returnSpellCalculatedDamage(enemyChosenSpell, wizard);
                parriedAttackName = enemyChosenSpell.getSpellName();
                dodgeChance = attackingEnemy.returnSpellChance(enemyChosenSpell);

                chosenSpellName = returnColoredText(enemyChosenSpell.getSpellName(), enemyChosenSpell.getSpellColor());
            }
            else if(attackingEnemy.getEnemyName().getEnemyCombat() == EnemyCombat.MELEE) {
                enemyCalculatedDamage = attackingEnemy.returnMeleeCalculatedDamage(wizard);
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
            wizardMoveType = MoveType.setMoveType(moveTypeList.get(returnChoiceInt(1, moveTypeList.size(), false, null) - 1));

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

    private static void wizardTurn(MoveType attackMoveType, List<String> moveTypeList) {
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
            printTitle(wizard.returnAllStringStats(7));

            printTitle("Choose an enemy.");
            printEnemies();
            enemyVictim = enemiesHashMap.get(enemiesKeyList.get(returnChoiceInt(1, enemiesKeyList.size(), false, null) - 1));
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
        wizardChosenSpell = wizard.returnTypedSpellsFromInt(attackMoveType, returnChoiceInt(1, wizard.returnTypedSpellsList(attackMoveType).size(), false, null) - 1);

        // IF THE SPELL IS READY IT WILL BE USED, OTHERWISE IT WILL PROMPT FOR ANOTHER SPELL
        while (!wizard.checkSpellReady(wizardChosenSpell)) {
            System.out.println(returnColoredText(wizardChosenSpell.getSpellName() + " will be ready in " + wizardChosenSpell.getSpellReadyIn() + " turn(s).", ANSI_RED));
            System.out.println(returnColoredText("Choose another spell.", ANSI_BLUE));
            wizardChosenSpell = wizard.returnTypedSpellsFromInt(attackMoveType, returnChoiceInt(1, wizard.returnTypedSpellsList(attackMoveType).size(), false, null) - 1);
        }

        // CALCULATE THE SPELL DAMAGE BASED ON OTHER BUFFS AND THEN ATTACK THE CHOSEN ENEMY
        wizardCalculatedDamage = wizard.returnSpellCalculatedDamage(wizardChosenSpell, enemyVictim);

        int generateRandomMoveType = (int) generateDoubleBetween(0, moveTypeList.size() - 1);
        enemyMoveType = MoveType.setMoveType(moveTypeList.get(generateRandomMoveType));

        // CHECK IF ENEMY IS A BOSS AND HIS HP LIMIT HAS BEEN REACHED AND RETURN IF CHOSEN SPELL CAN AFFECT THE ENEMY OR NOT
        boolean isVulnerableSpell = enemyVictim.vulnerabilityChecker(enemyVictim.getEnemyName().getEnemyHpLimitRatio(), wizardChosenSpell);

        if(isVulnerableSpell) {
            // EXECUTE ACTION BASED ON RANDOM CHOICE
            if (enemyMoveType == MoveType.DODGE) {
                enemyDodgeOrParrySuccess = enemyVictim.dodgeSpell(wizard.returnSpellChance(wizardChosenSpell), wizard);
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

    public static void chooseLevelAction(boolean switchTeams) {
        List<String> actionList = new ArrayList<>(Arrays.asList("Fight!", "Check Stats", "Use Potion"));
        printColoredHeader("What would you like to do?");
        if(switchTeams && wizard.getHouseName() == HouseName.SLYTHERIN) {
            printTitle(returnColoredText("You have the possibility the join the enemies.", ANSI_RED));
            actionList.add("Switch Teams");
        }
        printChoices(actionList);

        int choice = returnChoiceInt(1, actionList.size(), false, null);

        switch (choice) {
            case 1 -> fight();
            case 2 -> wizard.printAllStringStats(0);
            case 3 -> wizard.usePotion();
            case 4 -> switchTeams();
        }
    }

    private static void switchTeams() {
        printTitle(returnColoredText("You have joined the enemies!", ANSI_RED));
        resetLevel();
        // TODO - MAYBE ADD SOMETHING HERE IF THE ENEMY JOINS THE ENEMY TEAM
        exit(0);
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
                                       int combatTimeout, boolean switchTeams) {

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
                    chooseLevelAction(switchTeams);
                    clearConsole();
                    combatTimeout--;
                }

                levelOutcome(enemyDeathLineCopy, graduationLine, level,
                        combatTimeout, supposedTimeout);
            }
            else {
                printTitle(returnColoredText("You coward!", ANSI_RED));
            }
        }

        resetLevel();
    }

    private static void resetLevel() {
        continuePromptExtra();
        ConsoleFunctions.chooseAction();
        EnemyName.resetBossVulnerableSpellsList();
    }

    private static void levelOutcome(List<String> enemyDeathLine, String graduationLine,
                                     Level level, int combatTimeout, boolean supposedTimeout) {
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
            wizard.setSpecPoints(wizard.getSpecPoints() + Wizard.wizardSpecs);
            wizard.reduceSpellsCooldown();
            unlockNextLevel(level);
        }
    }

    public static void battleArena() {
        Level level = Level.Battle_Arena;
        String objective = "Your objective is to defeat all the enemies in the arena.";
        List<String> enemyDeathLine = Collections.singletonList("All the enemies in the arena have been defeated.");
        String graduationLine = "You have graduated from the Battle Arena!";

        spawnEnemies();
        wizard.updateStats();

        levelRepetition(level, objective, enemyDeathLine, graduationLine, 100, false);
    }

    public static void level1() {
        Level level = Level.The_Philosophers_Stone;
        EnemyName enemyName = EnemyName.TROLL;
        String objective = "Your objective is to kill the troll by using Wingardium Leviosa.";
        int enemyMinLevel = 1;
        int enemyMaxLevel = (int) wizard.getLevel();
        int enemyAmount = 1;
        List<String> enemyDeathLine = Collections.singletonList(enemyName.getEnemyDeathLine().get(0));
        String graduationLine = "You graduated Hogwarts's first year, you are now a second year student.";

        generateEnemies(enemyMinLevel, enemyMaxLevel, enemyAmount, enemyName);
        levelRepetition(level, objective, enemyDeathLine, graduationLine, 100, false);

    }

    public static void level2() {
        Level level = Level.The_Chamber_of_Secrets;
        EnemyName enemyName = EnemyName.BASILISK;
        String objective;
        List<String> enemyDeathLine = new ArrayList<>();
        boolean gryffindorHouse = wizard.getHouseName() == HouseName.GRYFFINDOR;
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

        generateEnemies(enemyMinLevel, enemyMaxLevel, enemyAmount, enemyName);
        levelRepetition(level, objective, enemyDeathLine, graduationLine, 100, false);
    }

    public static void level3() {
        Level level = Level.The_Prisoner_of_Azkaban;
        EnemyName enemyName = EnemyName.DEMENTOR;
        String objective = "The dementors are everywhere! Your objective is to use Expectro Patronum to eliminate them.";
        List<String> enemyDeathLine = Collections.singletonList("All Dementors have been sent back to hell.");
        int enemyMinLevel = 3;
        int enemyMaxLevel = (int) wizard.getLevel();
        int enemyAmount = 5;
        String graduationLine = "You graduated Hogwarts's third year, you are now a fourth year student.";

        generateEnemies(enemyMinLevel, enemyMaxLevel, enemyAmount, enemyName);
        levelRepetition(level, objective, enemyDeathLine, graduationLine, 100, false);

    }
    public static void level4() {
        Level level = Level.The_Goblet_of_Fire;
        EnemyName enemyName = EnemyName.PETER_PETTIGREW;
        String objective = "You're in a cemetery where you see Voldemort and Peter Pettigrew.\n" +
                "Your only hope of escape is to defeat Peter Pettigrew by using accio.";
        List<String> enemyDeathLine = Collections.singletonList(enemyName.getEnemyDeathLine().get(0));
        int enemyMinLevel = 4;
        int enemyMaxLevel = (int) wizard.getLevel();
        int enemyAmount = 1;
        String graduationLine = "You graduated Hogwarts's fourth year, you are now a fifth year student.";

        generateEnemies(enemyMinLevel, enemyMaxLevel, enemyAmount, enemyName);
        levelRepetition(level, objective, enemyDeathLine, graduationLine, 100, false);
    }
    public static void level5() {
        Level level = Level.The_Order_of_the_Phoenix;
        EnemyName enemyName = EnemyName.DOLORES_UMBRIDGE;
        String objective = "It's time for your exams! Your objective is to distract Dolores Umbridge until the fireworks are ready.\n" +
                "Don't worry, your spells won't kill her (Or will they?)";
        List<String> enemyDeathLine = Arrays.asList(enemyName.getEnemyDeathLine().get(1), enemyName.getEnemyDeathLine().get(0));
        int enemyMinLevel = 6;
        int enemyMaxLevel = (int) wizard.getLevel();
        int enemyAmount = 1;
        String graduationLine = "You graduated Hogwarts's fifth year, you are now a sixth year student.";

        generateEnemies(enemyMinLevel, enemyMaxLevel, enemyAmount, enemyName);
        levelRepetition(level, objective, enemyDeathLine, graduationLine, 6, false);
    }
    public static void level6() {
        Level level = Level.The_Half_Blooded_Prince;
        EnemyName enemyName = EnemyName.DEATH_EATER;
        String objective = "The death eaters have invaded Hogwarts, your objective is to eliminate all of them.";
        List<String> enemyDeathLine = Collections.singletonList("All Death Eaters have been sent back to hell.");
        int enemyMinLevel = 8;
        int enemyMaxLevel = (int) wizard.getLevel();
        int enemyAmount = 5;
        String graduationLine = "You graduated Hogwarts's sixth year, you are now a seventh year student.";

        generateEnemies(enemyMinLevel, enemyMaxLevel, enemyAmount, enemyName);
        levelRepetition(level, objective, enemyDeathLine, graduationLine, 100, true);
    }
    public static void level7() {
        Level level = Level.The_Deathly_Hallows;
        EnemyName enemyName1 = EnemyName.VOLDEMORT;
        EnemyName enemyName2 = EnemyName.BELLATRIX_LESTRANGE;
        String objective = "You are now facing Voldemort and Bellatrix Lestrange. Eliminate Them.";
        List<String> enemyDeathLine = Collections.singletonList(enemyName1.getEnemyDeathLine().get(0) + "\n" + enemyName2.getEnemyDeathLine().get(0));
        int enemyMinLevel = 10;
        int enemyMaxLevel = 10;
        int enemyAmount = 1;
        String magician;
        if(wizard.getGender().equals(Gender.MALE)) {
            magician = "wizard";
        }
        else {
            magician = "witch";
        }
        String graduationLine = "You graduated Hogwarts's curriculum, you are now a fully fledged " + magician + ".";

        generateEnemies(enemyMinLevel, enemyMaxLevel, enemyAmount, enemyName1);
        generateEnemies(enemyMinLevel, enemyMaxLevel, enemyAmount, enemyName2);

        System.out.println(enemiesHashMap);
        System.out.println(enemiesKeyList);

        levelRepetition(level, objective, enemyDeathLine, graduationLine, 100, false);
    }
}