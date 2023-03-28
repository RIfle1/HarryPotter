package Enums;

import Classes.Spell;
import lombok.Getter;

import java.util.*;

import static Enums.EnumMethods.returnFormattedEnum;

@Getter
public enum Level {
    The_Philosophers_Stone(true, Collections.singletonList(Spell.wingardiumLeviosa)),
    The_Chamber_of_Secrets(true, Arrays.asList(Spell.legendarySword, Spell.accio)),
    The_Prisoner_of_Azkaban(true, Collections.singletonList(Spell.expectroPatronum)),
    The_Goblet_of_Fire(true, Collections.singletonList(Spell.accio)),
    The_Order_of_the_Phoenix(true, new ArrayList<>()),
    The_Half_Blooded_Prince(true, Collections.singletonList(Spell.sectumsempra)),
    The_Deathly_Hallows(true, new ArrayList<>()),
    Battle_Arena(true, Collections.singletonList(Spell.avadaKedavra));

    private boolean unlocked;
    private final List<Spell> requiredSpellList;
    Level(boolean unlocked, List<Spell> requiredSpellList) {
        this.unlocked = unlocked;
        this.requiredSpellList = requiredSpellList;
    }


    public void setLevelStatus(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public static List<String> getLevelList() {
        Level[] levelValues = Level.values();
        return EnumMethods.getEnumList(levelValues);
    }

    public static List<String> getUnlockedLevelsList() {
        Level[] levelValues = Arrays.stream(Level.values()).filter(Level::isUnlocked).toList().toArray(new Level[0]);
        return EnumMethods.getEnumList(levelValues);
    }

    public static List<Level> getAllUnlockedLevelsList() {
        return Arrays.stream(Level.values()).filter(Level::isUnlocked).toList();
    }

    public static void unlockNextLevel(Level previousLevel) {
        Level nextLevel = setLevel(getLevelList().get(getLevelList().indexOf(returnFormattedEnum(previousLevel)) + 1));
        nextLevel.setLevelStatus(true);
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
