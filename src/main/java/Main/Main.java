package Main;

import static Classes.BattleArena.battleArena;
import static Classes.Enemy.generateRandomBasicEnemy;
import static Classes.Wizard.testWizard;
import static Main.ConsoleFunctions.*;
import static Main.SaveChecker.checkSaves;

public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {
        gameCredits();
        checkSaves();
//        CharacterCreation.characterInit();
        chooseLevel();
//        System.out.println(generateRandomBasicEnemy());
//        enemiesHashMap = generateEnemies(10, 10, 1, EnemyName.DARK_WIZARD, wizard.getDifficulty());
//
//        System.out.println(enemiesHashMap);
//        System.out.println(enemiesKeyList);



//        wizard.updateStats();
//        testWizard.updateStats();
//        testWizard.printStats();
//        wizard.attack(wizard.getSpell(Spell.testSpell), enemies.get("Dark Wizard-1"));


    }
}