package project.classes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.enums.CharacterState;
import project.enums.MoveType;
import project.functions.ConsoleFunctions;
import project.functions.GeneralFunctions;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
    private double spellChance;
    private int spellCooldown;
    private int spellReadyIn;
    private String spellColor;
    private String spellImg;
    final public static double maxDefenseReductionPercent = 0.2;
    final public static double parryMultiplier = 1;

    public static Spell wingardiumLeviosa = Spell.builder()
            .spellName("Wingardium Leviosa")
            .spellType(MoveType.ATTACK)
            .spellDescription("Summons and then throws special environmental objects at the targeted enemy.")
            .spellSpecialAttackLine("You've thrown an object at the enemy!")
            .spellLevelRequirement(0)
            .spellDamage(new double[]{50, 90})
            .characterState(CharacterState.LEVITATING)
            .spellChance(0.9)
            .spellCooldown(0)
            .spellReadyIn(0)
            .spellColor(Color.ANSI_RED)
            .spellImg("Damage Spell")
            .build();

    public static Spell basicCast = Spell.builder()
            .spellName("Basic Cast")
            .spellType(MoveType.ATTACK)
            .spellDescription("Deals minor damage to enemies and objects.")
            .spellSpecialAttackLine("A bright light rushes towards the enemy!")
            .spellLevelRequirement(0)
            .spellDamage(new double[]{65, 70})
            .characterState(CharacterState.STANDING)
            .spellChance(0.9)
            .spellCooldown(0)
            .spellReadyIn(0)
            .spellColor(Color.ANSI_RED)
            .spellImg("Damage Spell")
            .build();

//    public static Spell protego = Spell.builder()
//            .spellName("Protego")
//            .spellType(MoveType.PARRY)
//            .spellDescription("Protects against a variety of attacks, including spell casts, weapon strikes, and more.")
//            .spellSpecialAttackLine("You've cancelled the enemy's spell!")
//            .spellLevelRequirement(0)
//            .spellDamage(new double[]{100, 120})
//            .characterState(CharacterState.STANDING)
//            .spellChance(0.8)
//            .spellCooldown(2)
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
            .characterState(CharacterState.KNOCKED)
            .spellChance(0.8)
            .spellCooldown(1)
            .spellReadyIn(0)
            .spellColor(Color.ANSI_YELLOW)
            .spellImg("FollowUp Spell")
            .build();

    public static Spell glacius = Spell.builder()
            .spellName("Glacius")
            .spellType(MoveType.ATTACK)
            .spellDescription("Stuns enemies, making them easy targets for follow-up spells.")
            .spellSpecialAttackLine("You've cancelled the enemy's spell!")
            .spellLevelRequirement(5)
            .spellDamage(new double[]{80, 100})
            .characterState(CharacterState.FROZEN)
            .spellChance(0.6)
            .spellCooldown(2)
            .spellReadyIn(0)
            .spellColor(Color.ANSI_RED)
            .spellImg("Damage Spell")
            .build();

//    public static Spell transformation = Spell.builder()
//            .spellName("Transformation")
//            .spellType(MoveType.ATTACK)
//            .spellDescription("Enemies struck with the Transformation spell transform into explosive objects.")
//            .spellSpecialAttackLine("You've transformed the enemy into a ")
//            .spellLevelRequirement(10)
//            .spellDamage(new double[]{0, 0})
//            .characterState(CharacterState.STANDING)
//            .spellChance(0.4)
//            .spellCooldown(2)
//            .spellReadyIn(0)
//            .spellColor(ANSI_RED)
//            .build();

    public static Spell accio = Spell.builder()
            .spellName("Accio")
            .spellType(MoveType.ATTACK)
            .spellDescription("Summon a variety of objects and enemies to close range.")
            .spellSpecialAttackLine("The enemy has been pulled close to you!")
            .spellLevelRequirement(1)
            .spellDamage(new double[]{60, 90})
            .characterState(CharacterState.LEVITATING)
            .spellChance(0.7)
            .spellCooldown(2)
            .spellReadyIn(0)
            .spellColor(Color.ANSI_RED)
            .spellImg("Damage Spell")
            .build();

//    public static Spell descendo = Spell.builder()
//            .spellName("Descendo")
//            .spellType(MoveType.ATTACK)
//            .spellDescription("Deals no direct damage, but objects and enemies that are slammed to the ground will suffer considerable impact damage. Airborne enemies will take even greater damage upon hitting the ground.")
//            .spellSpecialAttackLine("You've slammed the enemy into the ground!")
//            .spellLevelRequirement(4)
//            .spellDamage(new double[]{70, 90})
//            .characterState(CharacterState.KNOCKED)
//            .spellChance(0.7)
//            .spellCooldown(2)
//            .spellReadyIn(0)
//            .spellColor(Color.ANSI_RED)
//            .spellImg("Damage Spell")
//            .build();

    public static Spell depulso = Spell.builder()
            .spellName("Depulso")
            .spellType(MoveType.ATTACK)
            .spellDescription("Repels many types of objects and enemies with considerable force.")
            .spellSpecialAttackLine("You've kicked the enemy into the wall!")
            .spellLevelRequirement(6)
            .spellDamage(new double[]{80, 110})
            .characterState(CharacterState.KNOCKED)
            .spellChance(0.6)
            .spellCooldown(2)
            .spellReadyIn(0)
            .spellColor(Color.ANSI_RED)
            .spellImg("Damage Spell")
            .build();

    public static Spell bombarda = Spell.builder()
            .spellName("Bombarda")
            .spellType(MoveType.ATTACK)
            .spellDescription("Deals heavy damage on impact, accompanied by an explosion that can destroy heavy objects and hit surrounding enemies.")
            .spellSpecialAttackLine("You send an explosion towards your enemies!")
            .spellLevelRequirement(8)
            .spellDamage(new double[]{150, 200})
            .characterState(CharacterState.ON_FIRE)
            .spellChance(0.4)
            .spellCooldown(3)
            .spellReadyIn(0)
            .spellColor(Color.ANSI_RED)
            .spellImg("Damage Spell")
            .build();

    public static Spell confringo = Spell.builder()
            .spellName("Confringo")
            .spellType(MoveType.ATTACK)
            .spellDescription("A long-range bolt that deals damage on impact, and set enemies on fire.")
            .spellSpecialAttackLine("Your enemy is on fire!")
            .spellLevelRequirement(5)
            .spellDamage(new double[]{90, 110})
            .characterState(CharacterState.ON_FIRE)
            .spellChance(0.6)
            .spellCooldown(2)
            .spellReadyIn(0)
            .spellColor(Color.ANSI_RED)
            .spellImg("Damage Spell")
            .build();

    public static Spell diffindo = Spell.builder()
            .spellName("Diffindo")
            .spellType(MoveType.ATTACK)
            .spellDescription("Slashes objects and enemies from afar dealing considerable damage.")
            .spellSpecialAttackLine("Your enemy has been slashed!")
            .spellLevelRequirement(7)
            .spellDamage(new double[]{70, 110})
            .characterState(CharacterState.STANDING)
            .spellChance(0.6)
            .spellCooldown(2)
            .spellReadyIn(0)
            .spellColor(Color.ANSI_RED)
            .spellImg("Damage Spell")
            .build();

    public static Spell incendio = Spell.builder()
            .spellName("Incendio")
            .spellType(MoveType.ATTACK)
            .spellDescription("Deals significant damage and lights certain objects on fire, but its range is short and requires you to be close to the target.")
            .spellSpecialAttackLine("Your enemy has been slashed!")
            .spellLevelRequirement(7)
            .spellDamage(new double[]{80, 110})
            .characterState(CharacterState.ON_FIRE)
            .spellChance(0.6)
            .spellCooldown(2)
            .spellReadyIn(0)
            .spellColor(Color.ANSI_RED)
            .spellImg("Damage Spell")
            .build();

    public static Spell avadaKedavra = Spell.builder()
            .spellName("Avada Kedavra")
            .spellType(MoveType.ATTACK)
            .spellDescription("Kills enemies instantly.")
            .spellSpecialAttackLine("the enemy is kinda dead")
            .spellLevelRequirement(10)
            .spellDamage(new double[]{100000, 100000})
            .characterState(CharacterState.STANDING)
            .spellChance(1)
            .spellCooldown(6)
            .spellReadyIn(0)
            .spellColor(Color.ANSI_RED)
            .spellImg("Forbidden Spell")
            .build();

    public static Spell crucio = Spell.builder()
            .spellName("Crucio")
            .spellType(MoveType.ATTACK)
            .spellDescription("Curses the victim, cursed enemies take extra damage.")
            .spellSpecialAttackLine("You've cursed the enemy!")
            .spellLevelRequirement(8)
            .spellDamage(new double[]{200, 350})
            .characterState(CharacterState.CURSED)
            .spellChance(0.8)
            .spellCooldown(4)
            .spellReadyIn(0)
            .spellColor(Color.ANSI_RED)
            .spellImg("Forbidden Spell")
            .build();

    public static Spell legendarySword = Spell.builder()
            .spellName("Legendary Sword")
            .spellType(MoveType.ATTACK)
            .spellDescription("Godric Gryffindor's Legendary Sword.")
            .spellSpecialAttackLine("You've slashed the enemy!")
            .spellLevelRequirement(-1)
            .spellDamage(new double[]{1000, 1000})
            .characterState(CharacterState.BLEEDING)
            .spellChance(0.8)
            .spellCooldown(0)
            .spellReadyIn(0)
            .spellColor(Color.ANSI_YELLOW)
            .spellImg("Damage Spell")
            .build();

    public static Spell expectroPatronum = Spell.builder()
            .spellName("Expectro Patronum")
            .spellType(MoveType.ATTACK)
            .spellDescription("Spell used to kill dementors")
            .spellSpecialAttackLine("A white light comes whizzing out of your wand.")
            .spellLevelRequirement(3)
            .spellDamage(new double[]{60, 90})
            .characterState(CharacterState.STUNNED)
            .spellChance(0.7)
            .spellCooldown(0)
            .spellReadyIn(0)
            .spellColor(Color.ANSI_BLUE)
            .spellImg("Damage Spell")
            .build();

    public static Spell sectumsempra = Spell.builder()
            .spellName("Sectumsempra")
            .spellType(MoveType.ATTACK)
            .spellDescription("Spell used to kill death eaters")
            .spellSpecialAttackLine("Sends a chill down the enemy's spine.")
            .spellLevelRequirement(5)
            .spellDamage(new double[]{60, 90})
            .characterState(CharacterState.STANDING)
            .spellChance(0.7)
            .spellCooldown(0)
            .spellReadyIn(0)
            .spellColor(Color.ANSI_RED)
            .spellImg("Damage Spell")
            .build();

    public static Spell expelliarmus = Spell.builder()
            .spellName("Expelliarmus")
            .spellType(MoveType.ATTACK)
            .spellDescription("Another spell that does something")
            .spellSpecialAttackLine("The enemy got expelled (lol).")
            .spellLevelRequirement(6)
            .spellDamage(new double[]{100, 120})
            .characterState(CharacterState.STANDING)
            .spellChance(0.6)
            .spellCooldown(2)
            .spellReadyIn(0)
            .spellColor(Color.ANSI_RED)
            .spellImg("Damage Spell")
            .build();

//    public static Spell oneClap = Spell.builder()
//            .spellName("One Clap")
//            .spellType(MoveType.ATTACK)
//            .spellDescription("enemy insta ded")
//            .spellSpecialAttackLine("The enemy got one clapped")
//            .spellLevelRequirement(0)
//            .spellDamage(new double[]{20000000, 20000000})
//            .characterState(CharacterState.STUNNED)
//            .spellChance(1)
//            .spellCooldown(1)
//            .spellReadyIn(0)
//            .spellColor(ANSI_RED)
//            .build();


    public static int getSpellNameMaxLength() {
        return returnAllSpellsNamesList().stream().map(String::length).toList().stream().reduce(0, Integer::max);
    }

    public static List<Spell> returnAllSpells(){
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

    public static List<String> returnAllSpellsNamesList() {
        List<String> spellNameList = new ArrayList<>();

        for(Spell spell: returnAllSpells()) {
            spellNameList.add(spell.getSpellName());
        }
        return spellNameList;
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
            spellState = Color.returnColoredText(String.format(column2Format , "Ready in " + this.spellReadyIn + " turn(s)"), Color.ANSI_BLUE);
        }
        else {
            spellState = Color.returnColoredText(String.format(column2Format , "Ready"), Color.ANSI_YELLOW);
        }

        String column1 = Color.returnColoredText(String.format(column1Format , this.getSpellName()), Color.ANSI_PURPLE);
        String column2 = spellState;
        String column3Bis = Color.returnColoredText(String.format(column3BisFormat , (int) GeneralFunctions.generateDoubleBetween(this.spellDamage[0], this.spellDamage[1])), Color.ANSI_RED);
        String column3 = Color.returnColoredText(String.format(column3Format , " ~ Base Damage"), Color.ANSI_RED);
        String column5 = Color.returnColoredText(String.format(column5Format , spellChance + "% Chance Success"), Color.ANSI_YELLOW);
        String column6 = Color.returnColoredText(String.format(column6Format , this.spellCooldown + " Turn(s) Cooldown"), Color.ANSI_BLUE);

        return column1 + ConsoleFunctions.printColumnSeparator("||")
                + column2 + ConsoleFunctions.printColumnSeparator("||")
                + column3Bis + column3 + ConsoleFunctions.printColumnSeparator("||")
                + column5 + ConsoleFunctions.printColumnSeparator("||")
                + column6;
    }

    @Override
    public Spell clone() throws CloneNotSupportedException {
        return (Spell) super.clone();
    }
}
