package Classes;

import AbstractClasses.AbstractEnemy;
import lombok.*;

@Getter
@Setter
public class Enemy extends AbstractEnemy {
    public Enemy(double healthPoints, double defensePoints, String name, double experiencePoints) {
        super(healthPoints, defensePoints, name, experiencePoints);
    }

//    public static Enemy testEnemy = new Enemy(100, 200, 20, 0.6, "enemyWizard", 150);

}
