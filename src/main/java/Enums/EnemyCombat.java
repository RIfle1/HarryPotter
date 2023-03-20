package Enums;

import java.util.HashMap;
import java.util.List;

import static Enums.EnumMethods.returnFormattedEnum;

public enum EnemyCombat {
    MELEE,
    MAGIC;

    public static List<String> getEnemyTypeList() {
        EnemyCombat[] enemyCombatValues = EnemyCombat.values();
        return EnumMethods.getEnumList(enemyCombatValues);
    }

    public static EnemyCombat setEnemyType(String enemyType) {
        HashMap<String, EnemyCombat> enemyTypeHashMap = new HashMap<>();
        EnemyCombat[] enemyCombatValues = EnemyCombat.values();

        for(EnemyCombat enemyCombatValue : enemyCombatValues) {
            enemyTypeHashMap.put(returnFormattedEnum(enemyCombatValue), enemyCombatValue);
        }
        return enemyTypeHashMap.get(enemyType);
    }

}
