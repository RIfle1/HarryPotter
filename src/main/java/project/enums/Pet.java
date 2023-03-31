package project.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;

import static project.enums.EnumMethods.returnFormattedEnum;

@Getter
public enum Pet {
    OWL,
    RAT,
    CAT,
    TOAD;

    public static List<String> getPetList() {
        Pet[] petValues = Pet.values();
        return EnumMethods.getEnumList(petValues);
    }

    public static Pet setPet(String pet) {
        HashMap<String, Pet> petHashMap = new HashMap<>();
        Pet[] petValues = Pet.values();

        for(Pet petValue:petValues) {
            petHashMap.put(returnFormattedEnum(petValue), petValue);
        }
        return petHashMap.get(pet);
    }


}
