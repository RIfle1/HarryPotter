package project.Functions;

import project.Classes.Wand;
import project.Classes.Wizard;
import project.Enums.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static project.Functions.ConsoleFunctions.*;
import static project.Functions.GeneralFunctions.generateRandomString;
import static project.Functions.SaveFunctions.saveProgress;

public class CharacterCreation {
    public static void characterInit() {
        String firstName;
        String lastName;

        Gender gender;
        Pet pet;
        HouseName houseName;

        Wand wand;
        Difficulty difficulty;

        printColoredHeader("Select your character's first name:");
        firstName = returnChoiceString();
        printColoredHeader("Select your character's last name:");
        lastName = returnChoiceString();

        printColoredHeader("Select your character's gender:");
        printChoices(Gender.getGenderList());
        gender = Gender.setGender(Gender.getGenderList().get(returnChoiceInt(1, Gender.getGenderList().size(), false, null) - 1));

        printColoredHeader("Select your pet");
        printChoices(Pet.getPetList());
        pet = Pet.setPet(Pet.getPetList().get(returnChoiceInt(1, Pet.getPetList().size(), false, null) - 1));

        wand = wandCreation();
        houseName = sortingHat();

        printColoredHeader("The sorting hat thinks you should be in the house of " + EnumMethods.returnFormattedEnum(houseName)+". Do you accept?");

        boolean answer = returnYesOrNo();
        if(!answer) {
            houseName = houseChoice();
        }
        printColoredHeader("Congratulations!, You are now part of the " + EnumMethods.returnFormattedEnum(houseName) + " House.");

        printColoredHeader("Choose the difficulty of your game: ");
        printChoices(Difficulty.getDifficultyList());
        difficulty = Difficulty.setDifficulty(Difficulty.getDifficultyList().get(returnChoiceInt(1, Difficulty.getDifficultyList().size(), false, null) - 1));

        Wizard.wizard = Wizard.builder()
                .healthPoints(Wizard.wizardBaseHp)
                .defensePoints(Wizard.wizardBaseDp)
                .maxDefensePoints(Wizard.wizardBaseDp)
                .maxHealthPoints(Wizard.wizardBaseHp)
                .difficulty(difficulty)
                .characterState(CharacterState.STANDING)
                .level(1)
                .firstName(firstName)
                .lastName(lastName)
                .name(firstName + " " + lastName)
                .gender(gender)
                .pet(pet)
                .wand(wand)
                .houseName(houseName)
                .spellsHashMap(new HashMap<>())
                .spellsKeyList(new ArrayList<>())
                .potionList(new ArrayList<>())
                .activePotionsList(new ArrayList<>())
                .experience(0)
                .charisma(0)
                .strength(0)
                .intelligence(0)
                .specPoints(0)
                .luck(0)
                .build();

        Wizard.wizard.updateHouseSpec();
        Wizard.wizard.updateStats();

        saveProgress("autoSave" + generateRandomString(5));
    }

    public static Wand wandCreation() {
        Core core;
        int size;

        printColoredHeader("Select your wand's Core:");
        printChoices(Core.getCoreList());
        core = Core.setCore(Core.getCoreList().get(returnChoiceInt(1, Core.getCoreList().size(), false, null) - 1));

        printColoredHeader("Select your wand's Size (10-27):");
        size = returnChoiceInt(10, 27, false, null);

        return new Wand(core, size);
    }

    public static HouseName houseChoice() {
        printColoredHeader("Select your House:");
        printChoices(HouseName.getHouseNameList());
        return HouseName.setHouseName(HouseName.getHouseNameList().get(returnChoiceInt(1, HouseName.getHouseNameList().size(), false, null) - 1));

    }

    public static HouseName sortingHat() {
        printColoredHeader("The sorting hat has been placed on your head." +
                "\nThe sorting hat whispers: You come here with preferences and preconceptions — certain expectations.");
        List<String> questionList1 = new ArrayList<>();
        questionList1.add("I can’t wait to start classes.");
        questionList1.add("I can’t wait to explore.");
        printChoices(questionList1);
        returnChoiceInt(1, questionList1.size(), false, null);

        printColoredHeader("Hmm. I wonder. Hmm. I detect something in you. A certain sense of — hmm — what is it?");
        HashMap<String, HouseName> questionMap2 = new HashMap<>();
        var questionList2 = new ArrayList<String>();
        questionMap2.put("Daring.", HouseName.GRYFFINDOR);
        questionMap2.put("Curiosity.", HouseName.RAVENCLAW);
        questionMap2.put("Loyalty.", HouseName.HUFFLEPUFF);
        questionMap2.put("Ambition.", HouseName.SLYTHERIN);

        questionMap2.forEach((key, value) -> questionList2.add(key));

        printChoices(questionList2);
        return questionMap2.get(questionList2.get(returnChoiceInt(1, questionList2.size(), false, null) - 1));

    }

}
