package Classes;

import Enums.CharacterState;
import Enums.MoveType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static Classes.Color.*;
import static Classes.Color.returnColoredText;
import static Main.ConsoleFunctions.printColumnSeparator;
import static Main.MechanicsFunctions.generateDoubleBetween;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Spell implements Cloneable{

    private String spellName;
    private MoveType spellType;
    private String spellDescription;
    private String spellSpecialAttackLine;
    private CharacterState characterState;
    private double spellLevelRequirement;
    private double[] spellDamage;
    private double[] spellEffectiveDistance;
    private double spellChance;
    private int spellCooldown;
    private int spellReadyIn;
    private String spellColor;
    final public static double maxDefenseReductionPercent = 0.2;
    final public static double parryMultiplier = 0.3;

    public static Spell wingardiumLeviosa = Spell.builder()
            .spellName("Wingardium Leviosa")
            .spellType(MoveType.ATTACK)
            .spellDescription("Summons and then throws special environmental objects at the targeted enemy.")
            .spellSpecialAttackLine("You've thrown an object at the enemy!")
            .spellLevelRequirement(1)
            .spellDamage(new double[]{100, 120})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.STUNNED)
            .spellChance(0.7)
            .spellCooldown(2)
            .spellReadyIn(0)
            .spellColor(ANSI_RED)
            .build();

    public static Spell basicCast = Spell.builder()
            .spellName("Basic Spell")
            .spellType(MoveType.ATTACK)
            .spellDescription("Deals minor damage to enemies and objects.")
            .spellSpecialAttackLine("Basic Attack!")
            .spellLevelRequirement(0)
            .spellDamage(new double[]{40, 60})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.STANDING)
            .spellChance(0.9)
            .spellCooldown(1)
            .spellReadyIn(0)
            .spellColor(ANSI_RED)
            .build();

//    public static Spell protego = Spell.builder()
//            .spellName("Protego")
//            .spellType(MoveType.PARRY)
//            .spellDescription("Protects against a variety of attacks, including spell casts, weapon strikes, and more.")
//            .spellSpecialAttackLine("You've cancelled the enemy's spell!")
//            .spellLevelRequirement(0)
//            .spellDamage(new double[]{100, 120})
//            .spellEffectiveDistance(new double[]{})
//            .characterState(CharacterState.STANDING)
//            .spellChance(0.8)
//            .spellCooldown(1)
//            .spellReadyIn(0)
//            .spellColor(ANSI_BLUE)
//            .build();

    public static Spell stupefy = Spell.builder()
            .spellName("Stupefy")
            .spellType(MoveType.FOLLOW_UP)
            .spellDescription("Stuns enemies, making them easy targets for follow-up spells.")
            .spellSpecialAttackLine("You've cancelled the enemy's spell!")
            .spellLevelRequirement(0)
            .spellDamage(new double[]{0, 0})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.STUNNED)
            .spellChance(0.8)
            .spellCooldown(0)
            .spellReadyIn(0)
            .spellColor(ANSI_YELLOW)
            .build();

    public static Spell glacius = Spell.builder()
            .spellName("Glacius")
            .spellType(MoveType.ATTACK)
            .spellDescription("Stuns enemies, making them easy targets for follow-up spells.")
            .spellSpecialAttackLine("You've cancelled the enemy's spell!")
            .spellLevelRequirement(5)
            .spellDamage(new double[]{60, 70})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.FROZEN)
            .spellChance(0.6)
            .spellCooldown(1)
            .spellReadyIn(0)
            .spellColor(ANSI_RED)
            .build();

    public static Spell transformation = Spell.builder()
            .spellName("Transformation")
            .spellType(MoveType.ATTACK)
            .spellDescription("Enemies struck with the Transformation spell transform into explosive objects.")
            .spellSpecialAttackLine("You've transformed the enemy into a ")
            .spellLevelRequirement(10)
            .spellDamage(new double[]{0, 0})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.STANDING)
            .spellChance(0.4)
            .spellCooldown(2)
            .spellReadyIn(0)
            .spellColor(ANSI_RED)
            .build();

    public static Spell accio = Spell.builder()
            .spellName("Accio")
            .spellType(MoveType.ATTACK)
            .spellDescription("Summon a variety of objects and enemies to close range.")
            .spellSpecialAttackLine("The enemy has been pulled close to you!")
            .spellLevelRequirement(0)
            .spellDamage(new double[]{50, 80})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.LEVITATING)
            .spellChance(0.7)
            .spellCooldown(1)
            .spellReadyIn(0)
            .spellColor(ANSI_RED)
            .build();

    public static Spell descendo = Spell.builder()
            .spellName("Descendo")
            .spellType(MoveType.ATTACK)
            .spellDescription("Deals no direct damage, but objects and enemies that are slammed to the ground will suffer considerable impact damage. Airborne enemies will take even greater damage upon hitting the ground.")
            .spellSpecialAttackLine("You've slammed the enemy into the ground!")
            .spellLevelRequirement(4)
            .spellDamage(new double[]{70, 90})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.KNOCKED)
            .spellChance(0.7)
            .spellCooldown(1)
            .spellReadyIn(0)
            .spellColor(ANSI_RED)
            .build();

    public static Spell depulso = Spell.builder()
            .spellName("Depulso")
            .spellType(MoveType.ATTACK)
            .spellDescription("Repels many types of objects and enemies with considerable force.")
            .spellSpecialAttackLine("You've kicked the enemy into the wall!")
            .spellLevelRequirement(6)
            .spellDamage(new double[]{80, 100})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.KNOCKED)
            .spellChance(0.6)
            .spellCooldown(2)
            .spellReadyIn(0)
            .spellColor(ANSI_RED)
            .build();

    public static Spell bombarda = Spell.builder()
            .spellName("Bombarda")
            .spellType(MoveType.ATTACK)
            .spellDescription("Deals heavy damage on impact, accompanied by an explosion that can destroy heavy objects and hit surrounding enemies.")
            .spellSpecialAttackLine("You blew up your enemies!")
            .spellLevelRequirement(8)
            .spellDamage(new double[]{120, 140})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.ON_FIRE)
            .spellChance(0.35)
            .spellCooldown(3)
            .spellReadyIn(0)
            .spellColor(ANSI_RED)
            .build();

    public static Spell confringo = Spell.builder()
            .spellName("Confringo")
            .spellType(MoveType.ATTACK)
            .spellDescription("A long-range bolt that deals damage on impact, and set enemies on fire.")
            .spellSpecialAttackLine("Your enemy is on fire!")
            .spellLevelRequirement(5)
            .spellDamage(new double[]{60, 80})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.ON_FIRE)
            .spellChance(0.6)
            .spellCooldown(2)
            .spellReadyIn(0)
            .spellColor(ANSI_RED)
            .build();

    public static Spell diffindo = Spell.builder()
            .spellName("Diffindo")
            .spellType(MoveType.ATTACK)
            .spellDescription("Slashes objects and enemies from afar dealing considerable damage.")
            .spellSpecialAttackLine("Your enemy has been slashed!")
            .spellLevelRequirement(7)
            .spellDamage(new double[]{60, 80})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.STANDING)
            .spellChance(0.6)
            .spellCooldown(1)
            .spellReadyIn(0)
            .spellColor(ANSI_RED)
            .build();

    public static Spell incendio = Spell.builder()
            .spellName("Incendio")
            .spellType(MoveType.ATTACK)
            .spellDescription("Deals significant damage and lights certain objects on fire, but its range is short and requires you to be close to the target.")
            .spellSpecialAttackLine("Your enemy has been slashed!")
            .spellLevelRequirement(7)
            .spellDamage(new double[]{60, 80})
            .spellEffectiveDistance(new double[]{0, 10})
            .characterState(CharacterState.ON_FIRE)
            .spellChance(0.6)
            .spellCooldown(1)
            .spellReadyIn(0)
            .spellColor(ANSI_RED)
            .build();

    public static Spell avadaKedavra = Spell.builder()
            .spellName("Avada Kedavra")
            .spellType(MoveType.ATTACK)
            .spellDescription("Kills enemies instantly.")
            .spellSpecialAttackLine("the enemy is kinda dead")
            .spellLevelRequirement(10)
            .spellDamage(new double[]{100000, 100000})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.STANDING)
            .spellChance(1)
            .spellCooldown(6)
            .spellReadyIn(0)
            .spellColor(ANSI_RED)
            .build();

    public static Spell crucio = Spell.builder()
            .spellName("Crucio")
            .spellType(MoveType.ATTACK)
            .spellDescription("Curses the victim, cursed enemies take extra damage.")
            .spellSpecialAttackLine("You've cursed the enemy!")
            .spellLevelRequirement(8)
            .spellDamage(new double[]{50, 70})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.CURSED)
            .spellChance(0.8)
            .spellCooldown(4)
            .spellReadyIn(0)
            .spellColor(ANSI_RED)
            .build();

    public static Spell testSpell = Spell.builder()
            .spellName("Test Spell")
            .spellType(MoveType.ATTACK)
            .spellDescription("test spell")
            .spellSpecialAttackLine("test spell attack line")
            .spellLevelRequirement(0)
            .spellDamage(new double[]{20000000, 20000000})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.STUNNED)
            .spellChance(1)
            .spellCooldown(0)
            .spellReadyIn(0)
            .spellColor(ANSI_RED)
            .build();


    public static int getSpellNameMaxLength() {
        return getAllSpellsNamesList().stream().map(String::length).toList().stream().reduce(0, Integer::max);
    }

    public static List<Spell> getAllSpells(){
        List<Spell> spellList = new ArrayList<>();
        Field[] declaredFields = Spell.class.getDeclaredFields();

        for(Field field:declaredFields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                if (Spell.class.isAssignableFrom(field.getType())) {
                    try {
                        spellList.add((Spell) field.get(null));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return spellList;
    }

    public static List<String> getAllSpellsNamesList() {
        List<String> spellNameList = new ArrayList<>();

        for(Spell spell: getAllSpells()) {
            spellNameList.add(spell.getSpellName());
        }
        return spellNameList;
    }



    public static void printSeparator(int length) {
        for(int i = 0; i < length; i++) {
            System.out.print("/");
        }
        System.out.println();
    }

    public static void printSpell(Spell AbstractSpell) {
        printSeparator(AbstractSpell.spellName.length());
        System.out.println(AbstractSpell.spellName);
        printSeparator(AbstractSpell.spellName.length());
    }

    public String printStats() {
        String spellState;
        int spellChance = (int) (this.spellChance * 100);

        String column1Format = "%-" + (getSpellNameMaxLength() + 1) + "s";
        String column2Format = "%-" + 19 + "s";
        String column3BisFormat = "%-" + 5 + "s";
        String column3Format = "%-" + 15 + "s";
        String column5Format = "%-" + 20 + "s";
        String column6Format = "%-" + 20 + "s";

        if(this.spellReadyIn > 0) {
            spellState = returnColoredText(String.format(column2Format , "Ready in " + this.spellReadyIn + " turn(s)"), ANSI_BLUE);
        }
        else {
            spellState = returnColoredText(String.format(column2Format , "Ready"), ANSI_YELLOW);
        }

        String column1 = returnColoredText(String.format(column1Format , this.getSpellName()), ANSI_PURPLE);
        String column2 = spellState;
        String column3Bis = returnColoredText(String.format(column3BisFormat , (int) generateDoubleBetween(this.spellDamage[0], this.spellDamage[1])), ANSI_RED);
        String column3 = returnColoredText(String.format(column3Format , " ~ Base Damage"), ANSI_RED);
        String column5 = returnColoredText(String.format(column5Format , spellChance + "% Chance Success"), ANSI_YELLOW);
        String column6 = returnColoredText(String.format(column6Format , this.spellCooldown + " Turn(s) Cooldown"), ANSI_BLUE);

        return column1 + printColumnSeparator("||")
                + column2 + printColumnSeparator("||")
                + column3Bis + column3 + printColumnSeparator("||")
                + column5 + printColumnSeparator("||")
                + column6;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
