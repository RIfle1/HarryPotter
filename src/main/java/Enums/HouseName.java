package Enums;

import java.util.HashMap;
import java.util.List;

import static Enums.EnumMethods.returnFormattedEnum;

public enum HouseName {
    HUFFLEPUFF("POTION", 0.15),
    SLYTHERIN("DAMAGE", 0.1),
    GRYFFINDOR("DEFENSE", 0.2),
    RAVENCLAW("CHARISMA", 5);

    public final String houseSpec;
    public final double value;

    HouseName(String houseSpec, double value) {
        this.houseSpec = houseSpec;
        this.value = value;
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
