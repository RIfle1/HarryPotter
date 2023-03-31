package project.Enums;

import java.util.HashMap;
import java.util.List;

import static project.Enums.EnumMethods.returnFormattedEnum;

public enum EnemyType {
    BASIC,
    BOSS;

    public static List<String> getEnemyTypeList() {
        EnemyType[] enemyTypeValues = EnemyType.values();
        return EnumMethods.getEnumList(enemyTypeValues);
    }

    public static EnemyType setEnemyType(String enemyType) {
        HashMap<String, EnemyType> enemyTypeHashMap = new HashMap<>();
        EnemyType[] enemyTypeValues = EnemyType.values();

        for(EnemyType enemyTypeValue:enemyTypeValues) {
            enemyTypeHashMap.put(returnFormattedEnum(enemyTypeValue), enemyTypeValue);
        }
        return enemyTypeHashMap.get(enemyType);
    }

}
