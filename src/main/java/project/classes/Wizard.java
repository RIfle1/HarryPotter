package project.classes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.abstractClasses.AbstractCharacter;
import project.enums.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static project.classes.Color.*;
import static project.enums.EnumMethods.returnFormattedEnum;
import static project.enums.HouseName.getHouseNameMaxLength;
import static project.functions.ConsoleFunctions.*;
import static project.functions.LevelFunctions.chooseLevelAction;

@Getter
@Setter
public class Wizard extends AbstractCharacter {
    public static Wizard wizard = Wizard.builder()
            .name("null")
            .healthPoints(0)
            .defensePoints(0)
            .maxHealthPoints(0)
            .maxDefensePoints(0)
            .difficulty(Difficulty.EASY)
            .characterState(CharacterState.STANDING)
            .potionList(new ArrayList<>())
            .activePotionsList(new ArrayList<>())
            .spellsHashMap(new HashMap<>())
            .spellsKeyList(new ArrayList<>())
            .level(1)
            .firstName("null")
            .lastName("null")
            .gender(Gender.MALE)
            .pet(Pet.OWL)
            .wand(new Wand(Core.PHOENIX_FEATHER, 0))
            .houseName(HouseName.GRYFFINDOR)
            .experience(0)
            .specPoints(0)
            .charisma(0)
            .strength(0)
            .intelligence(0)
            .luck(0)
            .build();

    public static final int wizardBaseHp = 120;
    public static final int wizardBaseDp = 40;
    private static final double baseLevelExperience = 100;
    private static final double levelIncrement = 20;
    public static final int wizardSpecs = 4;
    private static final int dungeonLevels = 7;
    private static final double maxPercent = 0.25;

    @Builder
    public Wizard(String name, double healthPoints, double defensePoints, double maxHealthPoints, double maxDefensePoints, Difficulty difficulty, CharacterState characterState, List<Potion> potionList, List<Potion> activePotionsList, HashMap<String, Spell> spellsHashMap, List<String> spellsKeyList, double level, String firstName, String lastName, Gender gender, Pet pet, Wand wand, HouseName houseName, double experience, double specPoints, double charisma, double strength, double intelligence, double luck) {
        super(name, healthPoints, defensePoints, maxHealthPoints, maxDefensePoints, difficulty, characterState, potionList, activePotionsList, spellsHashMap, spellsKeyList, level);
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.pet = pet;
        this.wand = wand;
        this.houseName = houseName;
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
    private HouseName houseName;
    private double experience;
    private double specPoints;
    private double charisma; // for dodging
    private double strength; // for more attack damage
    private double intelligence; // for parrying
    private double luck; // for more attackChance

    public String returnStringSpecs() {

        String column0Format = "%-" + getHouseNameMaxLength() + "s";
        String column1Format = "%-" + 14 + "s";
        String column2Format = "%-" + 14 + "s";
        String column3Format = "%-" + 14 + "s";
        String column4Format = "%-" + 14 + "s";

        String column0 = returnColoredText(String.format(column0Format , returnFormattedEnum(this.getHouseName())), this.getHouseName().getHouseColor());
        String column1 = returnColoredText(String.format(column1Format ,(int) this.getCharisma() + " Charisma"), ANSI_YELLOW);
        String column2 = returnColoredText(String.format(column2Format ,(int) this.getStrength() + " Strength"), ANSI_RED);
        String column3 = returnColoredText(String.format(column3Format ,(int) this.getIntelligence() + " Intelligence"), ANSI_BLUE);
        String column4 = returnColoredText(String.format(column4Format ,(int) this.getLuck() + " Luck"), ANSI_PURPLE);


        return "\n\n" + column0 +
                printColumnSeparator("||") + column1 +
                printColumnSeparator("||") + column2 +
                printColumnSeparator("||") + column3 +
                printColumnSeparator("||") + column4;
    }

    public String returnAllStringStats(int extraNameLength) {
        this.updateStats();
        return this.returnStringStats(extraNameLength) + this.returnStringSpecs();
    }

    public void printAllStringStats(int extraNameLength) {
        System.out.println(this.returnAllStringStats(extraNameLength));
        continuePromptExtra();
    }

    public List<String> returnSpecList() {
        return Arrays.asList("Charisma", "Strength", "Intelligence", "Luck");
    }

    public HashMap<String, Double> returnWizardSpecsPercent() {
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

    public boolean setWizardSpec(String spec, int specPoints) {
        if(this.getSpecPoints() >= specPoints) {
            switch (spec) {
                case "strength" -> this.setStrength(this.getStrength() + specPoints);
                case "luck" -> this.setLuck(this.getLuck() + specPoints);
                case "intelligence" -> this.setIntelligence(this.getIntelligence() + specPoints);
                case "charisma" -> this.setCharisma(this.getCharisma() + specPoints);
            }
            this.setSpecPoints(this.getSpecPoints() - specPoints);
            return true;
        }
        return false;



    }

    public void upgradeSpec(Runnable func) {
        List<String> specsList = new ArrayList<>(Arrays.asList("Strength", "Luck", "Intelligence", "Charisma"));

        if(this.getSpecPoints() > 0) {
            printTitle("You have " + this.getSpecPoints() + " spec points. What spec would you like to upgrade?");
            printChoices(specsList);
            int choiceInt = returnChoiceInt(1, specsList.size(), true, func);
            String spec = specsList.get(choiceInt - 1).toLowerCase();

            printTitle("You have " + (int) this.getSpecPoints() + " points, how many would you like to add?");
            int specPoints =  returnChoiceInt(1, (int) this.getSpecPoints(), true, func);

            setWizardSpec(spec, specPoints);
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
        HouseName houseName = this.getHouseName();
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

    public void updateStats() {
        this.updateLevel();
        this.updateWizardHpDf();
        this.updateSpellsHashMap();
    }

    public void usePotion() {
        List<String> potionNamesList = this.returnPotionNamesList();
        int choiceInt = 1;

        if(potionNamesList.size() > 0) {
            printTitle("Choose a potion you want to use.");
            printChoices(potionNamesList);
            choiceInt = returnChoiceInt(1, potionNamesList.size(), true, () -> chooseLevelAction(false));

            if(choiceInt != -2) {
                Potion chosenPotion = returnPotion(potionNamesList.get(choiceInt - 1));
                this.drinkPotion(chosenPotion);
            }

        }
        else {
            printTitle("You don't have any potions in your inventory.");
        }

        if(choiceInt != -2) {
            continuePromptExtra();
            chooseLevelAction(false);
        }

    }

}
