package AbstractClasses;

import Classes.Potion;
import Classes.Spell;
import lombok.*;

import java.util.List;


@Getter
@Setter
public abstract class AbstractEnemy extends AbstractCharacter {
    public AbstractEnemy(double healthPoints, double defensePoints, List<Potion> potionList, List<Spell> spellList, double level, String name, double experiencePoints) {
        super(healthPoints, defensePoints, potionList, spellList, level);
        this.name = name;
        this.experiencePoints = experiencePoints;
    }

    private String name;
    private double experiencePoints;

}
