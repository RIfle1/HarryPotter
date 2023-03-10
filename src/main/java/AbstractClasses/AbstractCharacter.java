package AbstractClasses;

import lombok.*;

@Getter
@Setter
public abstract class AbstractCharacter {

    public AbstractCharacter(double healthPoints, double defensePoints) {
        this.healthPoints = healthPoints;
        this.defensePoints = defensePoints;
    }

    private double healthPoints;
    private double defensePoints;

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
