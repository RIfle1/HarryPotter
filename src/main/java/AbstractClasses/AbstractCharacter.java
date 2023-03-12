package AbstractClasses;

import Classes.Potion;
import Classes.Spell;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class AbstractCharacter {
    public AbstractCharacter(double healthPoints, double defensePoints, List<Potion> potionList, List<Spell> spellList, double level) {
        this.healthPoints = healthPoints;
        this.defensePoints = defensePoints;
        this.potionList = potionList;
        this.spellList = spellList;
        this.level = level;
    }

    private double healthPoints;
    private double defensePoints;
    private List<Potion> potionList;
    private List<Spell> spellList;
    private double level;

    public static void updateSpells(AbstractCharacter abstractCharacter) {
        List<Spell> spellList = new ArrayList<>();
        for(Spell spell:Spell.getAllSpells()) {
            if(spell.getSpellLevelRequirement() <= abstractCharacter.getLevel()) {
                spellList.add(spell);
            }
        }
        abstractCharacter.setSpellList(spellList);
    }

    public static void takeDamage(AbstractCharacter abstractCharacter, double attackDamage) {
        double calculatedDamage = (attackDamage / (abstractCharacter.defensePoints / attackDamage));
        abstractCharacter.healthPoints -= calculatedDamage;
    }
    public static double attack(double baseAttackDamage, double addedAttackDamage, double baseAttackChance, double addedAttackChance) {
        double attackChance = baseAttackChance + addedAttackChance;
        double attackDamage = baseAttackDamage + addedAttackDamage;

        double attackSuccess = Math.random();

        if(attackSuccess < attackChance) {
            System.out.println("You hit the enemy with " + attackDamage + " damage!");
            return attackDamage;
        }
        else {
            System.out.println("You missed your spell!");
            return 0;
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
