package Enums;

import java.util.HashMap;
import java.util.List;

import static Enums.EnumMethods.returnFormattedEnum;

public enum MoveType {
    ATTACK,
    PARRY,
    FOLLOW_UP;

    public static List<String> getSpellTypeList() {
        MoveType[] spellTypeValues = MoveType.values();
        return EnumMethods.getEnumList(spellTypeValues);
    }

    public static MoveType setSpellType(String spellType) {
        HashMap<String, MoveType> spellTypeHashMap = new HashMap<>();
        MoveType[] spellTypeValues = MoveType.values();

        for(MoveType spellTypeValue:spellTypeValues) {
            spellTypeHashMap.put(returnFormattedEnum(spellTypeValue), spellTypeValue);
        }
        return spellTypeHashMap.get(spellType);
    }
}
