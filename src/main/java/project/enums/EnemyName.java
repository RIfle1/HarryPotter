package project.enums;

import project.classes.Spell;
import lombok.Getter;

import java.util.*;

import static project.enums.EnumMethods.returnFormattedEnum;

@Getter
public enum EnemyName {
    GOBLIN(EnemyCombat.MELEE, EnemyType.BASIC, null, 50, 0, 1, 30, 20, null),
    DARK_WIZARD(EnemyCombat.SPELL, EnemyType.BASIC, null, 70, 0.1, 1, 60, 30, null),
    TROLL(EnemyCombat.MELEE, EnemyType.BOSS, new ArrayList<>(), 200, 0.2, 0.1, 60, 120, new Object() {
        List<String> evaluate() {
            List<String> strings = new ArrayList<>();
            strings.add(trollDeathLine);
            return strings;
        }
    }.evaluate()),
    BASILISK(EnemyCombat.MELEE, EnemyType.BOSS, new ArrayList<>(), 250, 0.5, 0.4, 100, 180, new Object() {
        List<String> evaluate() {
            List<String> strings = new ArrayList<>();
            strings.add(basiliskDeathLine1);
            strings.add(basiliskDeathLine2);
            return strings;
        }
    }.evaluate()),
    DEMENTOR(EnemyCombat.MELEE, EnemyType.BOSS, new ArrayList<>(), 100, 0, 0.3, 110, 30, null),
    PETER_PETTIGREW(EnemyCombat.SPELL, EnemyType.BOSS, new ArrayList<>(), 300, 0.2, 0, 80, 40, new Object() {
        List<String> evaluate() {
            List<String> strings = new ArrayList<>();
            strings.add(peterPettigrewDeathLine);
            return strings;
        }
    }.evaluate()),
    DOLORES_UMBRIDGE(EnemyCombat.SPELL, EnemyType.BOSS, new ArrayList<>(), 10000, 0, 0, 80, 40, new Object() {
        List<String> evaluate() {
            List<String> strings = new ArrayList<>();
            strings.add(doloresUmbridgeDeathLine1);
            strings.add(doloresUmbridgeDeathLine2);
            return strings;
        }
    }.evaluate()),
    DEATH_EATER(EnemyCombat.MELEE, EnemyType.BOSS, new ArrayList<>(), 150, 0, 0.4, 120, 40, null),
    VOLDEMORT(EnemyCombat.SPELL, EnemyType.BOSS, new ArrayList<>(), 300, 0, 0, 120, 40, new Object() {
        List<String> evaluate() {
            List<String> strings = new ArrayList<>();
            strings.add(voldemortDeathLine);
            return strings;
        }
    }.evaluate()),
    BELLATRIX_LESTRANGE(EnemyCombat.SPELL, EnemyType.BOSS, new ArrayList<>(), 200, 0, 0, 120, 40, new Object() {
        List<String> evaluate() {
            List<String> strings = new ArrayList<>();
            strings.add(bellatrixLestrangeDeathLine);
            return strings;
        }
    }.evaluate());





    private final EnemyCombat enemyCombat;
    private final EnemyType enemyType;
    private final List<Spell> vulnerableSpellList;
    private final int enemyBaseHp;
    private final double enemyDmgMultiplier;
    private final double enemyHpLimitRatio;
    private final int enemyBaseDp;
    private final int enemyXp;
    private final List<String> enemyDeathLine;

    private static final String trollDeathLine = "You threw an object at the Troll and he finally collapsed.";
    private static final String basiliskDeathLine1 = "You stabbed the Basilisk with Godric Gryffindor's Legendary sword. The Basilisk isn't moving anymore.";
    private static final String basiliskDeathLine2 = "You removed one of the Basilisk's teeth and stabbed Tom Riddle's journal with it. The Basilisk suddenly collapsed.";
    private static final String peterPettigrewDeathLine = "You stole Peter Pettigrew's keys and you escaped the cemetery.";
    private static final String doloresUmbridgeDeathLine1 = "You distracted Dolores Umbridge long enough for the fireworks to go off.";
    private static final String doloresUmbridgeDeathLine2 = "Damn you actually killed her. The cops are coming for you. jk she was actually annoying af";
    private static final String voldemortDeathLine = "Voldemort was sent back to the netherworld. He will come back stronger though.";
    private static final String bellatrixLestrangeDeathLine = "Bellatrix Lestrange disappeared in a puff of smoke.";

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

    public void addVulnerableSpell(Spell spell) {
        this.vulnerableSpellList.add(spell);
    }

    public void resetVulnerableSpellsList() {
        List<Spell> vulnerableSpellList = new ArrayList<>(this.vulnerableSpellList);
        vulnerableSpellList.forEach(this.vulnerableSpellList::remove);
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

        for(EnemyName enemyNameValue:enemyNameValues) {
            enemyNameHashMap.put(returnFormattedEnum(enemyNameValue), enemyNameValue);
        }
        return enemyNameHashMap.get(enemyName);
    }
}
