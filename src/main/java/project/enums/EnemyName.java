package project.enums;

import lombok.Getter;
import project.classes.Spell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static project.enums.EnumMethods.returnFormattedEnum;

@Getter
public enum EnemyName {
    GOBLIN(EnemyCombat.MELEE, EnemyType.BASIC, new ArrayList<>(), 50, 0, 1, 30, 20, new ArrayList<>()),
    DARK_WIZARD(EnemyCombat.SPELL, EnemyType.BASIC, new ArrayList<>(), 70, 0.1, 1, 60, 30, new ArrayList<>()),
    TROLL(EnemyCombat.MELEE, EnemyType.BOSS, new ArrayList<>(), 200, 0.2, 0.2, 60, 120,
            List.of("You threw an object at the Troll and he finally collapsed.")),
    BASILISK(EnemyCombat.MELEE, EnemyType.BOSS, new ArrayList<>(), 250, 0.5, 0.4, 100, 180,
            List.of("You removed one of the Basilisk's teeth and stabbed Tom Riddle's journal with it. The Basilisk suddenly collapsed.",
                    "You stabbed the Basilisk with Godric Gryffindor's Legendary sword. The Basilisk isn't moving anymore.")),
    DEMENTOR(EnemyCombat.MELEE, EnemyType.BOSS, new ArrayList<>(), 100, 0, 0.3, 110, 30, new ArrayList<>()),
    PETER_PETTIGREW(EnemyCombat.SPELL, EnemyType.BOSS, new ArrayList<>(), 300, 0.2, 0, 80, 40,
            List.of("You stole Peter Pettigrew's keys and you escaped the cemetery.")),
    DOLORES_UMBRIDGE(EnemyCombat.SPELL, EnemyType.BOSS, new ArrayList<>(), 10000, 0, 0, 80, 40,
            List.of("You distracted Dolores Umbridge long enough for the fireworks to go off.",
                    "Damn, you actually killed her. The cops are coming for you. jk she just got knocked out.")),
    DEATH_EATER(EnemyCombat.MELEE, EnemyType.BOSS, new ArrayList<>(), 150, 0, 0.4, 120, 40, new ArrayList<>()),
    VOLDEMORT(EnemyCombat.SPELL, EnemyType.BOSS, new ArrayList<>(), 300, 0, 0, 140, 40,
            List.of("Voldemort was sent back to the netherworld. He will come back stronger though.")),
    BELLATRIX_LESTRANGE(EnemyCombat.SPELL, EnemyType.BOSS, new ArrayList<>(), 200, 0, 0, 120, 40,
            List.of("Bellatrix Lestrange disappeared in a puff of smoke."));

    private final EnemyCombat enemyCombat;
    private final EnemyType enemyType;
    private final List<Spell> vulnerableSpellList;
    private final int enemyBaseHp;
    private final double enemyDmgMultiplier;
    private final double enemyHpLimitRatio;
    private final int enemyBaseDp;
    private final int enemyXp;
    private final List<String> enemyDeathLine;

    public static final String timeoutDeathLine = "You Took Too Long To Defeat The Enemies. Just go to the next level already...";

    EnemyName(EnemyCombat enemyCombat, EnemyType enemyType, List<Spell> vulnerableSpellList, int enemyBaseHp, double enemyDmgMultiplier, double enemyHpLimitRatio, int enemyBaseDp, int enemyXp, List<String> enemyDeathLine) {
        this.enemyCombat = enemyCombat;
        this.enemyType = enemyType;
        this.vulnerableSpellList = vulnerableSpellList;
        this.enemyBaseHp = enemyBaseHp;
        this.enemyDmgMultiplier = enemyDmgMultiplier;
        this.enemyHpLimitRatio = enemyHpLimitRatio;
        this.enemyBaseDp = enemyBaseDp;
        this.enemyXp = enemyXp;
        this.enemyDeathLine = enemyDeathLine;
    }

    public static void resetBossVulnerableSpellsList() {
        returnAllBasicEnemyNames().stream().filter(enemyName -> enemyName.getEnemyType().equals(EnemyType.BOSS)).forEach(EnemyName::resetVulnerableSpellsList);
    }

    public static int getEnemyNameMaxLength() {
        return getEnemyNameList().stream().map(String::length).toList().stream().reduce(0, Integer::max);
    }

    public static List<EnemyName> returnAllEnemyNames() {
        EnemyName[] enemyNameValues = EnemyName.values();
        return new ArrayList<>(Arrays.asList(enemyNameValues));
    }

    public static List<EnemyName> returnAllBasicEnemyNames() {
        return Arrays.stream(EnemyName.values()).filter(enemyName -> enemyName.enemyType.equals(EnemyType.BASIC)).toList();
    }

    public static List<String> getEnemyNameList() {
        EnemyName[] enemyNameValues = EnemyName.values();
        return EnumMethods.getEnumList(enemyNameValues);
    }

    public static EnemyName setEnemyName(String enemyName) {
        HashMap<String, EnemyName> enemyNameHashMap = new HashMap<>();
        EnemyName[] enemyNameValues = EnemyName.values();

        for (EnemyName enemyNameValue : enemyNameValues) {
            enemyNameHashMap.put(returnFormattedEnum(enemyNameValue), enemyNameValue);
        }
        return enemyNameHashMap.get(enemyName);
    }

    public void addVulnerableSpell(Spell spell) {
        this.vulnerableSpellList.add(spell);
    }

    public void resetVulnerableSpellsList() {
        List<Spell> vulnerableSpellList = new ArrayList<>(this.vulnerableSpellList);
        vulnerableSpellList.forEach(this.vulnerableSpellList::remove);
    }
}
