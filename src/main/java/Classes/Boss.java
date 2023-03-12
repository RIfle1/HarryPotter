package Classes;

import AbstractClasses.AbstractEnemy;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class Boss extends AbstractEnemy {
    public Boss(double healthPoints, double defensePoints, List<Potion> potionList, List<Spell> spellList, double level, String name, double experiencePoints) {
        super(healthPoints, defensePoints, potionList, spellList, level, name, experiencePoints);
    }
//    public static Boss bossTest = new Boss();
}
