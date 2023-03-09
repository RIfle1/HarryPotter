package Enums;
import java.util.List;

public enum Core {
    PHEONIX_FEATHER("Pheonix Feather"),
    DRAGON_HEARTSTRING("Dragon Heartstring");
    public final String label;

    private Core(String label) {
        this.label = label;
    }

    public static List<String> getCoreList() {
        Core[] coreValues = Core.values();
        return EnumMethods.getEnumList(coreValues);
    }
}
