package AbstractClasses;

import Classes.*;
import Enums.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static Enums.EnumMethods.returnFormattedEnum;
import static Main.ConsoleFunctions.printSeparator;
import static Main.MechanicsFunctions.generateDoubleBetween;

@Getter
@Setter
public abstract class AbstractCharacter {
    public AbstractCharacter(String name, double healthPoints, double defensePoints, Difficulty difficulty, CharacterState characterState, List<AbstractItem> itemList, List<Potion> activePotionsList, List<Spell> spellList, double level) {
        this.name = name;
        this.healthPoints = healthPoints;
        this.defensePoints = defensePoints;
        this.difficulty = difficulty;
        this.characterState = characterState;
        this.itemList = itemList;
        this.activePotionsList = activePotionsList;
        this.spellList = spellList;
        this.level = level;
    }

    private String name;
    private double healthPoints;
    private double defensePoints;
    private Difficulty difficulty;
    private CharacterState characterState;
    private List<AbstractItem> itemList;
    private List<Potion> activePotionsList;
    private List<Spell> spellList;
    private double level;

    public final static int maxLevel = 10;

    // DIFFICULTY DIFFERENCE BETWEEN PLAYER AND ENEMY
    // =1 SAME STATS AS ENEMY
    // >1 ENEMIES HAVE BETTER STATS THAN THE PLAYER
    // <1 PLAYER HAS BETTER STATS THAN THE ENEMIES
    public final static double difficultyDifference = 1.3;

    public void deleteEnemy() {
        Enemy.enemies.removeIf(enemy1 -> enemy1.getName().equals(this.getName()));
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
        }
        else if (potionTypeActiveList.toArray().length >= maxPotionOfOneType){
            String text1 = returnFormattedEnum(potion.getPotionType()) + " potion is already active.";

            printSeparator(text1.length());
            System.out.println(text1);
            printSeparator(text1.length());
        }
        else if (activePotionList.toArray().length >= 3) {
            String text1 = "Cannot drink "+ returnFormattedEnum(potion.getPotionType()) +" potion, you can only have 3 potions active at once.";

            printSeparator(text1.length());
            System.out.println(text1);
            printSeparator(text1.length());
        }
    }

    public void updateSpells() {
        List<Spell> spellList = new ArrayList<>();

        for(Spell spell:Spell.getAllSpells()) {
            if(spell.getSpellLevelRequirement() <= this.getLevel()) {
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

    public double getSpellDamage(Spell spell, AbstractCharacter attackCharacter) {
        double[] spellDamage = spell.getSpellDamage();
        double spellRandomDamage;
        double spellBaseDamage = 0;

        double houseDamagePercent = 0;
        double housePotionPercent = 0;
        double strengthPercent = 0;

        if(spellDamage[0] != spellDamage[1]) {
            spellRandomDamage = generateDoubleBetween(spellDamage[0], spellDamage[1]);
        }
        else {
            spellRandomDamage = spellDamage[0];
        }

        // CHANGE SPELL BASE DAMAGE DEPENDING ON ENEMY OR PLAYER
        if(this.getClass() == Wizard.class) {
            // UPDATE WIZARD DAMAGE OR POTION EFFICIENCY BASED ON WHICH HOUSE THEY BELONG TO
            HouseName houseName = ((Wizard) this).getHouse().getHouseName();
            if(houseName == HouseName.SLYTHERIN) {
                houseDamagePercent = houseName.getSpecValue();
            }
            else if(houseName == HouseName.HUFFLEPUFF) {
                housePotionPercent = houseName.getSpecValue();
            }
            strengthPercent = ((Wizard) this).getWizardStatsPercent().get("strength");

            spellBaseDamage = Math.exp(this.getLevel() * difficulty.getWizardDiffMultiplier()) * spellRandomDamage ;
        }
        else if(this.getClass() == Enemy.class) {
            spellBaseDamage = (Math.exp(this.getLevel() * difficulty.getEnemyDiffMultiplier()) * spellRandomDamage) / 10;
        }



        System.out.println("spell base dmg: " + spellBaseDamage);

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
        double calculatedDamage;
        final int z = 2;

        if(spellDamage <= this.defensePoints && spellDamage >= 0) {
            calculatedDamage = (Math.pow(spellDamage, z)) / (this.defensePoints * z);
        }
        else {
            calculatedDamage = spellDamage - (this.defensePoints / z);
        }
        this.healthPoints -= (int) calculatedDamage;

        if(this.healthPoints <= 0) {
            this.deleteEnemy();
        }

        return calculatedDamage;

    }

    public double getSpellDefense(Spell spell) {
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

    public boolean checkSpellAvailability(Spell spell) {
        List<Spell> abstractCharacterSpellList = this.getSpellList();

        boolean spellExistsCharacterSpellList = abstractCharacterSpellList.stream().anyMatch(abstractCharacterSpell -> abstractCharacterSpell.equals(spell));
        boolean spellIsReady = spellExistsCharacterSpellList && spell.getSpellReadyIn() == 0;

        System.out.println(spell.getSpellReadyIn());

        if(spell.getSpellReadyIn() == 0) {
            System.out.println("yes");
        }

        return spellExistsCharacterSpellList && spellIsReady;
    }

    public void spellUsed(Spell spell) {
        spell.setSpellReadyIn(spell.getSpellCooldown());
    }

    public void reduceSpellsCooldown(int cooldown) {
        List<Spell> abstractCharacterSpellList = this.getSpellList();
        abstractCharacterSpellList
                .forEach(abstractCharacterSpell -> abstractCharacterSpell.setSpellReadyIn(abstractCharacterSpell.getSpellReadyIn() - cooldown));
    }

    public void reduceSpellsCooldown() {
        List<Spell> abstractCharacterSpellList = this.getSpellList();
        abstractCharacterSpellList
                .forEach(abstractCharacterSpell -> abstractCharacterSpell.setSpellReadyIn(0));
    }

    public void spellCast(Spell spell, AbstractCharacter attackCharacter) {
        double luckPercent = 0;
        if(this.getClass() == Wizard.class) {
            luckPercent = ((Wizard) this).getWizardStatsPercent().get("luck");
        }
        double spellSuccess = Math.random();
        double spellChance = spell.getSpellChance() * (1 + luckPercent);

        if(spellSuccess <= spellChance) {
            double spellDamage = getSpellDamage(spell, attackCharacter);
            double damageTaken = attackCharacter.takeDamage(spellDamage);
            double healthPoints = attackCharacter.getHealthPoints();

            String text1 = "You hit " + attackCharacter.getName() + " with " + (int) damageTaken + " damage!";
            String text2 = returnFormattedEnum(attackCharacter.getName()
                    + " "
                    + attackCharacter.getCharacterState()
                    + ".");
            String text3 = "Next attack will have "
                    + (int) (attackCharacter.getCharacterState().getDamageMultiplier() * 100)
                    + "% more damage.";
            String text4 = returnFormattedEnum(attackCharacter.getName())
                    + " has " + (int) healthPoints + " hp left.";
            String text5 = "You killed " + returnFormattedEnum(attackCharacter.getName());

            System.out.println(text1);
            System.out.println(text2);
            System.out.println(text3);

            if(healthPoints > 0) {

                System.out.println(text4);
            }
            else {
                System.out.println(text5);
            }

        }
        else {
            String text6 = "You missed your spell!";
            System.out.println(text6);
        }
    }

    public void attack(Spell spell, AbstractCharacter attackCharacter) {

//        SpellType spellType = spell.getSpellType();
//        String spellDescription = spell.getSpellDescription();

        String spellName = spell.getSpellName();
        String spellSpecialAttackLine = spell.getSpellSpecialAttackLine();

        double spellDefense = getSpellDefense(spell);
        double spellEffectiveDistance = getSpellEffectiveDistance(spell);

        // IF SPELL EXISTS IN THE CHARACTER'S SPELL LIST
        if(checkSpellAvailability(spell)) {
            // SPELL GETS USED AND PUT ON A COOLDOWN
            this.spellUsed(spell);

            // CONSOLE STUFF
            System.out.println(spellName+"!");
            System.out.println(spellSpecialAttackLine);

            // SPELL GETS CAST ON THE ATTACK CHARACTER
            spellCast(spell, attackCharacter);
        }
        else {
            System.out.println(spell.getSpellName() + " not available.");
        }
        printSeparator(30);
    }

//    public static double parry() {
//
//    }
//
//    public static boolean dodge() {
//
//    }


}
