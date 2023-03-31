package project.enums;

import java.util.HashMap;
import java.util.List;

import static project.enums.EnumMethods.returnFormattedEnum;

public enum PotionType {
    HEALTH,
    DEFENSE,
    REGENERATION,
    INVINCIBILITY,
    DAMAGE,
    COOLDOWN;

    public static List<String> getPotionTypeList() {
        PotionType[] potionTypeValues = PotionType.values();
        return EnumMethods.getEnumList(potionTypeValues);
    }

    public static PotionType setPotionType(String potionType) {
        HashMap<String, PotionType> potionTypeHashMap = new HashMap<>();
        PotionType[] potionTypeValues = PotionType.values();

        for(PotionType potionTypeValue:potionTypeValues) {
            potionTypeHashMap.put(returnFormattedEnum(potionTypeValue), potionTypeValue);
        }
        return potionTypeHashMap.get(potionType);
    }
}
