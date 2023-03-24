package Main;

import AbstractClasses.AbstractItem;
import Classes.Potion;

import java.util.ArrayList;
import java.util.Arrays;

import static Classes.BattleArena.battleArena;
import static Classes.Enemy.generateRandomBasicEnemy;
import static Classes.Wizard.testWizard;
import static Classes.Wizard.wizard;
import static Main.ConsoleFunctions.*;
import static Main.SaveChecker.checkSaves;

public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {
//        gameCredits();
//        checkSaves();
//        CharacterCreation.characterInit();
        wizard.setItemList(new ArrayList<>(Arrays.asList(Potion.minorDefensePotion, Potion.highDefensePotion, Potion.highHealthPotion)));
        chooseLevel();
//        wizard.usePotion();
//        wizard.applyPotionEffect();
//        wizard.printStats();
//        System.out.println(generateRandomBasicEnemy());
//        enemiesHashMap = generateEnemies(10, 10, 1, EnemyName.DARK_WIZARD, wizard.getDifficulty());
//
//        System.out.println(enemiesHashMap);
//        System.out.println(enemiesKeyList);



//        wizard.updateStats();
//        testWizard.updateStats();
//        testWizard.printStats();
//        wizard.attack(wizard.getSpell(Spell.testSpell), enemies.get("Dark Wizard-1"));

//        testWizard.updateStats();
//        System.out.println(testWizard.getLevel());

    }
}