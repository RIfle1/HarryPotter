package Enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;

import static Enums.EnumMethods.returnFormattedEnum;

@Getter
public enum HouseName {
    HUFFLEPUFF("POTION", 0.15),
    SLYTHERIN("DAMAGE", 0.1),
    GRYFFINDOR("DEFENSE", 0.2),
    RAVENCLAW("CHARISMA", 5);

    private final String specName;
    private final double specValue;

    HouseName(String houseSpec, double value) {
        this.specName = houseSpec;
        this.specValue = value;
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
