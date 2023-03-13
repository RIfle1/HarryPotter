package Classes;

import AbstractClasses.AbstractCharacter;
import AbstractClasses.AbstractItem;
import Enums.Difficulty;
import Enums.EnemyName;
import Enums.EnemyType;
import Enums.EnumMethods;
import lombok.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Enums.EnumMethods.returnFormattedEnum;
import static Main.MechanicsFunctions.generateDoubleBetween;
import static java.lang.Long.sum;


@Getter
@Setter
public class Enemy extends AbstractCharacter {
    @Builder
    public Enemy(double healthPoints, double defensePoints, List<AbstractItem> itemList, List<Potion> activePotionsList, List<Spell> spellList, double level, EnemyName enemyName, EnemyType enemyType, double experiencePoints, double distanceFromPlayer) {
        super(healthPoints, defensePoints, itemList, activePotionsList, spellList, level);
        this.enemyName = enemyName;
        this.enemyType = enemyType;
        this.experiencePoints = experiencePoints;
        this.distanceFromPlayer = distanceFromPlayer;
    }

    private EnemyName enemyName;
    private EnemyType enemyType;
    private double experiencePoints;
    private double distanceFromPlayer;


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



    public static List<Enemy> generateEnemies(double minLevel, double maxLevel, int amount, EnemyName enemyName, Difficulty difficulty) {
        // ENEMIES LIST
        List<Enemy> enemiesList = new ArrayList<Enemy>();
        Enemy[] enemies = new Enemy[amount];


        for(int i = 0; i < amount; i++) {
            final double enemyHpIncreasePercent = 0.6;
            final double enemyDefenseIncreasePercent = 0.6;
            final int baseHp = 100;
            final int baseDefense = 100;
            int enemyLevel;
                if(minLevel == maxLevel) {
                    enemyLevel = (int) minLevel;
                }
                else {
                    enemyLevel = (int) generateDoubleBetween(minLevel, maxLevel);
                }

            int enemyHp = (int) (Math.exp(enemyLevel - enemyLevel * difficulty.getDifficultyMultiplier()) * baseHp + baseHp);
            int enemyDefense = (int) ((Math.exp(enemyLevel - enemyLevel * difficulty.getDifficultyMultiplier()) * baseDefense + baseDefense) / 2);

            System.out.println("Level " + enemyLevel);
            System.out.println("HP: " + enemyHp);
            System.out.println("DF: " + enemyDefense);
            System.out.println("---------------");

            enemies[i] = Enemy.builder()
                    .healthPoints(enemyHp)
                    .defensePoints(enemyDefense)
                    .itemList(generateRandomPotions(3))
                    .activePotionsList(new ArrayList<Potion>())
                    .spellList(new ArrayList<Spell>())
                    .level(enemyLevel)
                    .enemyName(enemyName)
                    .experiencePoints(0)
                    .distanceFromPlayer((int) generateDoubleBetween(0, 100))
                    .build();

            enemies[i].updateSpells();

            enemiesList.add(enemies[i]);
        }
        return enemiesList;
    }


    public static List<Enemy> getAllEnemies(){
        List<Enemy> enemyList = new ArrayList<>();
        Field[] declaredFields = Enemy.class.getDeclaredFields();

        for(Field field:declaredFields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                if (Enemy.class.isAssignableFrom(field.getType())) {
                    try {
                        enemyList.add((Enemy) field.get(null));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return enemyList;
    }

    public static List<String> getAllEnemysNamesList() {
        List<String> enemyNameList = new ArrayList<>();

        for(Enemy enemy: getAllEnemies()) {
            enemyNameList.add(returnFormattedEnum(enemy.getEnemyName()));
        }
        return enemyNameList;
    }

    public static List<String> getEnemysNamesList(List<Enemy> enemyList) {
        List<String> enemyNameList = new ArrayList<>();

        for(Enemy enemy: enemyList) {
            enemyNameList.add(returnFormattedEnum(enemy.getEnemyName()));
        }
        return enemyNameList;
    }


}
