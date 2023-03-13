package Enums;

import java.util.HashMap;
import java.util.List;

import static Enums.EnumMethods.returnFormattedEnum;

public enum EnemyName {
    GOBLIN(EnemyType.MELEE_ENEMY),
    DARK_WIZARD(EnemyType.MAGIC_ENEMY),
    TROLL(EnemyType.MELEE_ENEMY),
    BASILISK(EnemyType.MELEE_ENEMY),
    DEMENTOR(EnemyType.MAGIC_ENEMY),
    DEATH_EATER(EnemyType.MAGIC_ENEMY);

    public final EnemyType enemyType;

    EnemyName(EnemyType enemyType) {
        this.enemyType = enemyType;
    }

    public static List<String> getEnemyNameList() {
        EnemyName[] enemyNameValues = EnemyName.values();
        return EnumMethods.getEnumList(enemyNameValues);
    }

    public static EnemyName setEnemyName(String enemyName) {
        HashMap<String, EnemyName> enemyNameHashMap = new HashMap<>();
        EnemyName[] enemyNameValues = EnemyName.values();

        for(EnemyName enemyNameValue:enemyNameValues) {
            enemyNameHashMap.put(returnFormattedEnum(enemyNameValue), enemyNameValue);
        }
        return enemyNameHashMap.get(enemyName);
    }
}
