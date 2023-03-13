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

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
//        gameCredits();
//        checkSaves();
//        CharacterCreation.characterInit();

        List<Enemy> enemies = Enemy.generateEnemies(10, 10, 1, EnemyName.BASILISK, Difficulty.DEATH_WISH);

        Wizard.testWizard.updateSpells();
        Wizard.testWizard.attack(Spell.avadaKedavra, enemies.get(0));

    }
}