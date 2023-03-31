package project.enums;

import project.classes.Spell;
import lombok.Getter;

import java.util.*;

import static project.enums.EnumMethods.returnFormattedEnum;

@Getter
public enum Level {
    The_Philosophers_Stone(true, Collections.singletonList(Spell.wingardiumLeviosa)),
    The_Chamber_of_Secrets(false, Arrays.asList(Spell.legendarySword, Spell.accio)),
    The_Prisoner_of_Azkaban(false, Collections.singletonList(Spell.expectroPatronum)),
    The_Goblet_of_Fire(false, Collections.singletonList(Spell.accio)),
    The_Order_of_the_Phoenix(false, new ArrayList<>()),
    The_Half_Blooded_Prince(false, Collections.singletonList(Spell.sectumsempra)),
    The_Deathly_Hallows(false, new ArrayList<>()),
    Battle_Arena(true, Collections.singletonList(Spell.avadaKedavra));

    private boolean unlocked;
    private final List<Spell> requiredSpellList;
    Level(boolean unlocked, List<Spell> requiredSpellList) {
        this.unlocked = unlocked;
        this.requiredSpellList = requiredSpellList;
    }


    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public static List<String> returnLevelList() {
        Level[] levelValues = Level.values();
        return EnumMethods.getEnumList(levelValues);
    }

    public static List<String> returnUnlockedLevelsList() {
        Level[] levelValues = Arrays.stream(Level.values()).filter(Level::isUnlocked).toList().toArray(new Level[0]);
        return EnumMethods.getEnumList(levelValues);
    }

    public static List<Level> returnAllUnlockedLevelsList() {
        return Arrays.stream(Level.values()).filter(Level::isUnlocked).toList();
    }

    public static List<Level> returnAllLevels() {
        return Arrays.stream(Level.values()).toList();
    }

    public static void unlockNextLevel(Level previousLevel) {
        Level nextLevel = setLevel(returnLevelList().get(returnLevelList().indexOf(returnFormattedEnum(previousLevel)) + 1));
        nextLevel.setUnlocked(true);
    }

    public static Level setLevel(String level) {
        HashMap<String, Level> levelHashMap = new HashMap<>();
        Level[] levelValues = Level.values();

        for(Level levelValue:levelValues) {
            levelHashMap.put(returnFormattedEnum(levelValue), levelValue);
        }
        return levelHashMap.get(level);
    }
}
