package Enums;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static Enums.EnumMethods.returnFormattedEnum;

@Getter
public enum Level {
    The_Philosophers_Stone(true),
    The_Chamber_of_Secrets(false),
    The_Prisoner_of_Azkaban(false),
    The_Goblet_of_Fire(false),
    The_Order_of_the_Phoenix(false),
    The_Half_Blooded_Prince(false),
    The_Deathly_Hallows(false),
    Battle_Arena(true);

    private boolean unlocked;
    Level(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public void setLevelStatus(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public static List<String> getLevelList() {
        Level[] levelValues = Level.values();
        return EnumMethods.getEnumList(levelValues);
    }

    public static List<String> getUnlockedLevelList() {
        Level[] levelValues = Arrays.stream(Level.values()).filter(Level::isUnlocked).toList().toArray(new Level[0]);
        return EnumMethods.getEnumList(levelValues);
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
