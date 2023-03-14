package AbstractClasses;

import Classes.Enemy;
import Classes.Potion;
import Classes.Spell;
import Enums.CharacterState;
import Enums.PotionType;
import Enums.SpellType;
import Main.ConsoleFunctions;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Enums.EnumMethods.returnFormattedEnum;
import static Main.MechanicsFunctions.generateDoubleBetween;

@Getter
@Setter
public abstract class AbstractCharacter {
    public AbstractCharacter(double healthPoints, double defensePoints, List<AbstractItem> itemList, List<Potion> activePotionsList, List<Spell> spellList, double level) {
        this.healthPoints = healthPoints;
        this.defensePoints = defensePoints;
        this.itemList = itemList;
        this.activePotionsList = activePotionsList;
        this.spellList = spellList;
        this.level = level;
    }

    private double healthPoints;
    private double defensePoints;
    private List<AbstractItem> itemList;
    private List<Potion> activePotionsList;
    private List<Spell> spellList;
    private double level;

    public void deleteCharacter() {
        Enemy.enemies.remove();
    }

    public void addItem(AbstractItem abstractItem){
        List<AbstractItem> currentItemList = this.getItemList();
        currentItemList.add(abstractItem);
    }

    public List<String> getItemNameList() {
        List<AbstractItem> currentItemList = this.getItemList();
        List<String> currentItemNameList = new ArrayList<>();
        for(AbstractItem currentItem:currentItemList) {
            currentItemNameList.add(currentItem.getItemName());
        }

        return currentItemNameList;
    }

    public void drinkPotion(Potion potion) {
        final int maxPotionActiveAtOnce = 3;
        final int maxPotionOfOneType = 1;

        List<AbstractItem> currentItemList = this.getItemList();
        boolean potionIsInInventory =  currentItemList.stream().anyMatch(currentItem -> currentItem.equals(potion));

        List<Potion> activePotionList = this.getActivePotionsList();
        List<Potion> potionTypeActiveList = activePotionList
                .stream()
                .filter(activePotion -> activePotion.getPotionType().equals(potion.getPotionType()))
                .toList();


        // NEEDS TO BE OPTIMIZED FOR ALL TYPES OF POTIONS
        if(potionIsInInventory && potionTypeActiveList.toArray().length < maxPotionOfOneType && activePotionList.toArray().length < maxPotionActiveAtOnce) {
            activePotionList.add(potion);
            String text1 = "You just drank a " + potion.getItemName();
            String text2 = "Your "
                    + returnFormattedEnum(potion.getPotionType())
                    + " has been improved by "
                    + potion.getPotionValue()
                    + " points.";

            ConsoleFunctions.printSeparator(text2.length());
            System.out.println(text1);
            System.out.println(text2);
            ConsoleFunctions.printSeparator(text2.length());
        }
        else if (potionTypeActiveList.toArray().length >= maxPotionOfOneType){
            String text1 = returnFormattedEnum(potion.getPotionType()) + " potion is already active.";

            ConsoleFunctions.printSeparator(text1.length());
            System.out.println(text1);
            ConsoleFunctions.printSeparator(text1.length());
        }
        else if (activePotionList.toArray().length >= 3) {
            String text1 = "Cannot drink "+ returnFormattedEnum(potion.getPotionType()) +" potion, you can only have 3 potions active at once.";

            ConsoleFunctions.printSeparator(text1.length());
            System.out.println(text1);
            ConsoleFunctions.printSeparator(text1.length());
        }
    }

    public void updateSpells() {
        List<Spell> spellList = new ArrayList<>();

        for(Spell spell:Spell.getAllSpells()) {
            if((spell.getSpellLevelRequirement() <= this.getLevel())
                    && !(this.getClass() == Enemy.class && spell.getSpellType() == SpellType.ANCIENT)) {
                spellList.add(spell);
            }
        }
        this.setSpellList(spellList);
    }

    public List<String> getSpellNamesList() {
        List<String> spellNamesList = new ArrayList<>();

        for(Spell spell:this.getSpellList()) {
            spellNamesList.add(spell.getSpellName());
        }

        return spellNamesList;
    }

    public double getActivePotionValueSum(PotionType potionType) {
        List<Potion> activePotionList = this.activePotionsList;

        return activePotionList
                .stream()
                .filter(potion -> potion.getPotionType() == potionType)
                .map(Potion::getPotionValue)
                .reduce((double) 0, Double::sum);
    }

    public double getSpellDamage(Spell spell) {
        double[] spellDamage = spell.getSpellDamage();
        double spellCalculatedDamage;

        if(spellDamage[0] != spellDamage[1]) {
            spellCalculatedDamage = generateDoubleBetween(spellDamage[0], spellDamage[1]);
        }
        else {
            spellCalculatedDamage = spellDamage[0];
        }

        double potionDamagePercentIncrease = getActivePotionValueSum(PotionType.DAMAGE);
//        System.out.println(spellCalculatedDamage);
//        System.out.println(potionDamagePercentIncrease);

        return spellCalculatedDamage+(spellCalculatedDamage*potionDamagePercentIncrease);
    }

    public double takeDamage(double spellDamage) {
        double calculatedDamage;
        final int z = 2;

        if(spellDamage <= this.defensePoints && spellDamage >= 0) {
            calculatedDamage = (Math.pow(spellDamage, z)) / (this.defensePoints * z);
        }
        else {
            calculatedDamage = spellDamage - (this.defensePoints / z);
        }
        System.out.println(calculatedDamage);
        this.healthPoints -= (int) calculatedDamage;
        if(this.healthPoints <= 0) {
            System.out.println();
        }

        return calculatedDamage;
    }

    public  double getSpellDefense(Spell spell) {
        double[] spellDefense = spell.getSpellDefense();
        double spellCalculatedDefense;

        if(spellDefense[0] != spellDefense[1]) {
            spellCalculatedDefense = generateDoubleBetween(spellDefense[0], spellDefense[1]);
        }
        else {
            spellCalculatedDefense = spellDefense[0];
        }

        double potionDefensePointsIncrease = getActivePotionValueSum(PotionType.DEFENSE);

        return spellCalculatedDefense + potionDefensePointsIncrease;
    }

    public double getSpellEffectiveDistance(Spell spell) {
        double[] spellEffectiveDistance = spell.getSpellEffectiveDistance();
        double spellCalculatedEffectiveDistance;

        if(spellEffectiveDistance.length == 0) {
            spellCalculatedEffectiveDistance = -1;
        }
        else if(spellEffectiveDistance[0] != spellEffectiveDistance[1]) {
            spellCalculatedEffectiveDistance = generateDoubleBetween(spellEffectiveDistance[0], spellEffectiveDistance[1]);
        }
        else {
            spellCalculatedEffectiveDistance = spellEffectiveDistance[0];
        }

        return spellCalculatedEffectiveDistance;
    }



    public void attack(Spell spell, Enemy enemy) {

        String spellName = spell.getSpellName();
        SpellType spellType = spell.getSpellType();
        String spellDescription = spell.getSpellDescription();
        String spellSpecialAttackLine = spell.getSpellSpecialAttackLine();
        CharacterState characterState = spell.getCharacterState();

        double spellChance = spell.getSpellChance();
        int spellCoolDown = spell.getSpellCoolDown();

        double spellDamage = getSpellDamage(spell);
        double spellDefense = getSpellDefense(spell);
        double spellEffectiveDistance = getSpellEffectiveDistance(spell);

        double spellSuccess = Math.random();

        System.out.println(spellName+"!");
        if(spellSuccess <= spellChance) {
            double damageTaken = enemy.takeDamage(spellDamage);
            System.out.println("You hit the enemy with " + (int) damageTaken + " damage!");
            System.out.println(returnFormattedEnum(enemy.getEnemyName()) + " has " + (int) enemy.getHealthPoints() + " hp left.");

        }
        else {
            System.out.println("You missed your spell!");
        }
    }

//    public static double parry() {
//
//    }
//
//    public static boolean dodge() {
//
//    }


}
