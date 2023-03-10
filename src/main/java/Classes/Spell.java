package Classes;

import AbstractClasses.AbstractSpell;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Spell extends AbstractSpell {
    public Spell(String spellName, double spellDamage, double spellDefense, double spellChance, int spellCoolDown) {
        super(spellName, spellDamage, spellDefense, spellChance, spellCoolDown);
    }

    @Override
    public double specialAttack() {
        return 0;
    }
}
