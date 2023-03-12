package Classes;

import AbstractClasses.AbstractEnemy;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class Enemy extends AbstractEnemy {
    public Enemy(double healthPoints, double defensePoints, List<Potion> potionList, List<Spell> spellList, double level, String name, double experiencePoints) {
        super(healthPoints, defensePoints, potionList, spellList, level, name, experiencePoints);
    }

//    public static Enemy testEnemy = new Enemy(100, 200, 20, 0.6, "enemyWizard", 150);

}
