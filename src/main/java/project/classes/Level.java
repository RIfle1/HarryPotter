package project.classes;

import lombok.Builder;
import lombok.Getter;
import project.enums.EnemyName;

import java.lang.reflect.Field;
import java.util.*;

import static project.classes.Wizard.wizard;

@Getter
@Builder
public class Level {

    private boolean unlocked;
    private String levelName;
    private List<Spell> requiredSpellList;
    private List<EnemyName> enemyNameList;
    private List<String> objectiveList;
    private int enemyMinLevel;
    private int enemyMaxLevel;
    private int enemyAmount;
    private String graduationLine;
    private int combatTimeout;
    private boolean switchTeams;

    public static Level The_Philosophers_Stone = Level.builder()
            .levelName("The Philosopher's Stone")
            .unlocked(true)
            .requiredSpellList(Collections.singletonList(Spell.wingardiumLeviosa))
            .enemyNameList(Collections.singletonList(EnemyName.TROLL))
            .objectiveList(Collections.singletonList("Your objective is to kill the troll by using Wingardium Leviosa."))
            .enemyMinLevel(1)
            .enemyMaxLevel((int) wizard.getLevel())
            .enemyAmount(1)
            .graduationLine("You graduated Hogwarts's first year, you are now a second year student.")
            .combatTimeout(100)
            .switchTeams(false)
            .build();

    public static Level The_Chamber_of_Secrets = Level.builder()
            .levelName("The Chamber of Secrets")
            .unlocked(false)
            .requiredSpellList(new ArrayList<>(Arrays.asList(Spell.legendarySword, Spell.accio)))
            .enemyNameList(Collections.singletonList(EnemyName.BASILISK))
            .objectiveList(new ArrayList<>(Arrays.asList("Your Objective is to kill the Basilisk by removing one of his teeth with Accio and then stabbing Tom Riddle's Journal.",
                    "Your Objective is to kill the Basilisk with Godric Gryffindor's legendary Sword.")))
            .enemyMinLevel(2)
            .enemyMaxLevel((int) wizard.getLevel())
            .enemyAmount(1)
            .graduationLine("You graduated Hogwarts's first year, you are now a third year student.")
            .combatTimeout(100)
            .switchTeams(false)
            .build();


    public static Level The_Prisoner_of_Azkaban = Level.builder()
            .levelName("The Prisoner of Azkaban")
            .unlocked(false)
            .requiredSpellList(Collections.singletonList(Spell.expectroPatronum))
            .enemyNameList(Collections.singletonList(EnemyName.DEMENTOR))
            .objectiveList(Collections.singletonList("The dementors are everywhere! Your objective is to use Expectro Patronum to eliminate them."))
            .enemyMinLevel(3)
            .enemyMaxLevel((int) wizard.getLevel())
            .enemyAmount(5)
            .graduationLine("You graduated Hogwarts's third year, you are now a fourth year student.")
            .combatTimeout(100)
            .switchTeams(false)
            .build();

    public static Level The_Goblet_of_Fire = Level.builder()
            .levelName("The Goblet of Fire")
            .unlocked(false)
            .requiredSpellList(Collections.singletonList(Spell.accio))
            .enemyNameList(Collections.singletonList(EnemyName.PETER_PETTIGREW))
            .objectiveList(Collections.singletonList("You're in a cemetery where you see Voldemort and Peter Pettigrew.\n" +
                    "Your only hope of escape is to defeat Peter Pettigrew by using accio."))
            .enemyMinLevel(4)
            .enemyMaxLevel((int) wizard.getLevel())
            .enemyAmount(1)
            .graduationLine("You graduated Hogwarts's fourth year, you are now a fifth year student.")
            .combatTimeout(100)
            .switchTeams(false)
            .build();

    public static Level The_Order_of_the_Phoenix = Level.builder()
            .levelName("The Order of the Phoenix")
            .unlocked(false)
            .requiredSpellList(new ArrayList<>())
            .enemyNameList(Collections.singletonList(EnemyName.DOLORES_UMBRIDGE))
            .objectiveList(Collections.singletonList("It's time for your exams! Your objective is to distract Dolores Umbridge until the fireworks are ready.\n" +
                    "Don't worry, your spells won't kill her (Or will they?)"))
            .enemyMinLevel(6)
            .enemyMaxLevel((int) wizard.getLevel())
            .enemyAmount(1)
            .graduationLine("You graduated Hogwarts's fifth year, you are now a sixth year student.")
            .combatTimeout(6)
            .switchTeams(false)
            .build();

    public static Level The_Half_Blooded_Prince = Level.builder()
            .levelName("The Half-Blooded Prince")
            .unlocked(false)
            .requiredSpellList(Collections.singletonList(Spell.sectumsempra))
            .enemyNameList(Collections.singletonList(EnemyName.DEATH_EATER))
            .objectiveList(Collections.singletonList("The death eaters have invaded Hogwarts, your objective is to eliminate all of them."))
            .enemyMinLevel(8)
            .enemyMaxLevel((int) wizard.getLevel())
            .enemyAmount(5)
            .graduationLine("You graduated Hogwarts's sixth year, you are now a seventh year student.")
            .combatTimeout(100)
            .switchTeams(true)
            .build();

    public static Level The_Deathly_Hallows = Level.builder()
            .levelName("The Deathly Hallows")
            .unlocked(false)
            .requiredSpellList(new ArrayList<>())
            .enemyNameList(new ArrayList<>(Arrays.asList(EnemyName.VOLDEMORT, EnemyName.BELLATRIX_LESTRANGE)))
            .objectiveList(Collections.singletonList("You are now facing Voldemort and Bellatrix Lestrange. Eliminate Them."))
            .enemyMinLevel(10)
            .enemyMaxLevel(10)
            .enemyAmount(1)
            .graduationLine("You graduated Hogwarts's seventh year, you are now a graduate.")
            .combatTimeout(100)
            .switchTeams(false)
            .build();

    public static Level Battle_Arena = Level.builder()
            .levelName("Battle Arena")
            .unlocked(true)
            .requiredSpellList(Collections.singletonList(Spell.avadaKedavra))
            .enemyNameList(new ArrayList<>())
            .objectiveList(Collections.singletonList("Your objective is to defeat all the enemies in the arena."))
            .enemyMinLevel(1)
            .enemyMaxLevel(1)
            .enemyAmount(1)
            .graduationLine("You defeated all the enemies in the Battle Arena!")
            .combatTimeout(100)
            .switchTeams(false)
            .build();

    public Level(boolean unlocked, String levelName, List<Spell> requiredSpellList, List<EnemyName> enemyNameList, List<String> objectiveList, int enemyMinLevel, int enemyMaxLevel, int enemyAmount, String graduationLine, int combatTimeout, boolean switchTeams) {
        this.unlocked = unlocked;
        this.levelName = levelName;
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

    public static List<Level> returnAllLevels(){
        List<Level> levelList = new ArrayList<>();
        Field[] declaredFields = Level.class.getDeclaredFields();

        for(Field field:declaredFields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                if (Level.class.isAssignableFrom(field.getType())) {
                    try {
                        levelList.add((Level) field.get(null));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return levelList;
    }

    public static List<String> returnAllLevelFields(){
        List<String> levelFields = new ArrayList<>();
        Field[] fields = Level.class.getDeclaredFields();

        for(Field field:fields) {
            if (java.lang.reflect.Modifier.isPrivate(field.getModifiers())) {
                levelFields.add(field.getName());
            }
        }
        return levelFields;
    }

    public static List<String> returnFieldsToOmit() {
        List<String> fieldsToOmit = returnAllLevelFields();
        fieldsToOmit.remove("unlocked");
        fieldsToOmit.remove("levelName");

        return fieldsToOmit;
    }

    public static List<String> returnLevelList() {
        List<String> levelNameList = new ArrayList<>();

        for(Level level: returnAllLevels()) {
            levelNameList.add(level.getLevelName());
        }
        return levelNameList;

    }

    public static List<String> returnUnlockedLevelsList() {
        return Level.returnAllLevels().stream().filter(Level::isUnlocked).map(Level::getLevelName).toList();

    }

    public static List<Level> returnAllUnlockedLevels() {
        return Level.returnAllLevels().stream().filter(Level::isUnlocked).toList();
    }

    public static void unlockNextLevel(Level previousLevel) {
        if(!previousLevel.equals(Level.Battle_Arena)) {
            Level nextLevel = setLevel(returnLevelList().get(returnLevelList().indexOf(previousLevel.getLevelName()) + 1));
            nextLevel.setUnlocked(true);
        }
    }

    public static Level setLevel(String levelName) {
        return returnAllLevels().stream()
                .filter(levelValue -> levelValue.getLevelName().equals(levelName))
                .findFirst().orElse(null);
    }

    public static void resetLevels() {
        returnAllLevels().forEach(level -> {
            if(!level.equals(Level.The_Philosophers_Stone)  && !level.equals(Level.Battle_Arena)) {
                level.setUnlocked(false);
            }
        });
    }
}
