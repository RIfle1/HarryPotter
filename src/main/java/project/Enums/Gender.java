package project.Enums;

import java.util.HashMap;
import java.util.List;

public enum Gender {
    MALE,
    FEMALE;

    public static List<String> getGenderList() {
        Gender[] genderValues = Gender.values();
        return EnumMethods.getEnumList(genderValues);
    }

    public static Gender setGender(String gender) {
        HashMap<String, Gender> genderHashMap = new HashMap<>();
        Gender[] genderValues = Gender.values();

        for(Gender genderValue:genderValues) {
            genderHashMap.put(EnumMethods.returnFormattedEnum(genderValue), genderValue);
        }
        return genderHashMap.get(gender);
    }
}
