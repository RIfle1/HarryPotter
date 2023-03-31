package project.enums;

import java.util.HashMap;
import java.util.List;

import static project.enums.EnumMethods.returnFormattedEnum;

public enum MoveType {
    ATTACK,
    PARRY,
    DODGE,
    FOLLOW_UP;

    public static List<String> getMoveTypeList() {
        MoveType[] spellTypeValues = MoveType.values();
        return EnumMethods.getEnumList(spellTypeValues);
    }

    public static List<String> getMoveTypeList(List<MoveType> unwantedMoveTypeList) {
        MoveType[] spellTypeValues = MoveType.values();
        List<String> moveTypeList = EnumMethods.getEnumList(spellTypeValues);

        for(MoveType moveType: unwantedMoveTypeList) {
            moveTypeList.remove(returnFormattedEnum(moveType));
        }
        return moveTypeList;
    }

    public static MoveType setMoveType(String spellType) {
        HashMap<String, MoveType> spellTypeHashMap = new HashMap<>();
        MoveType[] spellTypeValues = MoveType.values();

        for(MoveType spellTypeValue:spellTypeValues) {
            spellTypeHashMap.put(returnFormattedEnum(spellTypeValue), spellTypeValue);
        }
        return spellTypeHashMap.get(spellType);
    }
}
