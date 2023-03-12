package Classes;

import AbstractClasses.AbstractSpell;
import Enums.CharacterState;
import Enums.SpellType;
import lombok.Builder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ForbiddenSpell extends AbstractSpell {
    @Builder
    public ForbiddenSpell(String spellName, SpellType spellType, String spellDescription, String spellSpecialAttackLine, CharacterState characterState, double spellLevelRequirement, double[] spellDamage, double[] spellDefense, double[] spellEffectiveDistance, double spellChance, int spellCoolDown) {
        super(spellName, spellType, spellDescription, spellSpecialAttackLine, characterState, spellLevelRequirement, spellDamage, spellDefense, spellEffectiveDistance, spellChance, spellCoolDown);
    }

    public static ForbiddenSpell avadaKedavra = ForbiddenSpell.builder()
            .spellName("Avada Kedavra")
            .spellType(SpellType.UNFORGIVABLE_CURSE)
            .spellDescription("Kills enemies instantly.")
            .spellSpecialAttackLine("the enemy is kinda dead")
            .spellLevelRequirement(10)
            .spellDamage(new double[]{100000, 100000})
            .spellDefense(new double[]{0, 0})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.STANDING)
            .spellChance(1)
            .spellCoolDown(6)
            .build();

    public static ForbiddenSpell crucio = ForbiddenSpell.builder()
            .spellName("Crucio")
            .spellType(SpellType.UNFORGIVABLE_CURSE)
            .spellDescription("Curses the victim, cursed enemies take extra damage.")
            .spellSpecialAttackLine("You've cursed the enemy!")
            .spellLevelRequirement(8)
            .spellDamage(new double[]{50, 70})
            .spellDefense(new double[]{0, 0})
            .spellEffectiveDistance(new double[]{})
            .characterState(CharacterState.CURSED)
            .spellChance(0.8)
            .spellCoolDown(4)
            .build();


    public static List<ForbiddenSpell> getForbiddenSpellList() {
        List<ForbiddenSpell> forbiddenSpellList = new ArrayList<>();
        Field[] declaredFields = ForbiddenSpell.class.getDeclaredFields();

        for (Field field : declaredFields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                if (ForbiddenSpell.class.isAssignableFrom(field.getType())) {
                    try {
                        forbiddenSpellList.add((ForbiddenSpell) field.get(null));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return forbiddenSpellList;
    }
}
