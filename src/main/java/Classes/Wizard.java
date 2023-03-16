package Classes;

import AbstractClasses.AbstractCharacter;
import AbstractClasses.AbstractItem;
import Enums.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Getter
@Setter
public class Wizard extends AbstractCharacter {
    @Builder
    public Wizard(String name, double healthPoints, double defensePoints, Difficulty difficulty, CharacterState characterState, List<AbstractItem> itemList, List<Potion> activePotionsList, List<Spell> spellList, double level, String firstName, String lastName, Gender gender, Pet pet, Wand wand, House house, double experience, double charisma, double strength, double intelligence, double luck) {
        super(name, healthPoints, defensePoints, difficulty, characterState, itemList, activePotionsList, spellList, level);
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
    private final double levelIncrement = 0.2;

    public HashMap<String, Double> getWizardStatsPercent() {
        final int wizardStats = 4;
        final int dungeonLevels = 7;
        final double maxPercent = 0.25;
        int maxPoints = wizardStats * dungeonLevels;

        double strengthPercent = (this.getStrength() * maxPercent) / maxPoints;
        double luckPercent = (this.getLuck() * maxPercent) / maxPoints;
        double intelligencePercent = (this.getIntelligence() * maxPercent) / maxPoints;
        double charismaPercent = (this.getCharisma() * maxPercent) / maxPoints;

        HashMap<String, Double> WizardStatsPercent = new HashMap<String, Double>();
        WizardStatsPercent.put("strength", strengthPercent);
        WizardStatsPercent.put("luck", luckPercent);
        WizardStatsPercent.put("intelligence", intelligencePercent);
        WizardStatsPercent.put("charisma", charismaPercent);

        return  WizardStatsPercent;
    }
    // UPDATE WIZARD HEALTH AND DEFENSE POINTS BASED ON THEIR LEVEL
    public void updateWizardHpDf() {
        int wizardBaseHp = 100;
        int wizardBaseDp = 100;

        double wizardNewHp = (int) (this.getLevel() * 150 + wizardBaseHp);
        double wizardNewDp = (int) (this.getLevel() * 150 + wizardBaseDp);

        this.setHealthPoints(wizardNewHp);
        this.setDefensePoints(wizardNewDp);

    }


    // UPDATE WIZARD STATS BASED ON WHICH HOUSE THEY BELONG TO
    public void updateHouseSpec() {
        HouseName houseName = this.getHouse().getHouseName();
        double houseSpecValue = houseName.getSpecValue();

        if(houseName == HouseName.GRYFFINDOR) {
            this.setDefensePoints(this.getDefensePoints() * (1 + houseSpecValue));
        }
        else if(houseName == HouseName.RAVENCLAW) {
            this.setCharisma(this.getCharisma() + houseSpecValue);
        }
    }

    // UPDATE WIZARD LEVEL BASED ON XP THEY HAVE
    public void updateLevel() {
        double currentExperience = this.getExperience();
        for(double i = 0; i < AbstractCharacter.maxLevel * this.levelIncrement; i+= this.levelIncrement) {
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

    public void addExperience(double experience) {
        this.setExperience(this.getExperience() + experience);
    }

    public void updateStats() {
        this.updateLevel();
        this.updateSpells();
        this.updateWizardHpDf();
        this.updateHouseSpec();
    }

    public static House getHouseStat(Wizard wizard) {
        return wizard.getHouse();
    }

    public static Wizard testWizard = Wizard.builder()
            .healthPoints(200)
            .defensePoints(200)
            .difficulty(Difficulty.DEATH_WISH)
            .characterState(CharacterState.STANDING)
            .level(10)
            .firstName("Test")
            .lastName("Wizard")
            .name("Test Wizard")
            .gender(Gender.MALE)
            .pet(Pet.CAT)
            .wand(new Wand(Core.PHEONIX_FEATHER, 12))
            .house(new House(HouseName.SLYTHERIN))
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
