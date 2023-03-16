package Main;

import Classes.Enemy;
import Classes.Spell;
import Classes.Wizard;
import Enums.EnemyName;

import static Classes.Enemy.enemies;
import static Classes.Wizard.testWizard;

public class Main {
    public static void main(String[] args) {
//        gameCredits();
//
//        checkSaves();
//        CharacterCreation.characterInit();

        enemies = Enemy.generateEnemies(10, 10, 1, EnemyName.DARK_WIZARD, testWizard.getDifficulty());

//        testWizard.setDefensePoints(500);
//        testWizard.setCharisma(20);
//        testWizard.setStrength(25);
//        testWizard.setIntelligence(28);
////        testWizard.setLuck(15);

//        testWizard.attack(Spell.testSpell, enemies.get(0));
//        testWizard.reduceSpellsCooldown();
//        testWizard.attack(Spell.testSpell, enemies.get(0));
//        System.out.println("Level " + testWizard.getLevel());
//        System.out.println("HP: " + testWizard.getHealthPoints());
//        System.out.println("DF: " + testWizard.getDefensePoints());
//        System.out.println("---------------");
        testWizard.updateStats();
        enemies.get(0).attack(Spell.testSpell, testWizard);
        testWizard.attack(Spell.testSpell, enemies.get(0));

        System.out.println(testWizard.getSpellList().stream().map(Spell::getSpellName).toList());

        System.out.println(enemies.get(0).getHealthPoints());
        System.out.println(enemies.get(0).getDefensePoints());
//        Wizard.testWizard.attack(Spell.testSpell, Enemy.enemies.get(0));
//        System.out.println(testWizard.getWizardStatsPercent());
    }
}