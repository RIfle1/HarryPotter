package Classes;

import AbstractClasses.AbstractCharacter;
import AbstractClasses.AbstractItem;
import Enums.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class Wizard extends AbstractCharacter {
    @Builder
    public Wizard(String name, double healthPoints, double defensePoints, CharacterState characterState, List<AbstractItem> itemList, List<Potion> activePotionsList, List<Spell> spellList, double level, String firstName, String lastName, Gender gender, Pet pet, Wand wand, House house, double experience, double charisma, double strength, double intelligence, double luck) {
        super(name, healthPoints, defensePoints, characterState, itemList, activePotionsList, spellList, level);
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


    public void updateLevel() {
        double currentExperience = this.getExperience();
        for(double i = 0; i < this.maxLevel * this.levelIncrement; i+= this.levelIncrement) {
            double requiredExperience = this.baseLevelExperience + this.baseLevelExperience * i;
            if(currentExperience / (requiredExperience) >= 1) {
                currentExperience -= requiredExperience;
                double previousLevel = this.getLevel();
                this.setLevel(previousLevel + 1);

            }
            else {
                break;
            }
        }
    }

    public void updateWizardStats(double addExperience) {
        this.setExperience(addExperience);
        this.updateLevel();
        this.updateSpells();
    }

    public static Wizard testWizard = Wizard.builder()
            .healthPoints(100)
            .defensePoints(200)
            .characterState(CharacterState.STANDING)
            .level(1)
            .firstName("Test Wizard First Name")
            .lastName("Test Wizard Last Name")
            .name("testWizard")
            .gender(Gender.MALE)
            .pet(Pet.CAT)
            .wand(new Wand(Core.PHEONIX_FEATHER, 12))
            .house(new House(HouseName.GRYFFINDOR))
            .spellList(new ArrayList<Spell>())
            .itemList(new ArrayList<AbstractItem>())
            .activePotionsList(new ArrayList<Potion>())
            .experience(0)
            .charisma(0)
            .strength(0)
            .intelligence(0)
            .luck(0)
            .build();

}
