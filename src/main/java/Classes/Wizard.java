package Classes;

import AbstractClasses.AbstractCharacter;
import Enums.Core;
import Enums.Gender;
import Enums.HouseName;
import Enums.Pet;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class Wizard extends AbstractCharacter {
    @Builder
    public Wizard(double healthPoints, double defensePoints, String firstName, String lastName, Gender gender, Pet pet, Wand wand, House house, List<Spell> knownSpells, List<Potion> potions, double experience, double charisma, double strength, double intelligence, double luck) {
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
    private List<Potion> potions;
    private double experience;
    private double charisma; // for dodging
    private double strength; // for more attack damage
    private double intelligence; // for parrying
    private double luck; // for more attackChance

    public static Wizard wizard = Wizard.builder()
            .healthPoints(100)
            .defensePoints(200)
            .firstName("Test Wizard First Name")
            .lastName("Test Wizard Last Name")
            .gender(Gender.MALE)
            .pet(Pet.CAT)
            .wand(new Wand(Core.PHEONIX_FEATHER, 12))
            .house(new House(HouseName.GRYFFINDOR))
            .knownSpells(new ArrayList<Spell>())
            .potions(new ArrayList<Potion>())
            .experience(0)
            .charisma(0)
            .strength(0)
            .intelligence(0)
            .luck(0)
            .build();


}
