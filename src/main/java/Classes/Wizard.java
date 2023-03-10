package Classes;

import AbstractClasses.AbstractCharacter;
import Enums.Gender;
import Enums.Pet;
import lombok.*;

import java.util.List;
@Getter
@Setter
@Builder
public class Wizard extends AbstractCharacter {
    public Wizard(double healthPoints, double defensePoints, String firstName, String lastName, Gender gender, Pet pet, Wand wand, House house, List<Spell> knownSpells, List<Potions> potions, double experience, double charisma, double strength, double intelligence, double luck) {
        super(healthPoints, defensePoints);
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.pet = pet;
        this.wand = wand;
        this.house = house;
        this.knownSpells = knownSpells;
        this.potions = potions;
        this.experience = experience;
        this.charisma = charisma;
        this.strength = strength;
        this.intelligence = intelligence;
        this.luck = luck;
    }

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

//    private static Wizard wizard = new Wizard();


}
