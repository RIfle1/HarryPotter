package AbstractClasses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class AbstractEnemy extends AbstractCharacter {
    private String name;
    private double experiencePoints;
}
