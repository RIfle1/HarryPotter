package Enums;

import java.util.List;

public enum Pet {
    OWL,
    RAT,
    CAT,
    TOAD;

    public static List<String> getPetList() {
        Pet[] petValues = Pet.values();
        return EnumMethods.getEnumList(petValues);
    }


}
