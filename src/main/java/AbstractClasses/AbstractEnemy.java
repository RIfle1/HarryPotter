package AbstractClasses;

import lombok.*;


@Getter
@Setter
public abstract class AbstractEnemy extends AbstractCharacter {
    public AbstractEnemy(double healthPoints, double defensePoints, String name, double experiencePoints) {
        super(healthPoints, defensePoints);
        this.name = name;
        this.experiencePoints = experiencePoints;
    }

    private String name;
    private double experiencePoints;

}
