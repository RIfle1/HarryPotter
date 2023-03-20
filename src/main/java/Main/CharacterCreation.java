package Main;
import Classes.*;
import Enums.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static Classes.Wizard.*;
import static Main.ConsoleFunctions.*;

public class CharacterCreation {

    public static void characterInit() throws CloneNotSupportedException {
        String firstName;
        String lastName;

        Gender gender;
        Pet pet;

        Wand wand;
        House house;
        Difficulty difficulty;

        double charisma; // for dodging
        double strength; // for more attack damage
        double intelligence; // for parrying
        double luck; // for more attackChance

        printHeader("Select your character's first name:");
        firstName = returnChoiceString();
        printHeader("Select your character's last name:");
        lastName = returnChoiceString();

        printHeader("Select your character's gender:");
        printChoices(Gender.getGenderList());
        gender = Gender.setGender(Gender.getGenderList().get(returnChoiceInt() - 1));

        printHeader("Select your pet");
        printChoices(Pet.getPetList());
        pet = Pet.setPet(Pet.getPetList().get(returnChoiceInt() - 1));

        wand = wandCreation();
        house = sortingHat();

        printHeader("The sorting hat thinks you should be in the house of " + EnumMethods.returnFormattedEnum(house.getHouseName())+". Do you accept?");
        printChoices(yesOrNo);
        int answer = returnChoiceInt() - 1;
        if(answer == 1) {
            house = houseChoice();
        }
        printHeader("Congratulations!, You are now part of the " + EnumMethods.returnFormattedEnum(house.getHouseName()) + " House.");

        printHeader("Choose the difficulty of your game: ");
        printChoices(Difficulty.getDifficultyList());
        difficulty = Difficulty.setDifficulty(Difficulty.getDifficultyList().get(returnChoiceInt() - 1));


        System.out.println(
                firstName +
                        lastName +
                        gender +
                        pet +
                        wand.getSize() +
                        EnumMethods.returnFormattedEnum(wand.getCore()) + " " +
                        house.getHouseName()

        );

        Wizard.wizard = Wizard.builder()
                .healthPoints(wizardBaseHp)
                .defensePoints(wizardBaseDp)
                .maxDefensePoints(wizardBaseDp)
                .maxHealthPoints(wizardBaseHp)
                .difficulty(difficulty)
                .characterState(CharacterState.STANDING)
                .level(1)
                .firstName(firstName)
                .lastName(lastName)
                .name(firstName + " " + lastName)
                .gender(gender)
                .pet(pet)
                .wand(wand)
                .house(house)
                .spellHashMap(new HashMap<>())
                .itemList(new ArrayList<>())
                .activePotionsList(new ArrayList<>())
                .experience(0)
                .charisma(0)
                .strength(0)
                .intelligence(0)
                .luck(0)
                .build();

        wizard.updateHouseSpec();
        wizard.updateStats();
    }

    public static Wand wandCreation() {
        Core core;
        int size;

        printHeader("Select your wand's Core:");
        printChoices(Core.getCoreList());
        core = Core.setCore(Core.getCoreList().get(returnChoiceInt() - 1));

        printHeader("Select your wand's Size (10-27):");
        size = returnChoiceInt(10, 27);

        return new Wand(core, size);
    }

    public static House houseChoice() {
        HouseName houseName;

        printHeader("Select your House:");
        printChoices(HouseName.getHouseNameList());
        houseName = HouseName.setHouseName(HouseName.getHouseNameList().get(returnChoiceInt() - 1));

        return new House(houseName);
    }

    public static House sortingHat() {
        HouseName houseName;

        printHeader("The sorting hat has been placed on your head.");
        continuePrompt();

        printHeader("You come here with preferences and preconceptions — certain expectations.");
        List<String> questionList1 = new ArrayList<>();
        questionList1.add("I can’t wait to start classes.");
        questionList1.add("I can’t wait to explore.");
        printChoices(questionList1);
        returnChoiceInt();

        printHeader("Hmm. I wonder. Hmm. I detect something in you. A certain sense of — hmm — what is it?");
        HashMap<String, HouseName> questionMap2 = new HashMap<>();
        var questionList2 = new ArrayList<String>();
        questionMap2.put("Daring.", HouseName.GRYFFINDOR);
        questionMap2.put("Curiosity.", HouseName.RAVENCLAW);
        questionMap2.put("Loyalty.", HouseName.HUFFLEPUFF);
        questionMap2.put("Ambition.", HouseName.SLYTHERIN);

        questionMap2.forEach((key, value) -> {
            questionList2.add(key);
        });

        printChoices(questionList2);
        houseName = questionMap2.get(questionList2.get(returnChoiceInt() - 1));

        return new House(houseName);
    }

}
