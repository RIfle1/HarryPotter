package Enums;
import java.util.HashMap;
import java.util.List;

import static Enums.EnumMethods.returnFormattedEnum;

public enum Core {
    PHEONIX_FEATHER,
    DRAGON_HEARTSTRING,
    TEST;

    public static List<String> getCoreList() {
        Core[] coreValues = Core.values();
        return EnumMethods.getEnumList(coreValues);
    }

    public static Core setCore(String core) {
        HashMap<String, Core> coreHashMap = new HashMap<>();
        Core[] coreValues = Core.values();

        for(Core coreValue:coreValues) {
            coreHashMap.put(returnFormattedEnum(coreValue), coreValue);
        }
        return coreHashMap.get(core);
    }



}
