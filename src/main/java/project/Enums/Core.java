package project.Enums;
import java.util.HashMap;
import java.util.List;

import static project.Enums.EnumMethods.returnFormattedEnum;

public enum Core {
    PHOENIX_FEATHER,
    DRAGON_HEARTSTRING;

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
