package Main;

import Classes.Enemy;
import Classes.Spell;
import Enums.EnemyName;

import static Classes.Enemy.*;
import static Classes.Wizard.wizard;

public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {
//        gameCredits();
//        checkSaves();
//        CharacterCreation.characterInit();

        enemiesHashMap = generateEnemies(10, 10, 1, EnemyName.DARK_WIZARD, wizard.getDifficulty());

        System.out.println(enemiesHashMap);
        System.out.println(enemiesKeyList);

//        wizard.updateStats();
//        wizard.printStats();
//        wizard.attack(wizard.getSpell(Spell.testSpell), enemies.get("Dark Wizard-1"));


    }
}