package Enums;

import lombok.Getter;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

import static Classes.Color.*;
import static Enums.EnumMethods.returnFormattedEnum;

@Getter
public enum HouseName {
    HUFFLEPUFF("POTION", 0.15, ANSI_BLUE),
    SLYTHERIN("DAMAGE", 0.1, ANSI_GREEN),
    GRYFFINDOR("DEFENSE", 0.2, ANSI_YELLOW),
    RAVENCLAW("CHARISMA", 5, ANSI_BLACK);

    private final String specName;
    private final double specValue;
    private final String houseColor;

    HouseName(String houseSpec, double value, String houseColor) {
        this.specName = houseSpec;
        this.specValue = value;
        this.houseColor = houseColor;
    }

    public static int getHouseNameMaxLength() {
        return getHouseNameList().stream().map(String::length).toList().stream().reduce(0, Integer::max);
    }

    public static List<String> getHouseNameList() {
        HouseName[] houseNameValues = HouseName.values();
        return EnumMethods.getEnumList(houseNameValues);
    }

    public static HouseName setHouseName(String houseName) {
        HashMap<String, HouseName> houseNameHashMap = new HashMap<>();
        HouseName[] houseNameValues = HouseName.values();

        for(HouseName houseNameValue:houseNameValues) {
            houseNameHashMap.put(returnFormattedEnum(houseNameValue), houseNameValue);
        }
        return houseNameHashMap.get(houseName);
    }
}
