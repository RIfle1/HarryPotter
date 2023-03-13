package Enums;

import java.util.HashMap;
import java.util.List;

import static Enums.EnumMethods.returnFormattedEnum;

public enum SpellType {
    ANCIENT,
    ESSENTIAL,
    CONTROL,
    FORCE,
    DAMAGE,
    TRANSFIGURATION,
    UNFORGIVABLE_CURSE;

    public static List<String> getSpellTypeList() {
        SpellType[] spellTypeValues = SpellType.values();
        return EnumMethods.getEnumList(spellTypeValues);
    }

    public static SpellType setSpellType(String spellType) {
        HashMap<String, SpellType> spellTypeHashMap = new HashMap<>();
        SpellType[] spellTypeValues = SpellType.values();

        for(SpellType spellTypeValue:spellTypeValues) {
            spellTypeHashMap.put(returnFormattedEnum(spellTypeValue), spellTypeValue);
        }
        return spellTypeHashMap.get(spellType);
    }
}
