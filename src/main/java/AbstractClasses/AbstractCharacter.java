package AbstractClasses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class AbstractCharacter {
    private double healthPoints;
    private double defensePoints;
    private double attackDamage;
    private double attackChance;


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
