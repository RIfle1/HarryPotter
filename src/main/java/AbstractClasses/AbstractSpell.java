package AbstractClasses;

import Classes.Spell;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractSpell {
    public AbstractSpell(String spellName, double spellDamage, double spellDefense, double spellChance, int spellCoolDown) {
        this.spellName = spellName;
        this.spellDamage = spellDamage;
        this.spellDefense = spellDefense;
        this.spellChance = spellChance;
        this.spellCoolDown = spellCoolDown;
    }

    private String spellName;
    private double spellDamage;
    private double spellDefense;
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
    public abstract double specialAttack();
}
