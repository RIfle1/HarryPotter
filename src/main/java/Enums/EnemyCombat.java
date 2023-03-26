package Enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;

import static Enums.EnumMethods.returnFormattedEnum;

@Getter
public enum EnemyCombat {
    MELEE(50, 0.6),
    SPELL(0, 0);

    private final int combatDamage;
    private final double combatChance;

    EnemyCombat(int combatDamage, double combatChance) {
        this.combatDamage = combatDamage;
        this.combatChance = combatChance;
    }

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
