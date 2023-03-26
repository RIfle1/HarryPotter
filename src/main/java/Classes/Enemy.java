package Classes;

import AbstractClasses.AbstractCharacter;
import AbstractClasses.AbstractItem;
import Enums.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Classes.Color.*;
import static Classes.Wizard.wizard;
import static Enums.EnumMethods.returnFormattedEnum;
import static Main.MechanicsFunctions.generateDoubleBetween;


@Getter
@Setter
public class Enemy extends AbstractCharacter {
    @Builder
    public Enemy(String name, double healthPoints, double defensePoints, double maxHealthPoints, double maxDefensePoints, Difficulty difficulty, CharacterState characterState, List<AbstractItem> itemList, List<Potion> activePotionsList, HashMap<String, Spell> spellsHashMap, List<String> spellsKeyList, double level, EnemyName enemyName, EnemyCombat enemyCombat, double experiencePoints, double distanceFromPlayer) {
        super(name, healthPoints, defensePoints, maxHealthPoints, maxDefensePoints, difficulty, characterState, itemList, activePotionsList, spellsHashMap, spellsKeyList, level);
        this.enemyName = enemyName;
        this.enemyCombat = enemyCombat;
        this.experiencePoints = experiencePoints;
        this.distanceFromPlayer = distanceFromPlayer;
    }

    private EnemyName enemyName;
    private EnemyCombat enemyCombat;
    private double experiencePoints;
    private double distanceFromPlayer;
    public static HashMap<String, Enemy> enemiesHashMap = new HashMap<>();
    public static List<String> enemiesKeyList = new ArrayList<>();
    private static final double enemyXpIncrement = 0.2;

    public static void clearEnemies() {
        enemiesKeyList.forEach(key -> enemiesHashMap.remove(key));
        enemiesKeyList.clear();
    }

    public void giveItems(Wizard wizard) {
        List<AbstractItem> enemyItemList = this.getItemList();
        for (AbstractItem item : enemyItemList) {
            wizard.getItemList().add(item);
        }
    }

    public void killEnemy() {
        enemiesHashMap.remove(this.getName());
        enemiesKeyList.remove(this.getName());
    }

    public static EnemyName generateRandomBasicEnemy() {
        List<EnemyName> allEnemyNames;
        allEnemyNames = EnemyName.getAllBasicEnemyNames();
        int randomInt = (int) generateDoubleBetween(0, allEnemyNames.toArray().length - 1);

        return allEnemyNames.get(randomInt);
    }

    public void checkHealth(Wizard wizard) {
        if (this.getHealthPoints() <= 0) {
            this.killEnemy();
            wizard.addExperience(this.getExperiencePoints());
            this.giveItems(wizard);
        }
    }

    public static List<AbstractItem> generateRandomPotions(int potionNumber) throws CloneNotSupportedException {
        // GENERATE 3 RANDOM POTIONS FOR EACH ENEMY
        List<Potion> allPotionsList = Potion.getAllPotions();
        List<AbstractItem> randomPotions = new ArrayList<>();

        for (int y = 0; y < potionNumber; y++) {
            double potionChance = Math.random();
            int potionIndex = (int) generateDoubleBetween(0, allPotionsList.toArray().length);
            Potion chosenPotion = allPotionsList.get(potionIndex - 1);

            if (potionChance <= chosenPotion.getItemDropChance()) {
                randomPotions.add(chosenPotion.clone());
            }

        }
        return randomPotions;
    }

    public static void updateEnemiesKeyList(HashMap<String, Enemy> enemiesHashMap) {
        enemiesHashMap.forEach((key, value) -> enemiesKeyList.add(key));
    }

    public static void generateEnemiesSub(double minLevel, double maxLevel, EnemyName enemyName, Enemy[] enemies, int i) throws CloneNotSupportedException {
        int enemyLevel;
        if (minLevel == maxLevel) {
            enemyLevel = (int) minLevel;
        } else {
            enemyLevel = (int) generateDoubleBetween(minLevel, maxLevel);
        }

        int enemyHp = (int) Math.round(Math.exp(enemyLevel * wizard.getDifficulty().getEnemyDiffMultiplier()) * enemyName.getEnemyBaseHp());
        int enemyDp = (int) Math.round((Math.exp(enemyLevel * wizard.getDifficulty().getEnemyDiffMultiplier()) * enemyName.getEnemyBaseDp()) / 3);
        int enemyXp = (int) Math.round(enemyName.getEnemyXp() * enemyXpIncrement * enemyLevel + enemyName.getEnemyXp());

        enemies[i] = Enemy.builder()
                .healthPoints(enemyHp)
                .defensePoints(enemyDp)
                .maxHealthPoints(enemyHp)
                .maxDefensePoints(enemyDp)
                .difficulty(wizard.getDifficulty())
                .characterState(CharacterState.STANDING)
                .itemList(generateRandomPotions(3))
                .activePotionsList(new ArrayList<>())
                .spellsHashMap(new HashMap<>())
                .spellsKeyList(new ArrayList<>())
                .level(enemyLevel)
                .name(returnFormattedEnum(enemyName) + "-" + (i + 1))
                .enemyName(enemyName)
                .experiencePoints(enemyXp)
                .distanceFromPlayer((int) generateDoubleBetween(0, 100))
                .build();

//        System.out.println(enemyName + " " + enemyLevel + " " + enemyXp);

        if (enemies[i].getEnemyCombat() == EnemyCombat.SPELL) {
            enemies[i].updateSpellsHashMap();
        }
        enemiesHashMap.put(enemies[i].getName(), enemies[i]);
    }

    public static void generateEnemies(double minLevel, double maxLevel, int amount) throws CloneNotSupportedException {
        // ENEMIES LIST
        Enemy[] enemies = new Enemy[amount];
        for (int i = 0; i < amount; i++) {
            EnemyName enemyName = generateRandomBasicEnemy();
            generateEnemiesSub(minLevel, maxLevel, enemyName, enemies, i);
        }
        updateEnemiesKeyList(enemiesHashMap);
    }

    public static void generateEnemies(double minLevel, double maxLevel, int amount, EnemyName enemyName) throws CloneNotSupportedException {
        // ENEMIES LIST
        Enemy[] enemies = new Enemy[amount];
        for (int i = 0; i < amount; i++) {
            generateEnemiesSub(minLevel, maxLevel, enemyName, enemies, i);
        }
        updateEnemiesKeyList(enemiesHashMap);
    }

    public static void printEnemies() {
        int index = 1;
        for (Map.Entry<String, Enemy> enemy : enemiesHashMap.entrySet()) {
            System.out.printf("%-15s", "(" + returnColoredText(index + "", ANSI_YELLOW) + ")");
            System.out.println(enemy.getValue().printStats());
            index++;
        }
    }


    public void meleeAttack(AbstractCharacter attackedCharacter, double calculatedDamage, boolean attackAfterCast) {
        // CONSOLE STUFF
        System.out.println(returnColoredText(returnFormattedEnum(this.getEnemyName()) + " melee attack!", ANSI_RED));
        castAttack(this.getEnemyCombat().getCombatChance(), CharacterState.STANDING, attackedCharacter, calculatedDamage, attackAfterCast);
    }
}
