package Classes;

import AbstractClasses.AbstractCharacter;
import AbstractClasses.AbstractItem;
import Enums.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static Classes.Color.*;
import static Enums.EnumMethods.returnFormattedEnum;
import static Enums.HouseName.getHouseNameMaxLength;
import static Main.ConsoleFunctions.*;

@Getter
@Setter
public class Wizard extends AbstractCharacter {
    @Builder
    public Wizard(String name, double healthPoints, double defensePoints, double maxHealthPoints, double maxDefensePoints, Difficulty difficulty, CharacterState characterState, List<AbstractItem> itemList, List<Potion> activePotionsList, HashMap<String, Spell> spellsHashMap, List<String> spellsKeyList, double level, String firstName, String lastName, Gender gender, Pet pet, Wand wand, House house, double experience, double specPoints, double charisma, double strength, double intelligence, double luck) {
        super(name, healthPoints, defensePoints, maxHealthPoints, maxDefensePoints, difficulty, characterState, itemList, activePotionsList, spellsHashMap, spellsKeyList, level);
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.pet = pet;
        this.wand = wand;
        this.house = house;
        this.experience = experience;
        this.specPoints = specPoints;
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
    private double specPoints;
    private double charisma; // for dodging
    private double strength; // for more attack damage
    private double intelligence; // for parrying
    private double luck; // for more attackChance

    public static final int wizardBaseHp = 120;
    public static final int wizardBaseDp = 40;
    private final double baseLevelExperience = 100;
    private final double levelIncrement = 20;
    private final int wizardSpecs = 4;
    private final int dungeonLevels = 7;
    private final double maxPercent = 0.25;

    public String returnStringSpecs() {

        String column0Format = "%-" + getHouseNameMaxLength() + "s";
        String column1Format = "%-" + 14 + "s";
        String column2Format = "%-" + 14 + "s";
        String column3Format = "%-" + 14 + "s";
        String column4Format = "%-" + 14 + "s";

        String column0 = returnColoredText(String.format(column0Format , returnFormattedEnum(this.getHouse().getHouseName())), this.getHouse().getHouseName().getHouseColor());
        String column1 = returnColoredText(String.format(column1Format ,(int) this.getCharisma() + " Charisma"), ANSI_YELLOW);
        String column2 = returnColoredText(String.format(column2Format ,(int) this.getStrength() + " Strength"), ANSI_RED);
        String column3 = returnColoredText(String.format(column3Format ,(int) this.getIntelligence() + " Intelligence"), ANSI_BLUE);
        String column4 = returnColoredText(String.format(column4Format ,(int) this.getLuck() + " Luck"), ANSI_PURPLE);


        return printColumnSeparator("||") + column0 +
                printColumnSeparator("||") + column1 +
                printColumnSeparator("||") + column2 +
                printColumnSeparator("||") + column3 +
                printColumnSeparator("||") + column4;
    }

    public String returnAllStringStats(int extraNameLength) throws CloneNotSupportedException {
        this.updateStats();
        return this.returnStringStats(extraNameLength) + this.returnStringSpecs();
    }

    public HashMap<String, Double> getWizardSpecsPercent() {
        int maxPoints = wizardSpecs * dungeonLevels;

        double strengthPercent = (this.getStrength() * maxPercent) / maxPoints;
        double luckPercent = (this.getLuck() * maxPercent) / maxPoints;
        double intelligencePercent = (this.getIntelligence() * maxPercent) / maxPoints;
        double charismaPercent = (this.getCharisma() * maxPercent) / maxPoints;

        HashMap<String, Double> WizardStatsPercent = new HashMap<>();
        WizardStatsPercent.put("strength", strengthPercent);
        WizardStatsPercent.put("luck", luckPercent);
        WizardStatsPercent.put("intelligence", intelligencePercent);
        WizardStatsPercent.put("charisma", charismaPercent);

        return WizardStatsPercent;
    }

    public void setWizardSpec(String spec, int specPoints) {
        switch (spec) {
            case "strength" -> this.setStrength(this.getStrength() + specPoints);
            case "luck" -> this.setLuck(this.getLuck() + specPoints);
            case "intelligence" -> this.setIntelligence(this.getIntelligence() + specPoints);
            case "charisma" -> this.setCharisma(this.getCharisma() + specPoints);
        }
    }

    public void upgradeSpec(Runnable func) throws CloneNotSupportedException {
        List<String> specsList = new ArrayList<>(Arrays.asList("Strength", "Luck", "Intelligence", "Charisma"));

        if(this.getSpecPoints() > 0) {
            printTitle("What spec would you like to upgrade?");
            printChoices(specsList);
            int choiceInt = returnChoiceInt(1, specsList.size(), true);
            if(choiceInt == -2) {
                func.run();
            }
            String spec = specsList.get(choiceInt - 1).toLowerCase();

            printTitle("You have " + (int) this.getSpecPoints() + " points, how many would you like to add?");
            int specPoints =  returnChoiceInt(0, (int) this.getSpecPoints(), true);
            if(specPoints == -2) {
                func.run();
            }

            setWizardSpec(spec, specPoints);
            this.setSpecPoints(this.getSpecPoints() - specPoints);
        }
        else {
            printTitle("You have no spec points.");
        }

        continuePromptExtra();
        chooseAction();
    }

    // UPDATE WIZARD HEALTH AND DEFENSE POINTS BASED ON THEIR LEVEL
    public void updateWizardHpDf() {
        double wizardNewHp = (int) (this.getLevel() * wizardBaseHp + wizardBaseHp);
        double wizardNewDp = (int) (this.getLevel() * wizardBaseDp + wizardBaseDp);

        this.setMaxDefensePoints(wizardNewDp);
        this.setMaxHealthPoints(wizardNewHp);
    }

    public void restoreWizardHpDf() {
        this.setHealthPoints(this.getMaxHealthPoints());
        this.setDefensePoints(this.getMaxDefensePoints());
    }


    // UPDATE WIZARD STATS BASED ON WHICH HOUSE THEY BELONG TO
    public void updateHouseSpec() {
        HouseName houseName = this.getHouse().getHouseName();
        double houseSpecValue = houseName.getSpecValue();

        if (houseName == HouseName.GRYFFINDOR) {
            this.setDefensePoints(this.getDefensePoints() * (1 + houseSpecValue));
        } else if (houseName == HouseName.RAVENCLAW) {
            this.setCharisma(this.getCharisma() + houseSpecValue);
        }
    }

    // UPDATE WIZARD LEVEL BASED ON XP THEY HAVE
    private void updateLevel() {
        double currentExperience = this.getExperience();
        double xpFx;
        double xpCumulativeFunction = 0;
        double oldLevel = this.getLevel();

        for (int x = 1; x <= AbstractCharacter.maxLevel; x++) {
            xpFx = levelIncrement * Math.pow(x, 2) + baseLevelExperience;
            xpCumulativeFunction = xpCumulativeFunction + xpFx;

            double experienceRatio = currentExperience / xpCumulativeFunction;
            if (experienceRatio >= 1 && experienceRatio < 2) {
                this.setLevel(x);
            } else if (currentExperience == 0) {
                this.setLevel(0);
                break;
            }
        }
    }


    public void addExperience(double experience) {
        this.setExperience(this.getExperience() + experience);
    }

    public void updateStats() throws CloneNotSupportedException {
        this.updateLevel();
        this.updateWizardHpDf();
        this.updateSpellsHashMap();
    }

    public void usePotion() {
        List<String> potionNamesList = this.getPotionNamesList();


        if(potionNamesList.size() > 0) {
            printTitle("Choose a potion you want to use.");
            printChoices(potionNamesList);
        }
        else {
            printTitle("You don't have any potions in your inventory.");
        }

        Potion chosenPotion = getPotion(potionNamesList.get(returnChoiceInt(0, potionNamesList.size(), false) - 1));
        this.drinkPotion(chosenPotion);
    }

    public static Wizard wizard = Wizard.builder()
            .healthPoints(wizardBaseHp)
            .defensePoints(wizardBaseDp)
            .maxDefensePoints(wizardBaseDp)
            .maxHealthPoints(wizardBaseHp)
            .difficulty(Difficulty.EASY)
            .characterState(CharacterState.STANDING)
            .level(0)
            .firstName("null")
            .lastName("null")
            .name("null null")
            .gender(null)
            .pet(null)
            .wand(null)
            .house(new House(HouseName.GRYFFINDOR))
            .spellsHashMap(new HashMap<>())
            .spellsKeyList(new ArrayList<>())
            .itemList(new ArrayList<>())
            .activePotionsList(new ArrayList<>())
            .experience(10000)
            .charisma(0)
            .strength(0)
            .intelligence(0)
            .luck(0)
            .specPoints(0)
            .build();

    public static Wizard testWizard = Wizard.builder()
            .healthPoints(wizardBaseHp)
            .defensePoints(wizardBaseDp)
            .maxDefensePoints(wizardBaseDp)
            .maxHealthPoints(wizardBaseHp)
            .difficulty(Difficulty.DEATH_WISH)
            .characterState(CharacterState.STANDING)
            .level(0)
            .firstName("Test")
            .lastName("Wizard")
            .name("Test Wizard")
            .gender(Gender.MALE)
            .pet(Pet.CAT)
            .wand(new Wand(Core.PHEONIX_FEATHER, 12))
            .house(new House(HouseName.RAVENCLAW))
            .spellsHashMap(new HashMap<>())
            .spellsKeyList(new ArrayList<>())
            .itemList(new ArrayList<>())
            .activePotionsList(new ArrayList<>())
            .experience(580)
            .charisma(0)
            .strength(0)
            .specPoints(0)
            .intelligence(0)
            .luck(0)
            .build();
}
