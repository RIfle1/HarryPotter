package Main;

import AbstractClasses.AbstractCharacter;
import AbstractClasses.AbstractItem;
import Classes.Enemy;
import Classes.Potion;
import Classes.Spell;
import Classes.Wizard;
import Enums.Difficulty;
import Enums.EnemyName;
import Enums.EnemyType;
import com.sun.nio.file.SensitivityWatchEventModifier;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static Main.ConsoleFunctions.continuePrompt;
import static Main.ConsoleFunctions.gameCredits;
import static Main.SaveChecker.checkSaves;

public class Main {
    public static void main(String[] args) {
//        gameCredits();
//
//        checkSaves();
//        CharacterCreation.characterInit();

        Enemy.enemies = Enemy.generateEnemies(10, 10, 1, EnemyName.BASILISK, Wizard.testWizard.getDifficulty());

        Wizard.testWizard.updateSpells();
        Wizard.testWizard.attack(Spell.testSpell, Enemy.enemies.get(0));
        Wizard.testWizard.attack(Spell.testSpell, Enemy.enemies.get(0));
    }
}