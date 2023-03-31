package project.functions;

import project.abstractClasses.AbstractCharacter;
import project.classes.Enemy;
import project.classes.Spell;
import project.classes.Wizard;
import project.enums.*;

import java.util.*;

import static project.classes.Color.*;
import static project.enums.EnumMethods.returnFormattedEnum;
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

        ConsoleFunctions.printColoredHeader("Select enemy minimum level (1-" + AbstractCharacter.maxLevel + "): ");
        minLevel = ConsoleFunctions.returnChoiceInt(1, AbstractCharacter.maxLevel, false, null);
        ConsoleFunctions.printColoredHeader("Select enemy maximum level (1-" + AbstractCharacter.maxLevel + "): ");
        maxLevel = ConsoleFunctions.returnChoiceInt(minLevel, AbstractCharacter.maxLevel, false, null);

        ConsoleFunctions.printColoredHeader("Select enemy amount: ");
        enemyAmount = ConsoleFunctions.returnChoiceInt(0, 100, false, null);

        Enemy.generateEnemies(minLevel, maxLevel, enemyAmount);
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
        if(!Enemy.enemiesHashMap.isEmpty()) {
            boolean wizardDodgeOrParrySuccess = false;

            // CHOOSE A RANDOM ENEMY FROM THE ENEMY LIST TO ATTACK BACK
            randomEnemyIndex = (int) GeneralFunctions.generateDoubleBetween(0, Enemy.enemiesKeyList.toArray().length - 1);
            attackingEnemy = Enemy.enemiesHashMap.get(Enemy.enemiesKeyList.get(randomEnemyIndex));

            // CALCULATE THE ENEMY'S SPELL DAMAGE
            if(attackingEnemy.getEnemyName().getEnemyCombat() == EnemyCombat.SPELL) {
                // CHOOSE A RANDOM SPELL FROM THE ENEMY'S SPELL LIST
                randomEnemySpellIndex = (int) GeneralFunctions.generateDoubleBetween(0, attackingEnemy.returnTypedSpellsList(attackMoveType).size() - 1);
                enemyChosenSpell = attackingEnemy.returnTypedSpellsFromInt(attackMoveType, randomEnemySpellIndex);

                enemyCalculatedDamage = attackingEnemy.returnSpellCalculatedDamage(enemyChosenSpell, Wizard.wizard);
                parriedAttackName = enemyChosenSpell.getSpellName();
                dodgeChance = attackingEnemy.returnSpellChance(enemyChosenSpell);

                chosenSpellName = returnColoredText(enemyChosenSpell.getSpellName(), enemyChosenSpell.getSpellColor());
            }
            else if(attackingEnemy.getEnemyName().getEnemyCombat() == EnemyCombat.MELEE) {
                enemyCalculatedDamage = attackingEnemy.returnMeleeCalculatedDamage(Wizard.wizard);
                parriedAttackName = returnFormattedEnum(attackingEnemy.getEnemyName()) + " melee attack";
                dodgeChance = attackingEnemy.getEnemyName().getEnemyCombat().getCombatChance();

                chosenSpellName = returnColoredText("melee attack", ANSI_RED);
            }

            // NOTIFY THE PLAYER WHAT SPELL THE ENEMY WILL CHOOSE
            ConsoleFunctions.printTitle(returnColoredText(returnFormattedEnum(attackingEnemy.getEnemyName()), ANSI_PURPLE) +
                    returnColoredText(" Level " + (int) attackingEnemy.getLevel(), ANSI_YELLOW) + " will attack you with " +
                    chosenSpellName);

            // ASK THE PLAYER HIS ACTION
            ConsoleFunctions.printTitle("What will you do?");
            ConsoleFunctions.printChoices(moveTypeList);
            wizardMoveType = MoveType.setMoveType(moveTypeList.get(ConsoleFunctions.returnChoiceInt(1, moveTypeList.size(), false, null) - 1));

            // EXECUTE ACTION BASED ON PLAYER'S CHOICE
            if (wizardMoveType == MoveType.DODGE) {
                wizardDodgeOrParrySuccess = Wizard.wizard.dodgeSpell(dodgeChance, attackingEnemy);
            } else if (wizardMoveType == MoveType.PARRY) {
                wizardDodgeOrParrySuccess = Wizard.wizard.parry(parriedAttackName, attackingEnemy, enemyCalculatedDamage);
            }

            // IF DODGE OR PARRY FAILED, THE ENEMY WILL ATTACK
            if(!wizardDodgeOrParrySuccess) {
                if(attackingEnemy.getEnemyName().getEnemyCombat() == EnemyCombat.SPELL) {
                    assert enemyChosenSpell != null;
                    attackingEnemy.spellAttack(enemyChosenSpell, Wizard.wizard, enemyCalculatedDamage, true);
                }
                else if(attackingEnemy.getEnemyName().getEnemyCombat() == EnemyCombat.MELEE) {
                    attackingEnemy.meleeAttack(Wizard.wizard, enemyCalculatedDamage, true);
                }
            }

            ConsoleFunctions.continuePromptExtra();
        }
    }

    private static void wizardTurn(MoveType attackMoveType, List<String> moveTypeList) {
        Spell wizardChosenSpell;
        Enemy enemyVictim;
        double wizardCalculatedDamage;
        MoveType enemyMoveType;
        boolean enemyDodgeOrParrySuccess = false;

        // ACTIVATE POTIONS
        Wizard.wizard.applyPotionEffect();

        // CHOOSE THE ENEMY
        if(Enemy.enemiesHashMap.size() > 1) {
            // PRINT WIZARD STATS
            ConsoleFunctions.printTitle(Wizard.wizard.returnAllStringStats(7));

            ConsoleFunctions.printTitle("Choose an enemy.");
            Enemy.printEnemies();
            enemyVictim = Enemy.enemiesHashMap.get(Enemy.enemiesKeyList.get(ConsoleFunctions.returnChoiceInt(1, Enemy.enemiesKeyList.size(), false, null) - 1));
        }
        else {
            // PRINT WIZARD STATS
            ConsoleFunctions.printTitle(Wizard.wizard.returnAllStringStats(0));

            enemyVictim = Enemy.enemiesHashMap.get(Enemy.enemiesKeyList.get(0));
            System.out.println(enemyVictim.returnStringStats(0));
        }

        // WARN THE PLAYER ABOUT ENEMY CHANGES
        Enemy.checkEnemiesHpRatio();

        // CHOOSE THE SPELL
        ConsoleFunctions.printTitle("Choose the spell you want to use.");
        Wizard.wizard.printTypedSpells(attackMoveType);
        wizardChosenSpell = Wizard.wizard.returnTypedSpellsFromInt(attackMoveType, ConsoleFunctions.returnChoiceInt(1, Wizard.wizard.returnTypedSpellsList(attackMoveType).size(), false, null) - 1);

        // IF THE SPELL IS READY IT WILL BE USED, OTHERWISE IT WILL PROMPT FOR ANOTHER SPELL
        while (!Wizard.wizard.checkSpellReady(wizardChosenSpell)) {
            System.out.println(returnColoredText(wizardChosenSpell.getSpellName() + " will be ready in " + wizardChosenSpell.getSpellReadyIn() + " turn(s).", ANSI_RED));
            System.out.println(returnColoredText("Choose another spell.", ANSI_BLUE));
            wizardChosenSpell = Wizard.wizard.returnTypedSpellsFromInt(attackMoveType, ConsoleFunctions.returnChoiceInt(1, Wizard.wizard.returnTypedSpellsList(attackMoveType).size(), false, null) - 1);
        }

        // CALCULATE THE SPELL DAMAGE BASED ON OTHER BUFFS AND THEN ATTACK THE CHOSEN ENEMY
        wizardCalculatedDamage = Wizard.wizard.returnSpellCalculatedDamage(wizardChosenSpell, enemyVictim);

        int generateRandomMoveType = (int) GeneralFunctions.generateDoubleBetween(0, moveTypeList.size() - 1);
        enemyMoveType = MoveType.setMoveType(moveTypeList.get(generateRandomMoveType));

        // CHECK IF ENEMY IS A BOSS AND HIS HP LIMIT HAS BEEN REACHED AND RETURN IF CHOSEN SPELL CAN AFFECT THE ENEMY OR NOT
        boolean isVulnerableSpell = enemyVictim.vulnerabilityChecker(enemyVictim.getEnemyName().getEnemyHpLimitRatio(), wizardChosenSpell);

        if(isVulnerableSpell) {
            // EXECUTE ACTION BASED ON RANDOM CHOICE
            if (enemyMoveType == MoveType.DODGE) {
                enemyDodgeOrParrySuccess = enemyVictim.dodgeSpell(Wizard.wizard.returnSpellChance(wizardChosenSpell), Wizard.wizard);
            } else if (enemyMoveType == MoveType.PARRY) {
                enemyDodgeOrParrySuccess = enemyVictim.parry(wizardChosenSpell.getSpellName(), Wizard.wizard, wizardCalculatedDamage);
            }

            // IF DODGE OR PARRY FAILED, THE WIZARD WILL ATTACK
            if(!enemyDodgeOrParrySuccess) {
                Wizard.wizard.spellAttack(wizardChosenSpell, enemyVictim, wizardCalculatedDamage, true);
            }
        }
        ConsoleFunctions.continuePromptExtra();
    }

    public static void chooseLevelAction(boolean switchTeams) {
        List<String> actionList = new ArrayList<>(Arrays.asList("Fight!", "Check Stats", "Use Potion"));
        ConsoleFunctions.printColoredHeader("What would you like to do?");
        if(switchTeams && Wizard.wizard.getHouseName() == HouseName.SLYTHERIN) {
            ConsoleFunctions.printTitle(returnColoredText("You have the possibility the join the enemies.", ANSI_RED));
            actionList.add("Switch Teams");
        }
        ConsoleFunctions.printChoices(actionList);

        int choice = ConsoleFunctions.returnChoiceInt(1, actionList.size(), false, null);

        switch (choice) {
            case 1 -> fight();
            case 2 -> Wizard.wizard.printAllStringStats(0);
            case 3 -> Wizard.wizard.usePotion();
            case 4 -> switchTeams();
        }
    }

    private static void switchTeams() {
        ConsoleFunctions.printTitle(returnColoredText("You have joined the enemies!", ANSI_RED));
        resetLevel();
        // TODO - MAYBE ADD SOMETHING HERE IF THE ENEMY JOINS THE ENEMY TEAM
        exit(0);
    }

    public static boolean wizardHasRequiredSpell(Level level) {
        boolean hasRequiredSpell = true;
        if(level.getRequiredSpellList().size() > 0) {
            for (Spell spell : level.getRequiredSpellList()) {
                if(!Wizard.wizard.getSpellsKeyList().contains(spell.getSpellName()) && spell != Spell.legendarySword) {
                    ConsoleFunctions.printTitle(returnColoredText("You need to learn " + spell.getSpellName() + " before trying this level.", ANSI_RED));
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
            ConsoleFunctions.printColoredHeader(levelName);
            ConsoleFunctions.printTitle(objective);
            Enemy.checkEnemiesHpRatio();

            ConsoleFunctions.printTitle(returnColoredText("Do you accept the challenge?", ANSI_BLUE));
            boolean answer = ConsoleFunctions.returnYesOrNo();
            if(answer) {
                while (!Enemy.enemiesHashMap.isEmpty() && Wizard.wizard.getHealthPoints() > 0 && combatTimeout > 0) {
                    chooseLevelAction(switchTeams);
                    ConsoleFunctions.clearConsole();
                    combatTimeout--;
                }

                levelOutcome(enemyDeathLineCopy, graduationLine, level,
                        combatTimeout, supposedTimeout);
            }
            else {
                ConsoleFunctions.printTitle(returnColoredText("You coward!", ANSI_RED));
            }
        }

        resetLevel();
    }

    private static void resetLevel() {
        ConsoleFunctions.continuePromptExtra();
        ConsoleFunctions.chooseAction();
        EnemyName.resetBossVulnerableSpellsList();
    }

    private static void levelOutcome(List<String> enemyDeathLine, String graduationLine,
                                     Level level, int combatTimeout, boolean supposedTimeout) {
        if(combatTimeout == 0) {
            ConsoleFunctions.printTitle(enemyDeathLine.get(1));
        }
        else if (Enemy.enemiesHashMap.isEmpty()) {
            ConsoleFunctions.printTitle(enemyDeathLine.get(0));
        } else if (Wizard.wizard.getHealthPoints() <= 0) {
            ConsoleFunctions.printTitle(returnColoredText("You died.", ANSI_RED));
        }
        if((Enemy.enemiesHashMap.isEmpty() || supposedTimeout) && Wizard.wizard.getHealthPoints() > 0) {
            ConsoleFunctions.printTitle(returnColoredText(graduationLine, ANSI_YELLOW));
            Wizard.wizard.setSpecPoints(Wizard.wizard.getSpecPoints() + Wizard.wizardSpecs);
            Wizard.wizard.reduceSpellsCooldown();
            Level.unlockNextLevel(level);
        }
    }

    public static void battleArena() {
        Level level = Level.Battle_Arena;
        String objective = "Your objective is to defeat all the enemies in the arena.";
        List<String> enemyDeathLine = Collections.singletonList("All the enemies in the arena have been defeated.");
        String graduationLine = "You have graduated from the Battle Arena!";

        spawnEnemies();
        Wizard.wizard.updateStats();

        levelRepetition(level, objective, enemyDeathLine, graduationLine, 100, false);
    }

    public static void level1() {
        Level level = Level.The_Philosophers_Stone;
        EnemyName enemyName = EnemyName.TROLL;
        String objective = "Your objective is to kill the troll by using Wingardium Leviosa.";
        int enemyMinLevel = 1;
        int enemyMaxLevel = (int) Wizard.wizard.getLevel();
        int enemyAmount = 1;
        List<String> enemyDeathLine = Collections.singletonList(enemyName.getEnemyDeathLine().get(0));
        String graduationLine = "You graduated Hogwarts's first year, you are now a second year student.";

        Enemy.generateEnemies(enemyMinLevel, enemyMaxLevel, enemyAmount, enemyName);
        levelRepetition(level, objective, enemyDeathLine, graduationLine, 100, false);

    }

    public static void level2() {
        Level level = Level.The_Chamber_of_Secrets;
        EnemyName enemyName = EnemyName.BASILISK;
        String objective;
        List<String> enemyDeathLine = new ArrayList<>();
        boolean gryffindorHouse = Wizard.wizard.getHouseName() == HouseName.GRYFFINDOR;
        if(gryffindorHouse) {
            objective = "Your Objective is to kill the Basilisk with Godric Gryffindor's legendary Sword.";
            enemyDeathLine.add(enemyName.getEnemyDeathLine().get(0));
        }
        else {
            objective = "Your Objective is to kill the Basilisk by removing one of his teeth with Accio and then stabbing Tom Riddle's Journal.";
            enemyDeathLine.add(enemyName.getEnemyDeathLine().get(1));
        }
        int enemyMinLevel = 2;
        int enemyMaxLevel = (int) Wizard.wizard.getLevel();
        int enemyAmount = 1;
        String graduationLine = "You graduated Hogwarts's second year, you are now a third year student.";

        Enemy.generateEnemies(enemyMinLevel, enemyMaxLevel, enemyAmount, enemyName);
        levelRepetition(level, objective, enemyDeathLine, graduationLine, 100, false);
    }

    public static void level3() {
        Level level = Level.The_Prisoner_of_Azkaban;
        EnemyName enemyName = EnemyName.DEMENTOR;
        String objective = "The dementors are everywhere! Your objective is to use Expectro Patronum to eliminate them.";
        List<String> enemyDeathLine = Collections.singletonList("All Dementors have been sent back to hell.");
        int enemyMinLevel = 3;
        int enemyMaxLevel = (int) Wizard.wizard.getLevel();
        int enemyAmount = 5;
        String graduationLine = "You graduated Hogwarts's third year, you are now a fourth year student.";

        Enemy.generateEnemies(enemyMinLevel, enemyMaxLevel, enemyAmount, enemyName);
        levelRepetition(level, objective, enemyDeathLine, graduationLine, 100, false);

    }
    public static void level4() {
        Level level = Level.The_Goblet_of_Fire;
        EnemyName enemyName = EnemyName.PETER_PETTIGREW;
        String objective = "You're in a cemetery where you see Voldemort and Peter Pettigrew.\n" +
                "Your only hope of escape is to defeat Peter Pettigrew by using accio.";
        List<String> enemyDeathLine = Collections.singletonList(enemyName.getEnemyDeathLine().get(0));
        int enemyMinLevel = 4;
        int enemyMaxLevel = (int) Wizard.wizard.getLevel();
        int enemyAmount = 1;
        String graduationLine = "You graduated Hogwarts's fourth year, you are now a fifth year student.";

        Enemy.generateEnemies(enemyMinLevel, enemyMaxLevel, enemyAmount, enemyName);
        levelRepetition(level, objective, enemyDeathLine, graduationLine, 100, false);
    }
    public static void level5() {
        Level level = Level.The_Order_of_the_Phoenix;
        EnemyName enemyName = EnemyName.DOLORES_UMBRIDGE;
        String objective = "It's time for your exams! Your objective is to distract Dolores Umbridge until the fireworks are ready.\n" +
                "Don't worry, your spells won't kill her (Or will they?)";
        List<String> enemyDeathLine = Arrays.asList(enemyName.getEnemyDeathLine().get(1), enemyName.getEnemyDeathLine().get(0));
        int enemyMinLevel = 6;
        int enemyMaxLevel = (int) Wizard.wizard.getLevel();
        int enemyAmount = 1;
        String graduationLine = "You graduated Hogwarts's fifth year, you are now a sixth year student.";

        Enemy.generateEnemies(enemyMinLevel, enemyMaxLevel, enemyAmount, enemyName);
        levelRepetition(level, objective, enemyDeathLine, graduationLine, 6, false);
    }
    public static void level6() {
        Level level = Level.The_Half_Blooded_Prince;
        EnemyName enemyName = EnemyName.DEATH_EATER;
        String objective = "The death eaters have invaded Hogwarts, your objective is to eliminate all of them.";
        List<String> enemyDeathLine = Collections.singletonList("All Death Eaters have been sent back to hell.");
        int enemyMinLevel = 8;
        int enemyMaxLevel = (int) Wizard.wizard.getLevel();
        int enemyAmount = 5;
        String graduationLine = "You graduated Hogwarts's sixth year, you are now a seventh year student.";

        Enemy.generateEnemies(enemyMinLevel, enemyMaxLevel, enemyAmount, enemyName);
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
        if(Wizard.wizard.getGender().equals(Gender.MALE)) {
            magician = "wizard";
        }
        else {
            magician = "witch";
        }
        String graduationLine = "You graduated Hogwarts's curriculum, you are now a fully fledged " + magician + ".";

        Enemy.generateEnemies(enemyMinLevel, enemyMaxLevel, enemyAmount, enemyName1);
        Enemy.generateEnemies(enemyMinLevel, enemyMaxLevel, enemyAmount, enemyName2);

        System.out.println(Enemy.enemiesHashMap);
        System.out.println(Enemy.enemiesKeyList);

        levelRepetition(level, objective, enemyDeathLine, graduationLine, 100, false);
    }
}
