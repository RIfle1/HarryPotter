package project.classes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.abstractClasses.AbstractCharacter;
import project.enums.*;
import project.functions.GeneralFunctions;
import project.javafx.controllers.GameSceneController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static project.classes.Color.*;
import static project.classes.Wizard.wizard;
import static project.enums.EnumMethods.returnFormattedEnum;
import static project.functions.ConsoleFunctions.*;
import static project.functions.GeneralFunctions.generateDoubleBetween;


@Getter
@Setter
public class Enemy extends AbstractCharacter {
    @Builder
    public Enemy(String name, double healthPoints, double defensePoints, double maxHealthPoints, double maxDefensePoints, Difficulty difficulty, CharacterState characterState, List<Potion> potionList, List<Potion> activePotionsList, HashMap<String, Spell> spellsHashMap, List<String> spellsKeyList, double level, EnemyName enemyName, EnemyCombat enemyCombat, double experiencePoints) {
        super(name, healthPoints, defensePoints, maxHealthPoints, maxDefensePoints, difficulty, characterState, potionList, activePotionsList, spellsHashMap, spellsKeyList, level);
        this.enemyName = enemyName;
        this.enemyCombat = enemyCombat;
        this.experiencePoints = experiencePoints;
    }


    private EnemyName enemyName;
    private EnemyCombat enemyCombat;
    private double experiencePoints;
    public static HashMap<String, Enemy> enemiesHashMap = new HashMap<>();
    public static List<String> enemiesKeyList = new ArrayList<>();
    private static final double enemyXpIncrement = 0.2;

    public static void clearEnemies() {
        enemiesKeyList.forEach(key -> enemiesHashMap.remove(key));
        enemiesKeyList.clear();
    }

    public void givePotion(Wizard wizard) {
        List<Potion> enemyItemList = this.getPotionList();
        List<Potion> newPotionList = new ArrayList<>(wizard.getPotionList()) ;

        for (Potion potion : enemyItemList) {


            try {
                newPotionList.add(potion.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

        }

        wizard.setPotionList(newPotionList);
    }

    public void killEnemy() {
        enemiesHashMap.remove(this.getName());
        enemiesKeyList.remove(this.getName());
    }

    public static EnemyName generateRandomBasicEnemy() {
        List<EnemyName> allEnemyNames;
        allEnemyNames = EnemyName.returnAllEnemyNames();
        int randomInt = (int) generateDoubleBetween(0, allEnemyNames.toArray().length - 1);

        return allEnemyNames.get(randomInt);
    }

    public void checkHealth(Wizard wizard) {
        if (this.getHealthPoints() <= 0) {
            this.killEnemy();
            wizard.addExperience(this.getExperiencePoints());
            this.givePotion(wizard);
        }
    }

    public static List<Potion> generateRandomPotions(int potionNumber) {
        // GENERATE 3 RANDOM POTIONS FOR EACH ENEMY
        List<Potion> allPotionsList = Potion.returnAllPotions();
        List<Potion> randomPotions = new ArrayList<>();

        for (int y = 0; y < potionNumber; y++) {
            double potionChance = Math.random();
            int potionIndex = (int) generateDoubleBetween(0, allPotionsList.toArray().length - 1);
            Potion chosenPotion = allPotionsList.get(potionIndex);

            if (potionChance <= chosenPotion.getItemDropChance()) {
                try {
                    randomPotions.add(chosenPotion.clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }

        }
        return randomPotions;
    }

    public static void updateEnemiesKeyList(HashMap<String, Enemy> enemiesHashMap) {
        enemiesKeyList.clear();
        enemiesHashMap.forEach((key, value) -> enemiesKeyList.add(key));
    }

    public static void generateEnemiesSub(double minLevel, double maxLevel, EnemyName enemyName, Enemy[] enemies, int i) {
        int enemyLevel;
        if (minLevel == maxLevel) {
            enemyLevel = (int) minLevel;
        } else {
            enemyLevel = (int) generateDoubleBetween(minLevel, maxLevel);
        }

        int enemyHp = (int) Math.round(Math.exp(enemyLevel * wizard.getDifficulty().getEnemyDiffMultiplier()) * enemyName.getEnemyBaseHp());
        int enemyDp = (int) Math.round((Math.exp(enemyLevel * wizard.getDifficulty().getEnemyDiffMultiplier()) * enemyName.getEnemyBaseDp()) / 3);
        int enemyXp = (int) Math.round(enemyName.getEnemyXp() * (1 + enemyXpIncrement) * enemyLevel + enemyName.getEnemyXp());

        enemies[i] = Enemy.builder()
                .healthPoints(enemyHp)
                .defensePoints(enemyDp)
                .maxHealthPoints(enemyHp)
                .maxDefensePoints(enemyDp)
                .difficulty(wizard.getDifficulty())
                .characterState(CharacterState.STANDING)
                .potionList(generateRandomPotions(3))
                .activePotionsList(new ArrayList<>())
                .spellsHashMap(new HashMap<>())
                .spellsKeyList(new ArrayList<>())
                .level(enemyLevel)
                .name(returnFormattedEnum(enemyName) + "-" + (i + 1))
                .enemyName(enemyName)
                .experiencePoints(enemyXp)
                .build();

//        System.out.println(enemyName + " " + enemyLevel + " " + enemyXp);

        if (enemies[i].getEnemyName().getEnemyCombat() == EnemyCombat.SPELL) {
            enemies[i].updateSpellsHashMap();
        }
        enemiesHashMap.put(enemies[i].getName(), enemies[i]);
    }

    public static void generateEnemies(double minLevel, double maxLevel, int amount) {
        // ENEMIES LIST
        Enemy[] enemies = new Enemy[amount];
        for (int i = 0; i < amount; i++) {
            EnemyName enemyName = generateRandomBasicEnemy();
            generateEnemiesSub(minLevel, maxLevel, enemyName, enemies, i);
        }
        updateEnemiesKeyList(enemiesHashMap);
    }

    public static void generateEnemies(double minLevel, double maxLevel, int amount, EnemyName enemyName) {
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
            System.out.println(enemy.getValue().returnStringStats(0));
            index++;
        }
    }


    public void meleeAttack(boolean attackSucceeded, AbstractCharacter attackedCharacter, double calculatedDamage) {
        // CONSOLE STUFF
        System.out.println(returnColoredText(returnFormattedEnum(this.getEnemyName()) + " melee attack!", ANSI_RED));

        castAttack(attackSucceeded, CharacterState.STANDING, attackedCharacter, calculatedDamage);
    }

    public String getVulnerableSpellsList() {
        StringBuilder vulnerableSpellsList = new StringBuilder();
        this.getEnemyName().getVulnerableSpellList()
                .forEach(vulnerableSpell -> vulnerableSpellsList
                        .append("- ")
                        .append(returnColoredText(vulnerableSpell.getSpellName(), vulnerableSpell.getSpellColor()))
                        .append("\n"));
        vulnerableSpellsList.replace(vulnerableSpellsList.length() - 1, vulnerableSpellsList.length(), "");
        return vulnerableSpellsList.toString();
    }

    public boolean vulnerabilityChecker(double hpLimitRatio, Spell spell) {
        double hpLimit = this.getMaxHealthPoints() * hpLimitRatio;
        boolean isVulnerableSpell;

        if (this.getEnemyName().getEnemyType() == EnemyType.BOSS && this.getHealthPoints() <= hpLimit) {
            isVulnerableSpell = this.getEnemyName()
                    .getVulnerableSpellList()
                    .stream()
                    .anyMatch(vulnerableSpell -> vulnerableSpell.getSpellName().equals(spell.getSpellName()));
        } else {
            isVulnerableSpell = true;
        }

        if(!isVulnerableSpell) {
            String text = returnFormattedEnum(this.getEnemyName()) +
                    " is not affected by " +
                    returnColoredText(spell.getSpellName(), spell.getSpellColor()) + "." +
                    " Try to use:\n" +
                    getVulnerableSpellsList();
            printTitle(text);
            GameSceneController.updateConsoleTaStatic(text, true);
        }
        return isVulnerableSpell;
    }

    public void updateBossVulnerableSpellsList() throws CloneNotSupportedException {
        if(this.getEnemyName() == EnemyName.BASILISK) {
            if(wizard.getHouseName() == HouseName.GRYFFINDOR) {
                EnemyName.BASILISK.addVulnerableSpell(Spell.legendarySword.clone());
            }
            else {
                EnemyName.BASILISK.addVulnerableSpell(Spell.accio.clone());
            }
        }
        else if(this.getEnemyName() == EnemyName.TROLL) {
            EnemyName.TROLL.addVulnerableSpell(Spell.wingardiumLeviosa.clone());
        }
        else if(this.getEnemyName() == EnemyName.DEMENTOR) {
            EnemyName.DEMENTOR.addVulnerableSpell(Spell.expectroPatronum.clone());
        }
        else if(this.getEnemyName() == EnemyName.DEATH_EATER) {
            EnemyName.DEATH_EATER.addVulnerableSpell(Spell.sectumsempra.clone());
        }
    }

    public void checkHpRatio() {
        double hpLimit = this.getMaxHealthPoints() * this.getEnemyName().getEnemyHpLimitRatio();

        if(this.getEnemyName().getEnemyType() == EnemyType.BOSS && this.getHealthPoints() <= hpLimit && this.getEnemyName().getVulnerableSpellList().size() == 0) {

            try {
                this.updateBossVulnerableSpellsList();
            }
            catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            String text = returnFormattedEnum(this.getEnemyName()) + " is only " +  returnColoredText("vulnerable ", ANSI_YELLOW) + "to: \n" + getVulnerableSpellsList();
            printTitle(text);

            if(this.getEnemyName() == EnemyName.BASILISK && wizard.getHouseName() == HouseName.GRYFFINDOR) {
                printTitle(returnColoredText("You see a shiny object in the distance... You pick it up and realize it's Godric Gryffindor's legendary sword.", ANSI_GREEN));
                printTitle(returnColoredText("You can now use this legendary sword to defeat the Basilisk", ANSI_BLUE));
                try {
                    wizard.putSpellsHashMap(Spell.legendarySword.clone());
                }
                catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                updateSpellsKeyList(this.getSpellsHashMap());
            }
        }
    }

    public static void checkEnemiesHpRatio() {
        // TELL THE PLAYER THE CHANGES IN THE ENEMY
        enemiesHashMap.forEach((key, value) -> {
            value.checkHpRatio();
        });
    }

    public static int getEnemyNameMaxLength() {
        int maxLength = 0;
        for (Map.Entry<String, Enemy> enemy : enemiesHashMap.entrySet()) {
            if (enemy.getKey().length() > maxLength) {
                maxLength = enemy.getKey().length();
            }
        }
        return maxLength;
    }

    public double returnEnemyCalculatedDamage(Spell enemyChosenSpell) {
        double enemyCalculatedDamage = 0;

        if(this.getEnemyName().getEnemyCombat() == EnemyCombat.SPELL) {
            enemyCalculatedDamage = this.returnSpellCalculatedDamage(enemyChosenSpell, wizard);
        }
        else if(this.getEnemyName().getEnemyCombat() == EnemyCombat.MELEE) {
            enemyCalculatedDamage = this.returnMeleeCalculatedDamage(wizard);
        }

        return enemyCalculatedDamage;
    }

    public String returnAttackName(Spell enemyChosenSpell) {
        String attackName = "";

        if(this.getEnemyName().getEnemyCombat() == EnemyCombat.SPELL) {
            attackName = enemyChosenSpell.getSpellName();
        }
        else if(this.getEnemyName().getEnemyCombat() == EnemyCombat.MELEE) {
            attackName = returnFormattedEnum(this.getEnemyName()) + " melee attack";
        }

        return attackName;
    }

    public String returnChosenSpellName(Spell enemyChosenSpell) {
        String chosenSpellName = "";

        if(this.getEnemyName().getEnemyCombat() == EnemyCombat.SPELL) {
            chosenSpellName = returnColoredText(enemyChosenSpell.getSpellName(), enemyChosenSpell.getSpellColor());
        }
        else if(this.getEnemyName().getEnemyCombat() == EnemyCombat.MELEE) {
            chosenSpellName = returnColoredText("melee attack", ANSI_RED);
        }

        return chosenSpellName;
    }

    public static Spell getEnemyRandomSpell(Enemy attackingEnemy) {
        // CHOOSE A RANDOM SPELL FROM THE ENEMY'S SPELL LIST
        MoveType attackMoveType = MoveType.ATTACK;

        int randomEnemySpellIndex = (int) GeneralFunctions.generateDoubleBetween(0, attackingEnemy.returnTypedSpellsList(attackMoveType).size());
        return attackingEnemy.returnTypedSpellsFromInt(attackMoveType, randomEnemySpellIndex);
    }

    public static Enemy getRandomEnemy() {
        // CHOOSE A RANDOM ENEMY FROM THE ENEMY LIST TO ATTACK BACK
        int randomEnemyIndex = (int) GeneralFunctions.generateDoubleBetween(0, Enemy.enemiesKeyList.toArray().length - 1);
        return Enemy.enemiesHashMap.get(Enemy.enemiesKeyList.get(randomEnemyIndex));
    }

    public static void notifyEnemyChosenSpell(String chosenSpellName, Enemy attackingEnemy) {
        // NOTIFY THE PLAYER WHAT SPELL THE ENEMY WILL CHOOSE
        String text = returnColoredText(returnFormattedEnum(attackingEnemy.getEnemyName()), ANSI_PURPLE) +
                returnColoredText(" Level " + (int) attackingEnemy.getLevel(), ANSI_YELLOW) + " will attack you with " +
                chosenSpellName;
        printTitleTop(text);
        GameSceneController.updateConsoleTaStatic(text, false);
    }

}
