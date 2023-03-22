package Enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static Enums.EnumMethods.returnFormattedEnum;

@Getter
public enum EnemyName {
    GOBLIN(EnemyCombat.MELEE, EnemyType.BASIC, 50, 30),
    DARK_WIZARD(EnemyCombat.MAGIC, EnemyType.BASIC, 70, 60),
    TROLL(EnemyCombat.MELEE, EnemyType.BOSS, 200, 60),
    BASILISK(EnemyCombat.MELEE, EnemyType.BOSS, 140, 100),
    DEMENTOR(EnemyCombat.MAGIC, EnemyType.BASIC, 30, 110),
    DEATH_EATER(EnemyCombat.MAGIC, EnemyType.BASIC, 90, 120);

    private final EnemyCombat enemyCombat;
    private final EnemyType enemyType;
    private final int enemyBaseHp;
    private final int enemyBaseDp;

    // TODO - ADD BASE EXPERIENCE POINTS TO EACH ENEMY NAME

    EnemyName(EnemyCombat enemyCombat, EnemyType enemyType, int enemyBaseHp, int enemyBaseDp) {
        this.enemyCombat = enemyCombat;
        this.enemyType = enemyType;
        this.enemyBaseHp = enemyBaseHp;
        this.enemyBaseDp = enemyBaseDp;
    }

    public static List<EnemyName> getAllEnemyNames() {
        EnemyName[] enemyNameValues = EnemyName.values();
        return new ArrayList<>(Arrays.asList(enemyNameValues));
    }

    public static List<EnemyName> getAllBasicEnemyNames() {
        return Arrays.stream(EnemyName.values()).filter(enemyName -> enemyName.enemyType.equals(EnemyType.BASIC)).toList();
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
