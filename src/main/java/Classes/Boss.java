package Classes;

import AbstractClasses.AbstractEnemy;
import lombok.*;

@Getter
@Setter
public class Boss extends AbstractEnemy {
    public Boss(double healthPoints, double defensePoints, String name, double experiencePoints) {
        super(healthPoints, defensePoints, name, experiencePoints);
    }
//    public static Boss bossTest = new Boss();
}
