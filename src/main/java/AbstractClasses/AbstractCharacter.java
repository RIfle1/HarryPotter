package AbstractClasses;

import Classes.*;
import Enums.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Classes.Color.*;
import static Enums.EnumMethods.returnFormattedEnum;
import static Main.ConsoleFunctions.printSeparator;
import static Main.MechanicsFunctions.generateDoubleBetween;

@Getter
@Setter
public abstract class AbstractCharacter {
    public AbstractCharacter(String name, double healthPoints, double defensePoints, double maxHealthPoints, double maxDefensePoints, Difficulty difficulty, CharacterState characterState, List<AbstractItem> itemList, List<Potion> activePotionsList, HashMap<String, Spell> spellsHashMap, List<String> spellsKeyList, double level) {
        this.name = name;
        this.healthPoints = healthPoints;
        this.defensePoints = defensePoints;
        this.maxHealthPoints = maxHealthPoints;
        this.maxDefensePoints = maxDefensePoints;
        this.difficulty = difficulty;
        this.characterState = characterState;
        this.itemList = itemList;
        this.activePotionsList = activePotionsList;
        this.spellsHashMap = spellsHashMap;
        this.spellsKeyList = spellsKeyList;
        this.level = level;
    }

    private String name;
    private double healthPoints;
    private double defensePoints;
    private double maxHealthPoints;
    private double maxDefensePoints;
    private Difficulty difficulty;
    private CharacterState characterState;
    private List<AbstractItem> itemList;
    private List<Potion> activePotionsList;
    private HashMap<String, Spell> spellsHashMap;
    private List<String> spellsKeyList;
    private double level;

    public final static int maxLevel = 10;

    // DIFFICULTY DIFFERENCE BETWEEN PLAYER AND ENEMY
    // =1 SAME STATS AS ENEMY
    // >1 ENEMIES HAVE BETTER STATS THAN THE PLAYER
    // <1 PLAYER HAS BETTER STATS THAN THE ENEMIES
    public final static double difficultyDifference = 1.3;

    public String getStatBar(String statLogo, String statLogoColor, String color, double value, double maxValue) {
        final int statBarLength = 30;

        int stat = (int) ((value / maxValue) * statBarLength);
        int empty = statBarLength - stat;

        return returnColoredText(statLogo, statLogoColor) +
                returnColoredText("[", Color.ANSI_CYAN) + returnColoredText("|", Color.ANSI_GREEN).repeat(Math.max(0, stat)) +
                returnColoredText("-", Color.ANSI_RED).repeat(Math.max(0, empty)) +
                returnColoredText("]", Color.ANSI_CYAN);
    }

    public String printStats() {
        String name;

        if (this.getClass() == Wizard.class) {
            name = this.getName();
        } else {
            name = returnFormattedEnum(((Enemy) this).getEnemyName());
        }

        return
                returnColoredText(name, Color.ANSI_PURPLE) +
                        returnColoredText(" <> ", Color.ANSI_WHITE) +
                        returnColoredText("Level " + (int) this.getLevel(), Color.ANSI_YELLOW) +
                        returnColoredText(" <> ", Color.ANSI_WHITE) +
                        getStatBar("❤", Color.ANSI_RED, "", this.getHealthPoints(), this.getMaxHealthPoints()) +
                        returnColoredText(" " + (int) this.getHealthPoints() + "/" + (int) this.getMaxHealthPoints(), ANSI_RED) +
                        returnColoredText(" <> ", Color.ANSI_WHITE) +
                        returnColoredText((int) this.getDefensePoints() + " Defense", ANSI_BLUE);
    }

    public void addItem(AbstractItem abstractItem) {
        List<AbstractItem> currentItemList = this.getItemList();
        currentItemList.add(abstractItem);
    }

    public List<String> getItemNameList() {
        List<AbstractItem> currentItemList = this.getItemList();
        List<String> currentItemNameList = new ArrayList<>();
        for (AbstractItem currentItem : currentItemList) {
            currentItemNameList.add(currentItem.getItemName());
        }

        return currentItemNameList;
    }

    public void drinkPotion(Potion potion) {
        final int maxPotionActiveAtOnce = 3;
        final int maxPotionOfOneType = 1;

        List<AbstractItem> currentItemList = this.getItemList();
        boolean potionIsInInventory = currentItemList.stream().anyMatch(currentItem -> currentItem.equals(potion));

        List<Potion> activePotionList = this.getActivePotionsList();
        List<Potion> potionTypeActiveList = activePotionList
                .stream()
                .filter(activePotion -> activePotion.getPotionType().equals(potion.getPotionType()))
                .toList();


        // NEEDS TO BE OPTIMIZED FOR ALL TYPES OF POTIONS
        if (potionIsInInventory && potionTypeActiveList.toArray().length < maxPotionOfOneType && activePotionList.toArray().length < maxPotionActiveAtOnce) {
            activePotionList.add(potion);
            String text1 = "You just drank " + potion.getItemName();
            String text2 = "Your "
                    + returnFormattedEnum(potion.getPotionType())
                    + " has been improved by "
                    + potion.getPotionValue()
                    + " points.";

            printSeparator(text2.length());
            System.out.println(text1);
            System.out.println(text2);
            printSeparator(text2.length());
        } else if (potionTypeActiveList.toArray().length >= maxPotionOfOneType) {
            String text1 = returnFormattedEnum(potion.getPotionType()) + " potion is already active.";

            printSeparator(text1.length());
            System.out.println(text1);
            printSeparator(text1.length());
        } else if (activePotionList.toArray().length >= 3) {
            String text1 = "Cannot drink " + returnFormattedEnum(potion.getPotionType()) + " potion, you can only have 3 potions active at once.";

            printSeparator(text1.length());
            System.out.println(text1);
            printSeparator(text1.length());
        }
    }

    public Spell getSpellFromInt(int number) {
        return this.getSpellsHashMap().get(this.getSpellsKeyList().get(number));
    }

    public Spell getTypedSpellsFromInt(MoveType spellType, int number) {
        return this.getAllSpells().stream().filter(spell -> spell.getSpellType().equals(spellType)).toList().get(number);
    }

    public void putSpellsHashMap(Spell spell) {
        this.spellsHashMap.put(spell.getSpellName(), spell);
    }

    public void updateSpellsKeyList(HashMap<String, Spell> spellsHashMap) {
        spellsKeyList.clear();
        spellsHashMap.forEach((key, value) -> spellsKeyList.add(key));
    }

    public void updateSpellsHashMap() throws CloneNotSupportedException {
        for (Spell spell : Spell.getAllSpells()) {
            if (spell.getSpellLevelRequirement() <= this.getLevel() && !this.getSpellsHashMap().containsKey(spell.getSpellName())) {
                this.putSpellsHashMap((Spell) spell.clone());
            }
        }
        updateSpellsKeyList(this.getSpellsHashMap());
    }

    public double getActivePotionValueSum(PotionType potionType) {
        List<Potion> activePotionList = this.activePotionsList;

        return activePotionList
                .stream()
                .filter(potion -> potion.getPotionType() == potionType)
                .map(Potion::getPotionValue)
                .reduce((double) 0, Double::sum);
    }

    public double getSpellDamage(Spell spell, AbstractCharacter attackCharacter) {
        double[] spellDamage = spell.getSpellDamage();
        double spellRandomDamage;
        double spellBaseDamage = 0;

        double houseDamagePercent = 0;
        double housePotionPercent = 0;
        double strengthPercent = 0;

        if (spellDamage[0] != spellDamage[1]) {
            spellRandomDamage = generateDoubleBetween(spellDamage[0], spellDamage[1]);
        } else {
            spellRandomDamage = spellDamage[0];
        }

        // CHANGE SPELL BASE DAMAGE DEPENDING ON ENEMY OR PLAYER
        if (this.getClass() == Wizard.class) {
            // UPDATE WIZARD DAMAGE OR POTION EFFICIENCY BASED ON WHICH HOUSE THEY BELONG TO
            HouseName houseName = ((Wizard) this).getHouse().getHouseName();
            if (houseName == HouseName.SLYTHERIN) {
                houseDamagePercent = houseName.getSpecValue();
            } else if (houseName == HouseName.HUFFLEPUFF) {
                housePotionPercent = houseName.getSpecValue();
            }
            strengthPercent = ((Wizard) this).getWizardStatsPercent().get("strength");

            spellBaseDamage = (Math.exp(this.getLevel() * getDifficulty().getWizardDiffMultiplier()) * spellRandomDamage) / 10;
        } else if (this.getClass() == Enemy.class) {
            spellBaseDamage = (Math.exp(this.getLevel() * getDifficulty().getEnemyDiffMultiplier()) * spellRandomDamage) / 15;
        }

//        System.out.println("spell base dmg: " + spellBaseDamage);

        double potionDamagePercentIncrease = getActivePotionValueSum(PotionType.DAMAGE) * (1 + housePotionPercent);
        double characterStateDamageMultiplier = attackCharacter.getCharacterState().getDamageMultiplier();
        attackCharacter.setCharacterState(spell.getCharacterState());

        return spellBaseDamage
                * (1 + potionDamagePercentIncrease)
                * (1 + characterStateDamageMultiplier)
                * (1 + houseDamagePercent)
                * (1 + strengthPercent);


    }

    public double takeDamage(double spellDamage) {
        final double maxDefenseReductionPercent = 0.2;
        double calculatedDamage = spellDamage * (1 - (this.maxDefensePoints / this.maxHealthPoints) * maxDefenseReductionPercent);
        this.healthPoints -= calculatedDamage;

        if (this.healthPoints <= 0) {
            if (this.getClass() == Enemy.class) {
                ((Enemy) this).deleteEnemy();
            }
//            else {
//
//            }
        }

        return calculatedDamage;

    }

    public double getSpellDefense(Spell spell) {
        double[] spellDefense = spell.getSpellDefense();
        double spellCalculatedDefense;

        if (spellDefense[0] != spellDefense[1]) {
            spellCalculatedDefense = generateDoubleBetween(spellDefense[0], spellDefense[1]);
        } else {
            spellCalculatedDefense = spellDefense[0];
        }

        double potionDefensePointsIncrease = getActivePotionValueSum(PotionType.DEFENSE);

        return spellCalculatedDefense + potionDefensePointsIncrease;
    }

    public double getSpellEffectiveDistance(Spell spell) {
        double[] spellEffectiveDistance = spell.getSpellEffectiveDistance();
        double spellCalculatedEffectiveDistance;

        if (spellEffectiveDistance.length == 0) {
            spellCalculatedEffectiveDistance = -1;
        } else if (spellEffectiveDistance[0] != spellEffectiveDistance[1]) {
            spellCalculatedEffectiveDistance = generateDoubleBetween(spellEffectiveDistance[0], spellEffectiveDistance[1]);
        } else {
            spellCalculatedEffectiveDistance = spellEffectiveDistance[0];
        }

        return spellCalculatedEffectiveDistance;
    }

    public boolean checkSpellReady(Spell spell) {
        return spell.getSpellReadyIn() == 0;
    }

    public boolean checkSpellAvailable(Spell spell) {
        HashMap<String, Spell> characterSpellHashMap = this.getSpellsHashMap();
        List<Spell> characterSpellList = new ArrayList<>();
        characterSpellHashMap.forEach((key, value) -> characterSpellList.add(value));
        return characterSpellList.stream().anyMatch(abstractCharacterSpell -> abstractCharacterSpell.equals(spell));
    }

    public void setSpellReadyIn(Spell spell) {
        spell.setSpellReadyIn(spell.getSpellCooldown());
    }

    public List<Spell> getAllSpells() {
        HashMap<String, Spell> characterSpellHashMap = this.getSpellsHashMap();
        List<Spell> characterSpellList = new ArrayList<>();

        characterSpellHashMap.forEach((key, value) -> characterSpellList.add(value));
        return characterSpellList;
    }

    public List<String> getAllSpellNames() {
        HashMap<String, Spell> characterSpellHashMap = this.getSpellsHashMap();
        List<String> allSpellNames = new ArrayList<>();

        characterSpellHashMap.forEach((key, value) -> allSpellNames.add(value.getSpellName()));
        return allSpellNames;
    }

    public void printAllCharacterSpells() {
        int index = 1;
        for (Map.Entry<String, Spell> spell : this.getSpellsHashMap().entrySet()) {
            System.out.print("(" + index + ") ");
            System.out.println(spell.getValue().printStats());
            index++;
        }

    }


    public List<Spell> getTypedSpellsList(MoveType spellType) {
        return this.getAllSpells().stream().filter(spell -> spell.getSpellType().equals(spellType)).toList();
    }


    public void printTypedSpells(MoveType spellType) {
        int index = 1;
        List<Spell> typedSpellsList = getTypedSpellsList(spellType);

        for (Spell spell : typedSpellsList) {
            System.out.print("(" + index + ") ");
            System.out.println(spell.printStats());
            index++;
        }
    }

    public void reduceSpellsCooldown(int cooldown) {
        HashMap<String, Spell> spellsHashMap = this.getSpellsHashMap();

        spellsHashMap.forEach((key, value) -> {
            int newValue = value.getSpellReadyIn() - cooldown;
            if (newValue < 0) {
                newValue = 0;
            }
            value.setSpellReadyIn(newValue);
        });
    }

    public void reduceSpellsCooldown() {
        HashMap<String, Spell> characterSpellHashMap = this.getSpellsHashMap();
        characterSpellHashMap.forEach((key, value) -> {
            value.setSpellReadyIn(0);
        });
    }

    public void spellCast(Spell spell, AbstractCharacter attackedCharacter) {
        double luckPercent = 0;
        if (this.getClass() == Wizard.class) {
            luckPercent = ((Wizard) this).getWizardStatsPercent().get("luck");
        }
        double spellSuccess = Math.random();
        double spellChance = spell.getSpellChance() * (1 + luckPercent);

        String attackedCharacterName;
        String attackingCharacterName;

        if(this.getClass() == Enemy.class) {
            attackingCharacterName = returnFormattedEnum(((Enemy) this).getEnemyName());
            attackedCharacterName = attackedCharacter.getName();
        }
        else {
            attackingCharacterName = this.getName();
            attackedCharacterName = returnFormattedEnum(((Enemy) attackedCharacter).getEnemyName());
        }

        if (spellSuccess <= spellChance) {
            double spellDamage = getSpellDamage(spell, attackedCharacter);
            double damageTaken = attackedCharacter.takeDamage(spellDamage);
            // TODO - GIVE EXPERIENCE TO PLAYER WHEN HE KILLS AN ENEMY
            double healthPoints = attackedCharacter.getHealthPoints();
            double maxHealthPoints = attackedCharacter.getMaxHealthPoints();
            spellAfterCast(attackedCharacter, attackedCharacterName, attackingCharacterName, (int) damageTaken, healthPoints, maxHealthPoints);

        } else {
            String text6 = attackingCharacterName + returnColoredText( " missed ", ANSI_YELLOW) + "their spell!";
            System.out.println(text6);
        }
    }

    private static void spellAfterCast(AbstractCharacter attackedCharacter, String attackedCharacterName, String attackingCharacterName, int damageTaken, double healthPoints, double maxHealthPoints) {
        String text1 = attackingCharacterName + " hit " + attackedCharacterName + " with " + returnColoredText(damageTaken + " damage", ANSI_RED) + "!";
        String text2 = attackedCharacterName + " is now " + returnColoredText(returnFormattedEnum(attackedCharacter.getCharacterState()), ANSI_BLUE) + ".";
        String text4 = attackedCharacterName + " has " + returnColoredText((int) healthPoints + "/" + (int) maxHealthPoints + " hp", ANSI_GREEN)  + " left.";
        String text5 = "You killed " + attackedCharacterName;

        System.out.println(text1);
        if (healthPoints > 0) {
            System.out.println(text2);
            System.out.println(text4);
        } else {
            System.out.println(text5);
        }
    }

    public void attack(Spell spell, AbstractCharacter attackCharacter) {

        String spellName = spell.getSpellName();

        // TODO - USE SPELL EFFECTIVE DISTANCE
        double spellEffectiveDistance = getSpellEffectiveDistance(spell);

        // SPELL GETS USED AND PUT ON A COOLDOWN
        this.reduceSpellsCooldown(1);
        this.setSpellReadyIn(spell);

        // CONSOLE STUFF
        printSeparator(50);
        System.out.println(returnColoredText(spellName + "!", spell.getSpellColor()));
        spellCast(spell, attackCharacter);
        printSeparator(50);

    }

    public void parry(Spell spell, AbstractCharacter abstractCharacter) {
        // TODO - USE SPELL DEFENSE DISTANCE
    }

    public void  dodge() {
        // TODO - CAN DODGE DEPENDING ON THE SUCCESS RATE OF THE SPELL
    }


}
