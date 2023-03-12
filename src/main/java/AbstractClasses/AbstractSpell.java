package AbstractClasses;

import Classes.Spell;
import Enums.CharacterState;
import Enums.SpellType;
import lombok.Getter;
import lombok.Setter;

import javax.xml.stream.events.Characters;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public abstract class AbstractSpell {

    public AbstractSpell(String spellName, SpellType spellType, String spellDescription, String spellSpecialAttackLine, CharacterState characterState, double spellLevelRequirement, double[] spellDamage, double[] spellDefense, double[] spellEffectiveDistance, double spellChance, int spellCoolDown) {
        this.spellName = spellName;
        this.spellType = spellType;
        this.spellDescription = spellDescription;
        this.spellSpecialAttackLine = spellSpecialAttackLine;
        this.characterState = characterState;
        this.spellLevelRequirement = spellLevelRequirement;
        this.spellDamage = spellDamage;
        this.spellDefense = spellDefense;
        this.spellEffectiveDistance = spellEffectiveDistance;
        this.spellChance = spellChance;
        this.spellCoolDown = spellCoolDown;
    }

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
    private int spellCoolDown;

    public static void printSeparator(int length) {
        for(int i = 0; i < length; i++) {
            System.out.print("/");
        }
        System.out.println();
    }

    public static void printSpell(AbstractSpell AbstractSpell) {
        printSeparator(AbstractSpell.spellName.length());
        System.out.println(AbstractSpell.spellName);
        printSeparator(AbstractSpell.spellName.length());
    }

    public static void useSpell(AbstractSpell AbstractSpell) {
        printSpell(AbstractSpell);

    }

}
