package Classes;

import AbstractClasses.AbstractSpell;
import Enums.CharacterState;
import Enums.SpellType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Spell extends AbstractSpell {
    @Builder
    public Spell(String spellName, SpellType spellType, String spellDescription, String spellSpecialAttackLine, CharacterState characterState, double spellLevelRequirement, double[] spellDamage, double[] spellDefense, double[] spellEffectiveDistance, double spellChance, int spellCoolDown) {
        super(spellName, spellType, spellDescription, spellSpecialAttackLine, characterState, spellLevelRequirement, spellDamage, spellDefense, spellEffectiveDistance, spellChance, spellCoolDown);
    }

    public static Spell ancientMagicThrow = Spell.builder()
            .spellName("Ancient Magic Throw")
            .spellType(SpellType.ESSENTIAL)
            .spellDescription("Summons and then throws special environmental objects at the targeted enemy. Particularly useful for breaking through Shield Charms.")
            .spellSpecialAttackLine("You've thrown an object at the enemy!")
            .spellLevelRequirement(1)
            .spellDamage(new double[]{100, 120})
            .spellDefense(new double[]{0, 0})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.STUNNED)
            .spellChance(0.7)
            .spellCoolDown(2)
            .build();

    public static Spell basicCast = Spell.builder()
            .spellName("Basic Spell")
            .spellType(SpellType.ESSENTIAL)
            .spellDescription("Deals minor damage to enemies and objects.")
            .spellSpecialAttackLine("Basic Attack!")
            .spellLevelRequirement(0)
            .spellDamage(new double[]{40, 60})
            .spellDefense(new double[]{0, 0})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.STANDING)
            .spellChance(0.9)
            .spellCoolDown(1)
            .build();

    public static Spell protego = Spell.builder()
            .spellName("Protego")
            .spellType(SpellType.ESSENTIAL)
            .spellDescription("Protects against a variety of attacks, including spell casts, weapon strikes, and more.")
            .spellSpecialAttackLine("You've cancelled the enemy's spell!")
            .spellLevelRequirement(0)
            .spellDamage(new double[]{100, 120})
            .spellDefense(new double[]{50, 60})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.STANDING)
            .spellChance(0.8)
            .spellCoolDown(1)
            .build();

    public static Spell stupefy = Spell.builder()
            .spellName("Stupefy")
            .spellType(SpellType.ESSENTIAL)
            .spellDescription("Stuns enemies, making them easy targets for follow-up spells.")
            .spellSpecialAttackLine("You've cancelled the enemy's spell!")
            .spellLevelRequirement(0)
            .spellDamage(new double[]{60, 70})
            .spellDefense(new double[]{50, 60})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.STUNNED)
            .spellChance(0.8)
            .spellCoolDown(1)
            .build();

    public static Spell glacius = Spell.builder()
            .spellName("Glacius")
            .spellType(SpellType.CONTROL)
            .spellDescription("Stuns enemies, making them easy targets for follow-up spells.")
            .spellSpecialAttackLine("You've cancelled the enemy's spell!")
            .spellLevelRequirement(5)
            .spellDamage(new double[]{60, 70})
            .spellDefense(new double[]{50, 60})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.FROZEN)
            .spellChance(0.6)
            .spellCoolDown(1)
            .build();

    public static Spell transformation = Spell.builder()
            .spellName("Transformation")
            .spellType(SpellType.CONTROL)
            .spellDescription("Enemies struck with the Transformation spell transform into explosive objects.")
            .spellSpecialAttackLine("You've transformed the enemy into a ")
            .spellLevelRequirement(10)
            .spellDamage(new double[]{0, 0})
            .spellDefense(new double[]{0, 0})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.STANDING)
            .spellChance(0.4)
            .spellCoolDown(2)
            .build();

    public static Spell accio = Spell.builder()
            .spellName("Accio")
            .spellType(SpellType.FORCE)
            .spellDescription("Summon a variety of objects and enemies to close range.")
            .spellSpecialAttackLine("The enemy has been pulled close to you!")
            .spellLevelRequirement(0)
            .spellDamage(new double[]{0, 0})
            .spellDefense(new double[]{0, 0})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.LEVITATING)
            .spellChance(0.7)
            .spellCoolDown(1)
            .build();

    public static Spell descendo = Spell.builder()
            .spellName("Descendo")
            .spellType(SpellType.FORCE)
            .spellDescription("Deals no direct damage, but objects and enemies that are slammed to the ground will suffer considerable impact damage. Airborne enemies will take even greater damage upon hitting the ground.")
            .spellSpecialAttackLine("You've slammed the enemy into the ground!")
            .spellLevelRequirement(4)
            .spellDamage(new double[]{70, 90})
            .spellDefense(new double[]{0, 0})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.KNOCKED)
            .spellChance(0.7)
            .spellCoolDown(1)
            .build();

    public static Spell depulso = Spell.builder()
            .spellName("Depulso")
            .spellType(SpellType.FORCE)
            .spellDescription("Repels many types of objects and enemies with considerable force.")
            .spellSpecialAttackLine("You've kicked the enemy into the wall!")
            .spellLevelRequirement(6)
            .spellDamage(new double[]{80, 100})
            .spellDefense(new double[]{0, 0})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.KNOCKED)
            .spellChance(0.6)
            .spellCoolDown(2)
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
            .spellCoolDown(3)
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
            .spellCoolDown(2)
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
            .spellCoolDown(1)
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
            .spellCoolDown(1)
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

        for(Spell spell:Spell.getAllSpells()) {
            spellNameList.add(spell.getSpellName());
        }
        return spellNameList;
    }

}