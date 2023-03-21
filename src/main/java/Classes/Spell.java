package Classes;

import Enums.CharacterState;
import Enums.SpellType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Classes.Color.*;
import static Classes.Color.printColoredText;
import static Enums.EnumMethods.returnFormattedEnum;
import static Main.MechanicsFunctions.generateDoubleBetween;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Spell implements Cloneable{

    private String spellName;
    private SpellType spellType;
    private String spellDescription;
    private String spellSpecialAttackLine;
    private CharacterState characterState;
    private double spellLevelRequirement;
    private double[] spellDamage;
    private double[] spellDefense;
    private double[] spellEffectiveDistance;
    private double spellChance;
    private int spellCooldown;
    private int spellReadyIn;

    public static Spell magicThrow = Spell.builder()
            .spellName("Magic Throw")
            .spellType(SpellType.DAMAGE)
            .spellDescription("Summons and then throws special environmental objects at the targeted enemy.")
            .spellSpecialAttackLine("You've thrown an object at the enemy!")
            .spellLevelRequirement(1)
            .spellDamage(new double[]{100, 120})
            .spellDefense(new double[]{0, 0})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.STUNNED)
            .spellChance(0.7)
            .spellCooldown(2)
            .spellReadyIn(0)
            .build();

    public static Spell basicCast = Spell.builder()
            .spellName("Basic Spell")
            .spellType(SpellType.DAMAGE)
            .spellDescription("Deals minor damage to enemies and objects.")
            .spellSpecialAttackLine("Basic Attack!")
            .spellLevelRequirement(0)
            .spellDamage(new double[]{40, 60})
            .spellDefense(new double[]{0, 0})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.STANDING)
            .spellChance(0.9)
            .spellCooldown(1)
            .spellReadyIn(0)
            .build();

    public static Spell protego = Spell.builder()
            .spellName("Protego")
            .spellType(SpellType.DEFENSE)
            .spellDescription("Protects against a variety of attacks, including spell casts, weapon strikes, and more.")
            .spellSpecialAttackLine("You've cancelled the enemy's spell!")
            .spellLevelRequirement(0)
            .spellDamage(new double[]{100, 120})
            .spellDefense(new double[]{50, 60})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.STANDING)
            .spellChance(0.8)
            .spellCooldown(1)
            .spellReadyIn(0)
            .build();

    public static Spell stupefy = Spell.builder()
            .spellName("Stupefy")
            .spellType(SpellType.FOLLOW_UP)
            .spellDescription("Stuns enemies, making them easy targets for follow-up spells.")
            .spellSpecialAttackLine("You've cancelled the enemy's spell!")
            .spellLevelRequirement(0)
            .spellDamage(new double[]{60, 70})
            .spellDefense(new double[]{50, 60})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.STUNNED)
            .spellChance(0.8)
            .spellCooldown(1)
            .spellReadyIn(0)
            .build();

    public static Spell glacius = Spell.builder()
            .spellName("Glacius")
            .spellType(SpellType.DAMAGE)
            .spellDescription("Stuns enemies, making them easy targets for follow-up spells.")
            .spellSpecialAttackLine("You've cancelled the enemy's spell!")
            .spellLevelRequirement(5)
            .spellDamage(new double[]{60, 70})
            .spellDefense(new double[]{0, 0})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.FROZEN)
            .spellChance(0.6)
            .spellCooldown(1)
            .spellReadyIn(0)
            .build();

    public static Spell transformation = Spell.builder()
            .spellName("Transformation")
            .spellType(SpellType.DAMAGE)
            .spellDescription("Enemies struck with the Transformation spell transform into explosive objects.")
            .spellSpecialAttackLine("You've transformed the enemy into a ")
            .spellLevelRequirement(10)
            .spellDamage(new double[]{0, 0})
            .spellDefense(new double[]{0, 0})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.STANDING)
            .spellChance(0.4)
            .spellCooldown(2)
            .spellReadyIn(0)
            .build();

    public static Spell accio = Spell.builder()
            .spellName("Accio")
            .spellType(SpellType.DAMAGE)
            .spellDescription("Summon a variety of objects and enemies to close range.")
            .spellSpecialAttackLine("The enemy has been pulled close to you!")
            .spellLevelRequirement(0)
            .spellDamage(new double[]{0, 0})
            .spellDefense(new double[]{0, 0})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.LEVITATING)
            .spellChance(0.7)
            .spellCooldown(1)
            .spellReadyIn(0)
            .build();

    public static Spell descendo = Spell.builder()
            .spellName("Descendo")
            .spellType(SpellType.DAMAGE)
            .spellDescription("Deals no direct damage, but objects and enemies that are slammed to the ground will suffer considerable impact damage. Airborne enemies will take even greater damage upon hitting the ground.")
            .spellSpecialAttackLine("You've slammed the enemy into the ground!")
            .spellLevelRequirement(4)
            .spellDamage(new double[]{70, 90})
            .spellDefense(new double[]{0, 0})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.KNOCKED)
            .spellChance(0.7)
            .spellCooldown(1)
            .spellReadyIn(0)
            .build();

    public static Spell depulso = Spell.builder()
            .spellName("Depulso")
            .spellType(SpellType.DAMAGE)
            .spellDescription("Repels many types of objects and enemies with considerable force.")
            .spellSpecialAttackLine("You've kicked the enemy into the wall!")
            .spellLevelRequirement(6)
            .spellDamage(new double[]{80, 100})
            .spellDefense(new double[]{0, 0})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.KNOCKED)
            .spellChance(0.6)
            .spellCooldown(2)
            .spellReadyIn(0)
            .build();

    public static Spell bombarda = Spell.builder()
            .spellName("Bombarda")
            .spellType(SpellType.DAMAGE)
            .spellDescription("Deals heavy damage on impact, accompanied by an explosion that can destroy heavy objects and hit surrounding enemies.")
            .spellSpecialAttackLine("You blew up your enemies!")
            .spellLevelRequirement(8)
            .spellDamage(new double[]{120, 140})
            .spellDefense(new double[]{0, 0})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.ON_FIRE)
            .spellChance(0.35)
            .spellCooldown(3)
            .spellReadyIn(0)
            .build();

    public static Spell confringo = Spell.builder()
            .spellName("Confringo")
            .spellType(SpellType.DAMAGE)
            .spellDescription("A long-range bolt that deals damage on impact, and set enemies on fire.")
            .spellSpecialAttackLine("Your enemy is on fire!")
            .spellLevelRequirement(5)
            .spellDamage(new double[]{60, 80})
            .spellDefense(new double[]{0, 0})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.ON_FIRE)
            .spellChance(0.6)
            .spellCooldown(2)
            .spellReadyIn(0)
            .build();

    public static Spell diffindo = Spell.builder()
            .spellName("Diffindo")
            .spellType(SpellType.DAMAGE)
            .spellDescription("Slashes objects and enemies from afar dealing considerable damage.")
            .spellSpecialAttackLine("Your enemy has been slashed!")
            .spellLevelRequirement(7)
            .spellDamage(new double[]{60, 80})
            .spellDefense(new double[]{0, 0})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.STANDING)
            .spellChance(0.6)
            .spellCooldown(1)
            .spellReadyIn(0)
            .build();

    public static Spell incendio = Spell.builder()
            .spellName("Incendio")
            .spellType(SpellType.DAMAGE)
            .spellDescription("Deals significant damage and lights certain objects on fire, but its range is short and requires you to be close to the target.")
            .spellSpecialAttackLine("Your enemy has been slashed!")
            .spellLevelRequirement(7)
            .spellDamage(new double[]{60, 80})
            .spellDefense(new double[]{0, 0})
            .spellEffectiveDistance(new double[]{0, 10})
            .characterState(CharacterState.STANDING)
            .spellChance(0.6)
            .spellCooldown(1)
            .spellReadyIn(0)
            .build();

    public static Spell avadaKedavra = Spell.builder()
            .spellName("Avada Kedavra")
            .spellType(SpellType.DAMAGE)
            .spellDescription("Kills enemies instantly.")
            .spellSpecialAttackLine("the enemy is kinda dead")
            .spellLevelRequirement(10)
            .spellDamage(new double[]{100000, 100000})
            .spellDefense(new double[]{0, 0})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.STANDING)
            .spellChance(1)
            .spellCooldown(6)
            .spellReadyIn(0)
            .build();

    public static Spell crucio = Spell.builder()
            .spellName("Crucio")
            .spellType(SpellType.DAMAGE)
            .spellDescription("Curses the victim, cursed enemies take extra damage.")
            .spellSpecialAttackLine("You've cursed the enemy!")
            .spellLevelRequirement(8)
            .spellDamage(new double[]{50, 70})
            .spellDefense(new double[]{0, 0})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.CURSED)
            .spellChance(0.8)
            .spellCooldown(4)
            .spellReadyIn(0)
            .build();

    public static Spell testSpell = Spell.builder()
            .spellName("Test Spell")
            .spellType(SpellType.DAMAGE)
            .spellDescription("test spell")
            .spellSpecialAttackLine("test spell attack line")
            .spellLevelRequirement(0)
            .spellDamage(new double[]{200, 200})
            .spellDefense(new double[]{0, 0})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.STUNNED)
            .spellChance(1)
            .spellCooldown(2)
            .spellReadyIn(0)
            .build();


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
        String spellState = "(Ready)";
        if(this.spellReadyIn > 0) {
            spellState = "(Ready in " + this.spellReadyIn + " turns)";
        }

        int spellChance = (int) (this.spellChance * 100);

        return
                printColoredText(this.getSpellName() + "\t", ANSI_PURPLE)
                + printColoredText(spellState + "\t", ANSI_YELLOW)
                + printColoredText((int) generateDoubleBetween(this.spellDamage[0], this.spellDamage[1]) + " Approx Base Damage\t", ANSI_RED)
                + printColoredText((int) generateDoubleBetween(this.spellDefense[0], this.spellDefense[1]) + " Approx Base Defense\t", ANSI_BLUE)
//                + printColoredText(generateDoubleBetween(this.spellEffectiveDistance[0], this.spellEffectiveDistance[1]) + " Approx Effective Distance", ANSI_CYAN)
                + printColoredText(spellChance + "% Chance Success\t", ANSI_YELLOW)
                + printColoredText(this.spellCooldown + " Turn(s) Cooldown\t", ANSI_BLUE)
                + returnFormattedEnum(this.spellType);
    }

    public static void useSpell(Spell AbstractSpell) {
        printSpell(AbstractSpell);

    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
