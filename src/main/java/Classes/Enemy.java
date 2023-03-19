package Classes;

import AbstractClasses.AbstractCharacter;
import AbstractClasses.AbstractItem;
import Enums.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static Enums.EnumMethods.returnFormattedEnum;
import static Main.MechanicsFunctions.generateDoubleBetween;


@Getter
@Setter
public class Enemy extends AbstractCharacter {
    @Builder
    public Enemy(String name, double healthPoints, double defensePoints, double maxHealthPoints, double maxDefensePoints, Difficulty difficulty, CharacterState characterState, List<AbstractItem> itemList, List<Potion> activePotionsList, HashMap<Spell, Spell> spellHashMap, double level, EnemyName enemyName, EnemyType enemyType, double experiencePoints, double distanceFromPlayer) {
        super(name, healthPoints, defensePoints, maxHealthPoints, maxDefensePoints, difficulty, characterState, itemList, activePotionsList, spellHashMap, level);
        this.enemyName = enemyName;
        this.enemyType = enemyType;
        this.experiencePoints = experiencePoints;
        this.distanceFromPlayer = distanceFromPlayer;
    }

    private EnemyName enemyName;
    private EnemyType enemyType;
    private double experiencePoints;
    private double distanceFromPlayer;
    public static HashMap<String, Enemy> enemiesHashMap = new HashMap<>();
    public static List<String> enemiesKeyList = new ArrayList<>();
    static final int enemyBaseHp = 100;
    static final int enemyBaseDp = 100;

    public static void clearEnemies() {
        enemiesKeyList.forEach(key -> enemiesHashMap.remove(key));
        enemiesKeyList.clear();
    }

    public void deleteEnemy() {
        enemiesHashMap.remove(this.getName());
    }

    public static List<AbstractItem> generateRandomPotions(int potionNumber) {
        // GENERATE 3 RANDOM POTIONS FOR EACH ENEMY
        List<Potion> allPotionsList = Potion.getAllPotions();
        List<AbstractItem> randomPotions = new ArrayList<>();

        for(int y = 0; y < potionNumber; y++) {
            double potionChance = Math.random();
            int potionIndex = (int) generateDoubleBetween(0, allPotionsList.toArray().length);
            Potion chosenPotion = allPotionsList.get(potionIndex - 1);

            if(potionChance <= chosenPotion.getItemDropChance()) {
                randomPotions.add(chosenPotion);
            }

        }
        return randomPotions;
    }

    public static void updateEnemiesKeyList(HashMap<String, Enemy> enemiesHashMap) {
        enemiesHashMap.forEach((key, value) -> enemiesKeyList.add(key));
    }

    public static HashMap<String, Enemy> generateEnemies(double minLevel, double maxLevel, int amount, EnemyName enemyName, Difficulty difficulty) throws CloneNotSupportedException {
        // ENEMIES LIST
        HashMap<String, Enemy> enemiesHashMap = new HashMap<>();
        Enemy[] enemies = new Enemy[amount];


        for(int i = 0; i < amount; i++) {
            int enemyLevel;
                if(minLevel == maxLevel) {
                    enemyLevel = (int) minLevel;
                }
                else {
                    enemyLevel = (int) generateDoubleBetween(minLevel, maxLevel);
                }

            int enemyHp = (int) Math.round(Math.exp(enemyLevel * difficulty.getEnemyDiffMultiplier()) * enemyBaseHp) ;
            int enemyDp = (int) Math.round((Math.exp(enemyLevel * difficulty.getEnemyDiffMultiplier()) * enemyBaseDp) / 3);

//            System.out.println("Level " + enemyLevel);
//            System.out.println("HP: " + enemyHp);
//            System.out.println("DF: " + enemyDp);
//            System.out.println("---------------");

            enemies[i] = Enemy.builder()
                    .healthPoints(enemyHp)
                    .defensePoints(enemyDp)
                    .maxHealthPoints(enemyHp)
                    .maxDefensePoints(enemyDp)
                    .difficulty(difficulty)
                    .characterState(CharacterState.STANDING)
                    .itemList(generateRandomPotions(3))
                    .activePotionsList(new ArrayList<>())
                    .spellHashMap(new HashMap<>())
                    .level(enemyLevel)
                    .name(returnFormattedEnum(enemyName)+"-"+(i+1))
                    .enemyName(enemyName)
                    .experiencePoints(0)
                    .distanceFromPlayer((int) generateDoubleBetween(0, 100))
                    .build();

            enemies[i].updateSpells();
            enemiesHashMap.put(enemies[i].getName(), enemies[i]);
        }
        updateEnemiesKeyList(enemiesHashMap);
        return enemiesHashMap;
    }





}
