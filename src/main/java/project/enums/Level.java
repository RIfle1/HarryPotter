package project.enums;

import lombok.Getter;
import project.classes.Spell;

import java.util.*;

import static project.classes.Wizard.wizard;
import static project.enums.EnumMethods.returnFormattedEnum;

@Getter
public enum Level {
    The_Philosophers_Stone(true,
            List.of(Spell.wingardiumLeviosa),
            List.of(EnemyName.TROLL),
            List.of("Your objective is to kill the troll by using Wingardium Leviosa."),
            1,
            (int) wizard.getLevel(),
            1,
            "You graduated Hogwarts's first year, you are now a second year student.",
            100,
            false),
    The_Chamber_of_Secrets(false,
            List.of(Spell.legendarySword, Spell.accio),
            List.of(EnemyName.BASILISK),
            List.of("Your Objective is to kill the Basilisk by removing one of his teeth with Accio and then stabbing Tom Riddle's Journal.",
                    "Your Objective is to kill the Basilisk with Godric Gryffindor's legendary Sword."),
            2,
            (int) wizard.getLevel(),
            1,
            "You graduated Hogwarts's second year, you are now a third year student.",
            100,
            false),
    The_Prisoner_of_Azkaban(false,
            List.of(Spell.expectroPatronum),
            List.of(EnemyName.DEMENTOR),
            List.of("The dementors are everywhere! Your objective is to use Expectro Patronum to eliminate them."),
            3,
            (int) wizard.getLevel(),
            5,
            "You graduated Hogwarts's third year, you are now a fourth year student.",
            100,
            false),
    The_Goblet_of_Fire(false,
            List.of(Spell.accio),
            List.of(EnemyName.PETER_PETTIGREW),
            List.of("You're in a cemetery where you see Voldemort and Peter Pettigrew.\n" +
                    "Your only hope of escape is to defeat Peter Pettigrew by using accio."),
            4,
            (int) wizard.getLevel(),
            1,
            "You graduated Hogwarts's fourth year, you are now a fifth year student.",
            100,
            false),
    The_Order_of_the_Phoenix(false,
            new ArrayList<>(),
            List.of(EnemyName.DOLORES_UMBRIDGE),
            List.of("It's time for your exams! Your objective is to distract Dolores Umbridge until the fireworks are ready.\n" +
                    "Don't worry, your spells won't kill her (Or will they?)"),
            6,
            (int) wizard.getLevel(),
            1,
            "You graduated Hogwarts's fifth year, you are now a sixth year student.",
            6,
            false),
    The_Half_Blooded_Prince(false,
            List.of(Spell.sectumsempra),
            List.of(EnemyName.DEATH_EATER),
            List.of("The death eaters have invaded Hogwarts, your objective is to eliminate all of them."),
            8,
            (int) wizard.getLevel(),
            5,
            "You graduated Hogwarts's sixth year, you are now a seventh year student.",
            100,
            true),
    The_Deathly_Hallows(false,
            new ArrayList<>(),
            List.of(EnemyName.VOLDEMORT, EnemyName.BELLATRIX_LESTRANGE),
            List.of("You are now facing Voldemort and Bellatrix Lestrange. Eliminate Them."),
            10,
            10,
            1,
            "You graduated Hogwarts's seventh year, you are now a graduate.",
            100,
            false),
    Battle_Arena(true,
            List.of(Spell.avadaKedavra),
            new ArrayList<>(),
            List.of("Your objective is to defeat all the enemies in the arena."),
            1,
            1,
            1,
            "You deafeated all the enemies in the Battle Arena!",
            100,
            false);

    private boolean unlocked;
    private final List<Spell> requiredSpellList;
    private final List<EnemyName> enemyNameList;
    private final List<String> objectiveList;
    private final int enemyMinLevel;
    private final int enemyMaxLevel;
    private final int enemyAmount;
    private final String graduationLine;
    private final int combatTimeout;
    private final boolean switchTeams;

    Level(boolean unlocked, List<Spell> requiredSpellList, List<EnemyName> enemyNameList, List<String> objectiveList, int enemyMinLevel, int enemyMaxLevel, int enemyAmount, String graduationLine, int combatTimeout, boolean switchTeams) {
        this.unlocked = unlocked;
        this.requiredSpellList = requiredSpellList;
        this.enemyNameList = enemyNameList;
        this.objectiveList = objectiveList;
        this.enemyMinLevel = enemyMinLevel;
        this.enemyMaxLevel = enemyMaxLevel;
        this.enemyAmount = enemyAmount;
        this.graduationLine = graduationLine;
        this.combatTimeout = combatTimeout;
        this.switchTeams = switchTeams;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public static List<String> returnLevelList() {
        Level[] levelValues = Level.values();
        return EnumMethods.getEnumList(levelValues);
    }

    public static List<String> returnUnlockedLevelsList() {
        Level[] levelValues = Arrays.stream(Level.values()).filter(Level::isUnlocked).toList().toArray(new Level[0]);
        return EnumMethods.getEnumList(levelValues);
    }

    public static List<Level> returnAllUnlockedLevelsList() {
        return Arrays.stream(Level.values()).filter(Level::isUnlocked).toList();
    }

    public static List<Level> returnAllLevels() {
        return Arrays.stream(Level.values()).toList();
    }

    public static void unlockNextLevel(Level previousLevel) {
        if(!previousLevel.equals(Level.Battle_Arena)) {
            Level nextLevel = setLevel(returnLevelList().get(returnLevelList().indexOf(returnFormattedEnum(previousLevel)) + 1));
            nextLevel.setUnlocked(true);
        }
    }

    public static Level setLevel(String level) {
        HashMap<String, Level> levelHashMap = new HashMap<>();
        Level[] levelValues = Level.values();

        for(Level levelValue:levelValues) {
            levelHashMap.put(returnFormattedEnum(levelValue), levelValue);
        }
        return levelHashMap.get(level);
    }
}
