package AbstractClasses;

import Classes.*;
import Enums.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Classes.Color.*;
import static Classes.Spell.*;
import static Classes.Wizard.wizard;
import static Enums.EnumMethods.returnFormattedEnum;
import static Main.ConsoleFunctions.*;
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

    public String getStatBar(String statLogo, String statLogoColor, double value, double maxValue) {
        final int statBarLength = 30;

        int stat = (int) ((value / maxValue) * statBarLength);
        int empty = statBarLength - stat;

        return returnColoredText(statLogo, statLogoColor) +
                returnColoredText("[", Color.ANSI_CYAN) + returnColoredText("|", Color.ANSI_GREEN).repeat(Math.max(0, stat)) +
                returnColoredText("-", Color.ANSI_RED).repeat(Math.max(0, empty)) +
                returnColoredText("]", Color.ANSI_CYAN) +
                returnColoredText(" " + (int) value + "/" + (int) maxValue, ANSI_RED);
    }

    public String printStats() {
        String name;
        int nameLength;

        if (this.getClass() == Wizard.class) {
            name = this.getName();
            nameLength = this.getName().length();
        } else {
            name = returnFormattedEnum(((Enemy) this).getEnemyName());
            nameLength = getSpellNameMaxLength();
        }

        String column1Format = "%-" + (nameLength + 1) + "s";
        String column2Format = "%-" + 10 + "s";
        String column3Format = "%-" + 20 + "s";
        String column4Format = "%-" + 12 + "s";


        String column1 = returnColoredText(String.format(column1Format, name), Color.ANSI_PURPLE);
        String column2 = returnColoredText(String.format(column2Format, "Level " + (int) this.getLevel()), Color.ANSI_YELLOW);
        String column3 = String.format(column3Format ,getStatBar("❤", Color.ANSI_RED, this.getHealthPoints(), this.getMaxHealthPoints()));
        String column4 = returnColoredText(String.format(column4Format, (int) this.getDefensePoints() + " Defense"), ANSI_BLUE);

        return column1 + printColumnSeparator("<>") +
                column2 + printColumnSeparator("<>") +
                column3 + printColumnSeparator("<>") +
                column4;
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

            printLineSeparator(text2.length());
            System.out.println(text1);
            System.out.println(text2);
            printLineSeparator(text2.length());
        } else if (potionTypeActiveList.toArray().length >= maxPotionOfOneType) {
            String text1 = returnFormattedEnum(potion.getPotionType()) + " potion is already active.";

            printLineSeparator(text1.length());
            System.out.println(text1);
            printLineSeparator(text1.length());
        } else if (activePotionList.toArray().length >= 3) {
            String text1 = "Cannot drink " + returnFormattedEnum(potion.getPotionType()) + " potion, you can only have 3 potions active at once.";

            printLineSeparator(text1.length());
            System.out.println(text1);
            printLineSeparator(text1.length());
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

    public double takeDamage(double calculatedDamage) {
        this.healthPoints -= calculatedDamage;
        return calculatedDamage;
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
            System.out.printf("%-6s", "("+index+")");
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
            System.out.printf("%-6s", "("+index+")");
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

    public double getCalculatedDamage(Spell spell, AbstractCharacter attackedCharacter) {
        // CALCULATIONS FOR DAMAGE
        double spellDamage = getSpellDamage(spell, attackedCharacter);
        return spellDamage * (1 - (this.maxDefensePoints / this.maxHealthPoints) * maxDefenseReductionPercent);
    }

    public double getSpellChance(Spell spell) {
        double luckPercent = 0;
        if (this.getClass() == Wizard.class) {
            luckPercent = ((Wizard) this).getWizardStatsPercent().get("luck");
        }
        return spell.getSpellChance() * (1 + luckPercent);
    }

    public void spellCast(Spell spell, AbstractCharacter attackedCharacter, double calculatedDamage) {

        String attackedCharacterName = "";
        String attackingCharacterName = "";

        if(this.getClass() == Enemy.class) {
            attackingCharacterName = returnFormattedEnum(((Enemy) this).getEnemyName());
            attackedCharacterName = attackedCharacter.getName();
        }
        else if (this.getClass() == Wizard.class){
            attackingCharacterName = this.getName();
            attackedCharacterName = returnFormattedEnum(((Enemy) attackedCharacter).getEnemyName());
        }

        double spellSuccess = Math.random();
        if (spellSuccess <= getSpellChance(spell)) {
            // TAKE DAMAGE
            double damageTaken = attackedCharacter.takeDamage(calculatedDamage);
            // ADD EXPERIENCE
            if(attackedCharacter.getClass() == Enemy.class) {
                assert this instanceof Wizard;
                ((Enemy) attackedCharacter).checkHealth((Wizard) this);
            }

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
        String text5 = "";

        if(attackedCharacter.getClass() == Wizard.class) {
            text5 = attackingCharacterName + " killed you.";
        }
        else if(attackedCharacter.getClass() == Enemy.class) {
            text5 = "You killed " + attackedCharacterName + ".";
        }

        System.out.println(text1);
        if (healthPoints > 0) {
            if(attackedCharacter.getCharacterState() != CharacterState.STANDING) {
                System.out.println(text2);
            }
            System.out.println(text4);
        } else {
            System.out.println(text5);
        }
    }

    public void attack(Spell spell, AbstractCharacter attackCharacter, double calculatedDamage) {
        String spellName = spell.getSpellName();

        // TODO - USE SPELL EFFECTIVE DISTANCE
        double spellEffectiveDistance = getSpellEffectiveDistance(spell);

        // SPELL GETS USED AND PUT ON A COOLDOWN
        this.reduceSpellsCooldown(1);
        this.setSpellReadyIn(spell);

        // CONSOLE STUFF
        printLineSeparator(50);
        System.out.println(returnColoredText(spellName + "!", spell.getSpellColor()));
        spellCast(spell, attackCharacter, calculatedDamage);
        printLineSeparator(50);

    }

    public boolean dodge(Spell spell, AbstractCharacter attackingCharacter) {
        double spellSuccess = Math.random();
        double spellChance = getSpellChance(spell);
        String text1 = "";
        String text2 = "";

        if(this.getClass() == Wizard.class) {
            text1 = returnColoredText("You dodged " + returnFormattedEnum(((Enemy) attackingCharacter).getEnemyName()) + "'s attack.", ANSI_YELLOW);
            text2 = returnColoredText("You were unable to dodge " + returnFormattedEnum(((Enemy) attackingCharacter).getEnemyName()) + "'s attack.", ANSI_RED);
        }
        else if(this.getClass() == Enemy.class) {
            text1 = returnColoredText(returnFormattedEnum(((Enemy)this).getEnemyName())  + " dodged your attack.", ANSI_YELLOW);
            text2 = returnColoredText(returnFormattedEnum(((Enemy)this).getEnemyName()) + " was unable to dodge your attack.", ANSI_RED);
            spellChance = spellChance / 5;
        }

        if (spellSuccess <= spellChance) {
            printTitle(text1);
            return true;
        }
        else {
            printTitle(text2);
            return false;
        }

    }

    public boolean parry(Spell parrySpell, AbstractCharacter enemyAttacker, double calculatedDamage) {
        String text1 = "";
        String text2 = "";
        double newDamage = calculatedDamage * (1 + parryMultiplier);
        double defensePoints = this.getDefensePoints();

        if(this.getClass() == Wizard.class) {
            text1 = returnColoredText("You parried " + parrySpell.getSpellName() + " and returned ", ANSI_YELLOW)
                    + returnColoredText((int) newDamage + " Damage.", ANSI_RED);
            text2 = returnColoredText("You were unable to parry " + returnFormattedEnum(((Enemy) enemyAttacker).getEnemyName()) + "'s attack.", ANSI_RED);
        }
        else if(this.getClass() == Enemy.class) {
            text1 = returnColoredText(returnFormattedEnum(((Enemy)this).getEnemyName()) + " parried your attack and returned", ANSI_YELLOW)
                    + returnColoredText((int) newDamage + " Damage.", ANSI_RED);
            text2 = returnColoredText(returnFormattedEnum(((Enemy)this).getEnemyName()) + " was unable to parry your attack.", ANSI_RED);
            defensePoints = defensePoints / 3;
        }

        // THE ATTACK CAN BE PARRIED ONLY IF THE ATTACKED CHARACTER'S DEFENSE IS HIGHER THAN THE ATTACKING CHARACTER'S CALCULATED SPELL DAMAGE
        if(defensePoints > calculatedDamage) {
            printTitle(text1);
            this.attack(stupefy, enemyAttacker, newDamage);
            return true;
        }
        else {
            printTitle(text2);
            return false;
        }
    }
}
