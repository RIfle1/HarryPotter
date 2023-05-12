package project.functions;

import project.abstractClasses.AbstractCharacter;
import project.classes.Enemy;
import project.classes.Spell;
import project.classes.Wizard;
import project.enums.*;
import project.fx.controllers.GameSceneController;

import java.util.*;

import static java.lang.System.exit;
import static project.classes.Color.*;
import static project.classes.Enemy.*;
import static project.classes.Wizard.wizard;
import static project.enums.EnumMethods.returnFormattedEnum;
import static project.functions.ConsoleFunctions.printTitle;
import static project.functions.ConsoleFunctions.printTitleTop;


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
        wizardTurn();
        enemyTurn();
    }

    private static void enemyTurn() {

        // IF ENEMY LIST ISN'T EMPTY
        if(!Enemy.enemiesHashMap.isEmpty()) {

            Enemy attackingEnemy = getRandomEnemy();
            Spell enemyChosenSpell = null;
            String chosenSpellName = "";

            if(attackingEnemy.getEnemyName().getEnemyCombat() == EnemyCombat.SPELL) {
                enemyChosenSpell = getEnemyRandomSpell(attackingEnemy);
                chosenSpellName = attackingEnemy.returnChosenSpellName(enemyChosenSpell);
            }
            else if(attackingEnemy.getEnemyName().getEnemyCombat() == EnemyCombat.MELEE) {
                chosenSpellName = "Melee Attack";
            }

            notifyEnemyChosenSpell(chosenSpellName, attackingEnemy);

            // =-------------------------------=
            // CREATE A LIST OF POSSIBLE MOVES FOR THE WIZARD
            List<String> moveTypeList = MoveType.returnMoveTypeListExcept(new ArrayList<>(Arrays.asList(MoveType.ATTACK, MoveType.FOLLOW_UP)));
            // ASK THE PLAYER HIS ACTION
            printTitle("What will you do?");
            ConsoleFunctions.printChoices(moveTypeList);
            MoveType wizardMoveType = MoveType.setMoveType(moveTypeList.get(ConsoleFunctions.returnChoiceInt(1, moveTypeList.size(), false, null) - 1));
            // =-------------------------------=

            wizardDodgeOrParry(attackingEnemy, enemyChosenSpell, wizardMoveType);

            // =-------------------------------=
            ConsoleFunctions.continuePromptExtra();
            // =-------------------------------=
        }
    }

    public static void wizardDodgeOrParry(Enemy attackingEnemy, Spell enemyChosenSpell, MoveType wizardMoveType) {
        double spellSuccess = Math.random();

        double enemyCalculatedDamage = 0;
        boolean dodgeSuccess = false;
        boolean parrySuccess = false;

        if(attackingEnemy.getEnemyName().getEnemyCombat() == EnemyCombat.SPELL) {
            enemyCalculatedDamage = attackingEnemy.returnSpellCalculatedDamage(enemyChosenSpell, wizard);
            double dodgeChance = attackingEnemy.returnSpellChance(enemyChosenSpell);

            dodgeSuccess = spellSuccess <= wizard.returnDodgeChance(attackingEnemy, dodgeChance);
            parrySuccess = wizard.returnParryChance() > enemyCalculatedDamage;
        }
        else if(attackingEnemy.getEnemyName().getEnemyCombat() == EnemyCombat.MELEE) {
            enemyCalculatedDamage = attackingEnemy.returnMeleeCalculatedDamage(wizard);
            double dodgeChance = attackingEnemy.getEnemyName().getEnemyCombat().getCombatChance();

            dodgeSuccess = spellSuccess <= wizard.returnDodgeChance(attackingEnemy, dodgeChance);
            parrySuccess = wizard.returnParryChance() > enemyCalculatedDamage;
        }


        // EXECUTE ACTION BASED ON PLAYER'S CHOICE
        String attackName = attackingEnemy.returnAttackName(enemyChosenSpell);

        boolean attackSucceeded = isAttackSucceeded(attackingEnemy, enemyChosenSpell);
        boolean wizardDodgeOrParrySuccess = isWizardDodgeOrParrySuccess(attackingEnemy, enemyCalculatedDamage, attackName, wizardMoveType, dodgeSuccess, parrySuccess, attackSucceeded);

        enemyAttack(attackSucceeded, wizardDodgeOrParrySuccess, enemyCalculatedDamage, attackingEnemy, enemyChosenSpell);
    }

    public static boolean isWizardDodgeOrParrySuccess(Enemy attackingEnemy, double enemyCalculatedDamage,
                                                      String attackName, MoveType wizardMoveType,
                                                      boolean dodgeSuccess, boolean parrySuccess, boolean attackSucceeded) {
        boolean wizardDodgeOrParrySuccess = false;
        if(attackSucceeded) {
            if (wizardMoveType == MoveType.DODGE) {
                wizardDodgeOrParrySuccess = wizard.dodge(dodgeSuccess, attackingEnemy);
            } else if (wizardMoveType == MoveType.PARRY) {
                wizardDodgeOrParrySuccess = wizard.parry(parrySuccess, attackName, attackingEnemy, enemyCalculatedDamage);
            }
        }
        return wizardDodgeOrParrySuccess;
    }

    public static void enemyAttack(boolean attackSucceeded, boolean wizardDodgeOrParrySuccess, double enemyCalculatedDamage, Enemy attackingEnemy, Spell enemyChosenSpell) {
        String spellSpecialAttackLine = "";
//        boolean attackSucceeded = false;
        CharacterState characterState = null;

        // IF DODGE OR PARRY FAILED AND THE ENEMY DIDN'T MISS THEIR SHOT
        if(!wizardDodgeOrParrySuccess) {
            if(attackingEnemy.getEnemyName().getEnemyCombat() == EnemyCombat.SPELL) {
//                attackSucceeded = Math.random() <= enemyChosenSpell.getSpellChance();
                spellSpecialAttackLine = returnColoredText(enemyChosenSpell.getSpellSpecialAttackLine(), ANSI_PURPLE);
                characterState = enemyChosenSpell.getCharacterState();
            }
            else if(attackingEnemy.getEnemyName().getEnemyCombat() == EnemyCombat.MELEE) {
//                attackSucceeded = Math.random() <= attackingEnemy.getEnemyName().getEnemyCombat().getCombatChance();
                spellSpecialAttackLine = returnColoredText(returnFormattedEnum(attackingEnemy.getEnemyName()) + " melee attack!", ANSI_RED);
                characterState = CharacterState.STANDING;
            }
            attackingEnemy.castAttack(attackSucceeded, characterState, wizard, enemyCalculatedDamage, spellSpecialAttackLine);
        }

    }

    public static boolean isAttackSucceeded(Enemy attackingEnemy, Spell enemyChosenSpell) {
        boolean attackSucceeded = false;
        if(attackingEnemy.getEnemyName().getEnemyCombat() == EnemyCombat.SPELL) {
            attackSucceeded = Math.random() <= enemyChosenSpell.getSpellChance();
        }
        else if(attackingEnemy.getEnemyName().getEnemyCombat() == EnemyCombat.MELEE) {
            attackSucceeded = Math.random() <= attackingEnemy.getEnemyName().getEnemyCombat().getCombatChance();
        }
        return attackSucceeded;
    }

    private static void wizardTurn() {
        MoveType attackMoveType = MoveType.ATTACK;
        Spell wizardChosenSpell;
        Enemy enemyVictim;

        // ACTIVATE POTIONS
        wizard.applyPotionEffect();

        // CHOOSE THE ENEMY
        if(Enemy.enemiesHashMap.size() > 1) {
            // PRINT WIZARD STATS
            printTitle(wizard.returnAllStringStats(7));

            printTitle("Choose an enemy.");
            Enemy.printEnemies();
            enemyVictim = Enemy.enemiesHashMap.get(Enemy.enemiesKeyList.get(ConsoleFunctions.returnChoiceInt(1, Enemy.enemiesKeyList.size(), false, null) - 1));
        }
        else {
            // PRINT WIZARD STATS
            printTitle(wizard.returnAllStringStats(0));

            enemyVictim = Enemy.enemiesHashMap.get(Enemy.enemiesKeyList.get(0));
            System.out.println(enemyVictim.returnStringStats(0));
        }

        // WARN THE PLAYER ABOUT ENEMY CHANGES
        Enemy.checkEnemiesHpRatio();

        // CHOOSE THE SPELL
        printTitle("Choose the spell you want to use.");
        wizard.printTypedSpells(attackMoveType);
        wizardChosenSpell = wizard.returnTypedSpellsFromInt(attackMoveType, ConsoleFunctions.returnChoiceInt(1, wizard.returnTypedSpellsList(attackMoveType).size(), false, null) - 1);

        // IF THE SPELL IS READY IT WILL BE USED, OTHERWISE IT WILL PROMPT FOR ANOTHER SPELL
        while (!wizard.checkSpellReady(wizardChosenSpell)) {
            System.out.println(returnColoredText(wizardChosenSpell.getSpellName() + " will be ready in " + wizardChosenSpell.getSpellReadyIn() + " turn(s).", ANSI_RED));
            System.out.println(returnColoredText("Choose another spell.", ANSI_BLUE));
            wizardChosenSpell = wizard.returnTypedSpellsFromInt(attackMoveType, ConsoleFunctions.returnChoiceInt(1, wizard.returnTypedSpellsList(attackMoveType).size(), false, null) - 1);
        }

        boolean attackSucceeded = Math.random() <= wizardChosenSpell.getSpellChance();

        // CHECK IF ENEMY IS A BOSS AND HIS HP LIMIT HAS BEEN REACHED AND RETURN IF CHOSEN SPELL CAN AFFECT THE ENEMY OR NOT
        boolean isVulnerableSpell = enemyVictim.vulnerabilityChecker(enemyVictim.getEnemyName().getEnemyHpLimitRatio(), wizardChosenSpell);

        // CALCULATE THE SPELL DAMAGE BASED ON OTHER BUFFS
        double wizardCalculatedDamage = wizard.returnSpellCalculatedDamage(wizardChosenSpell, enemyVictim);

        // GET PARRY AND DODGE SUCCESS
        double spellSuccess = Math.random();
        double dodgeChance = wizard.returnSpellChance(wizardChosenSpell);
        boolean dodgeSuccess = spellSuccess <= enemyVictim.returnDodgeChance(wizard, dodgeChance);
        boolean parrySuccess = enemyVictim.returnParryChance() > wizardCalculatedDamage;

        MoveType enemyMoveType = returnEnemyMoveType();

        wizardCombatSystem(wizardChosenSpell, enemyVictim, attackSucceeded, isVulnerableSpell, dodgeSuccess, parrySuccess, wizardCalculatedDamage, enemyMoveType);
        ConsoleFunctions.continuePromptExtra();
    }

    public static MoveType returnEnemyMoveType() {
        // CREATE A LIST OF MOVE TYPES THAT THE ENEMY CAN USE
        List<String> moveTypeList = MoveType.returnMoveTypeListExcept(new ArrayList<>(Arrays.asList(MoveType.ATTACK, MoveType.FOLLOW_UP)));
        // GENERATE A RANDOM MOVE TYPE
        int generateRandomMoveType = (int) GeneralFunctions.generateDoubleBetween(0, moveTypeList.size() - 1);
        // ENEMY RANDOM MOVE TYPE
        return MoveType.setMoveType(moveTypeList.get(generateRandomMoveType));
    }

    // CONSOLE AND GUI FUNCTION
    public static void wizardCombatSystem(Spell wizardChosenSpell, Enemy enemyVictim,
                                          boolean attackSucceeded, boolean isVulnerableSpell,
                                          boolean enemyDodgeSuccess, boolean enemyParrySuccess,
                                          double wizardCalculatedDamage, MoveType enemyMoveType) {
//        MoveType enemyMoveType;
        // INITIALIZE VARIABLE
        boolean enemyDodgeOrParrySuccess = false;

        // CREATE A LIST OF MOVE TYPES THAT THE ENEMY CAN USE
//        List<String> moveTypeList = MoveType.returnMoveTypeListExcept(new ArrayList<>(Arrays.asList(MoveType.ATTACK, MoveType.FOLLOW_UP)));

        // GENERATE A RANDOM MOVE TYPE
//        int generateRandomMoveType = (int) GeneralFunctions.generateDoubleBetween(0, moveTypeList.size() - 1);

        String spellName = wizardChosenSpell.getSpellName();
        String spellNameText = returnColoredText(spellName + "!", wizardChosenSpell.getSpellColor());


        if(isVulnerableSpell) {
            // PRINT IN CONSOLE
            printTitleTop(spellNameText);
            GameSceneController.updateConsoleTaStatic(spellNameText, false);

            // ENEMY RANDOM MOVE TYPE
//            enemyMoveType = MoveType.setMoveType(moveTypeList.get(generateRandomMoveType));


            // EXECUTE ACTION BASED ON RANDOM CHOICE
            if (enemyMoveType == MoveType.DODGE) {
                enemyDodgeOrParrySuccess = enemyVictim.dodge(enemyDodgeSuccess, wizard);
            } else if (enemyMoveType == MoveType.PARRY) {
                enemyDodgeOrParrySuccess = enemyVictim.parry(enemyParrySuccess, wizardChosenSpell.getSpellName(), wizard, wizardCalculatedDamage);
            }

            // IF DODGE OR PARRY FAILED, THE WIZARD WILL ATTACK
            if(!enemyDodgeOrParrySuccess) {
                String spellSpecialAttackLine = returnColoredText(wizardChosenSpell.getSpellSpecialAttackLine(), ANSI_PURPLE);
                wizard.castAttack(attackSucceeded, wizardChosenSpell.getCharacterState(), enemyVictim, wizardCalculatedDamage, spellSpecialAttackLine);
            }
        }

        // SPELL GETS USED AND PUT ON A COOLDOWN
        wizard.reduceSpellsCooldown(1);
        wizard.resetSpellReadyIn(wizardChosenSpell);
    }

    public static void chooseLevelAction(boolean switchTeams) {
        List<String> actionList = new ArrayList<>(Arrays.asList("Fight!", "Check Stats", "Use Potion"));
        ConsoleFunctions.printColoredHeader("What would you like to do?");
        if(switchTeams && wizard.getHouseName() == HouseName.SLYTHERIN) {
            printTitle(returnColoredText("You have the possibility the join the enemies.", ANSI_RED));
            actionList.add("Switch Teams");
        }
        ConsoleFunctions.printChoices(actionList);


        int choice = ConsoleFunctions.returnChoiceInt(1, actionList.size(), false, null);

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
            enemyDeathLineCopy.add(EnemyName.timeoutDeathLine);
            supposedTimeout = false;
        }

        if (wizardHasRequiredSpell(level)) {
            String levelName = returnFormattedEnum(level);
            ConsoleFunctions.printColoredHeader(levelName);
            printTitle(objective);
            Enemy.checkEnemiesHpRatio();

            printTitle(returnColoredText("Do you accept the challenge?", ANSI_BLUE));
            boolean answer = ConsoleFunctions.returnYesOrNo();
            if(answer) {
                while (!Enemy.enemiesHashMap.isEmpty() && wizard.getHealthPoints() > 0 && combatTimeout > 0) {
                    chooseLevelAction(switchTeams);
                    ConsoleFunctions.clearConsole();
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
        ConsoleFunctions.continuePromptExtra();
        ConsoleFunctions.chooseAction();
        EnemyName.resetBossVulnerableSpellsList();
    }

    private static void levelOutcome(List<String> enemyDeathLine, String graduationLine,
                                     Level level, int combatTimeout, boolean supposedTimeout) {
        if(combatTimeout == 0) {
            String text = enemyDeathLine.get(1);
            printTitle(text);
        }
        else if (Enemy.enemiesHashMap.isEmpty()) {
            String text = enemyDeathLine.get(0);
            printTitle(text);
        } else if (wizard.getHealthPoints() <= 0) {
            String text = returnColoredText("You died.", ANSI_RED);
            printTitle(text);
        }

        if((Enemy.enemiesHashMap.isEmpty() || supposedTimeout) && wizard.getHealthPoints() > 0) {
            printTitle(returnColoredText(graduationLine, ANSI_YELLOW));

            wizardEndLevelRewards(level);
        }
    }

    public static void wizardEndLevelRewards(Level level) {
        wizard.setSpecPoints(wizard.getSpecPoints() + Wizard.wizardSpecs);
        wizard.reduceSpellsCooldown();
        wizard.updateStats();
        Level.unlockNextLevel(level);
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

        Enemy.generateEnemies(enemyMinLevel, enemyMaxLevel, enemyAmount, enemyName);
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
            enemyDeathLine.add(enemyName.getEnemyDeathLine().get(1));
        }
        else {
            objective = "Your Objective is to kill the Basilisk by removing one of his teeth with Accio and then stabbing Tom Riddle's Journal.";
            enemyDeathLine.add(enemyName.getEnemyDeathLine().get(0));
        }

        int enemyMinLevel = 2;
        int enemyMaxLevel = (int) wizard.getLevel();
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
        int enemyMaxLevel = (int) wizard.getLevel();
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
        int enemyMaxLevel = (int) wizard.getLevel();
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
        int enemyMaxLevel = (int) wizard.getLevel();
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
        int enemyMaxLevel = (int) wizard.getLevel();
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
        if(wizard.getGender().equals(Gender.MALE)) {
            magician = "wizard";
        }
        else {
            magician = "witch";
        }
        String graduationLine = "You graduated Hogwarts's curriculum, you are now a fully fledged " + magician + ".";

        Enemy.generateEnemies(enemyMinLevel, enemyMaxLevel, enemyAmount, enemyName1);
        Enemy.generateEnemies(enemyMinLevel, enemyMaxLevel, enemyAmount, enemyName2);

        levelRepetition(level, objective, enemyDeathLine, graduationLine, 100, false);
    }
}
