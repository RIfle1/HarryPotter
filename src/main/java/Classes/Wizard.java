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
    public Wizard(double healthPoints, double defensePoints, List<Potion> potionList, List<Spell> spellList, double level, String firstName, String lastName, Gender gender, Pet pet, Wand wand, House house, double experience, double charisma, double strength, double intelligence, double luck) {
        super(healthPoints, defensePoints, potionList, spellList, level);
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.pet = pet;
        this.wand = wand;
        this.house = house;
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

    private double experience;
    private double charisma; // for dodging
    private double strength; // for more attack damage
    private double intelligence; // for parrying
    private double luck; // for more attackChance

    private final double baseLevelExperience = 100;
    private final int maxLevel = 10;
    private final double levelIncrement = 0.2;
    public static void updateLevel(Wizard wizard) {
        double currentExperience = wizard.getExperience();
        for(double i = 0; i < wizard.maxLevel * wizard.levelIncrement; i+= wizard.levelIncrement) {
            double requiredExperience = wizard.baseLevelExperience + wizard.baseLevelExperience * i;
            if(currentExperience / (requiredExperience) >= 1) {
                currentExperience -= requiredExperience;
                double previousLevel = wizard.getLevel();
                wizard.setLevel(previousLevel + 1);

            }
            else {
                break;
            }
        }
    }

    public static Wizard testWizard = Wizard.builder()
            .healthPoints(100)
            .defensePoints(200)
            .level(0)
            .firstName("Test Wizard First Name")
            .lastName("Test Wizard Last Name")
            .gender(Gender.MALE)
            .pet(Pet.CAT)
            .wand(new Wand(Core.PHEONIX_FEATHER, 12))
            .house(new House(HouseName.GRYFFINDOR))
            .spellList(new ArrayList<Spell>())
            .potionList(new ArrayList<Potion>())
            .experience(0)
            .charisma(0)
            .strength(0)
            .intelligence(0)
            .luck(0)
            .build();


}
