package Enums;

import java.util.HashMap;
import java.util.List;

import static Enums.EnumMethods.returnFormattedEnum;

public enum ThrowableObject {
    EXPLOSIVE_BARREL(90),
    CRATE(30),
    ANVIL(60),
    AXE(50),
    SWORD(55);

    public final double damage;

    ThrowableObject(double damage) {
        this.damage = damage;
    }

    public static List<String> getThrowableObjectList() {
        ThrowableObject[] throwableObjectValues = ThrowableObject.values();
        return EnumMethods.getEnumList(throwableObjectValues);
    }

    public static ThrowableObject setThrowableObject(String throwableObject) {
        HashMap<String, ThrowableObject> throwableObjectHashMap = new HashMap<>();
        ThrowableObject[] throwableObjectValues = ThrowableObject.values();

        for(ThrowableObject throwableObjectValue:throwableObjectValues) {
            throwableObjectHashMap.put(returnFormattedEnum(throwableObjectValue), throwableObjectValue);
        }
        return throwableObjectHashMap.get(throwableObject);
    }
}
