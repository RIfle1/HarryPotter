package Classes;

import AbstractClasses.AbstractCharacter;
import Enums.Gender;
import Enums.Pet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class Wizard extends AbstractCharacter {
    private String firstName;
    private String lastName;
    private Gender gender;
    private Pet pet;
    private Wand wand;
    private House house;
    private List<Spell> knownSpells;
    private List<Potions> potions;
    private double experience;
    private double charisma; // for dodging
    private double strength; // for more attack damage
    private double intelligence; // for parrying
    private double luck; // for more attackChance

}
