package Enums;

import java.util.HashMap;
import java.util.List;

import static Enums.EnumMethods.returnFormattedEnum;

public enum Gender {
    MALE,
    FEMALE,
    SDFIJSDJKFSDHJFK,
    NONE;

    public static List<String> getGenderList() {
        Gender[] genderValues = Gender.values();
        return EnumMethods.getEnumList(genderValues);
    }

    public static Gender setGender(String gender) {
        HashMap<String, Gender> genderHashMap = new HashMap<>();
        Gender[] genderValues = Gender.values();

        for(Gender genderValue:genderValues) {
            genderHashMap.put(returnFormattedEnum(genderValue), genderValue);
        }
        return genderHashMap.get(gender);
    }
}
