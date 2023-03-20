package Main;

import Classes.Enemy;
import Classes.Spell;
import Enums.EnemyName;

import static Classes.BattleArena.battleArena;
import static Classes.Enemy.*;
import static Classes.Wizard.wizard;
import static Main.ConsoleFunctions.*;
import static Main.SaveChecker.checkSaves;

public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {
//        gameCredits();
//        checkSaves();
//        CharacterCreation.characterInit();

        String[] optionsList = {
                "The Philosopher's Stone",
                "The Chamber of Secrets",
                "The Prisoner of Azkaban",
                "The Goblet of Fire",
                "The Order of the Phoenix",
                "The Half-Blooded Prince",
                "The Deathly Hallows",
                "Battle Arena",
        };

        printHeader("Choose your level: ");
        printChoices(optionsList);

        switch (returnChoiceInt()) {
            case 1, 2, 3, 4, 5, 6, 7, 8 -> battleArena();
        }
        ;

//        enemiesHashMap = generateEnemies(10, 10, 1, EnemyName.DARK_WIZARD, wizard.getDifficulty());
//
//        System.out.println(enemiesHashMap);
//        System.out.println(enemiesKeyList);



//        wizard.updateStats();
//        wizard.printStats();
//        wizard.attack(wizard.getSpell(Spell.testSpell), enemies.get("Dark Wizard-1"));


    }
}