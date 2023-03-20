package Enums;

import AbstractClasses.AbstractCharacter;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;

import static Enums.EnumMethods.returnFormattedEnum;

@Getter
public enum Difficulty {
    EASY(setEnemyMultiplier(7.5), setWizardMultiplier(7.5)),
    NORMAL(setEnemyMultiplier(10), setWizardMultiplier(10)),
    HARD(setEnemyMultiplier(15), setWizardMultiplier(15)),
    EXTREME(setEnemyMultiplier(20), setWizardMultiplier(20)),
    DEATH_WISH(setEnemyMultiplier(30), setWizardMultiplier(30));

    private final double enemyDiffMultiplier;
    private final double wizardDiffMultiplier;

    Difficulty(double enemyDiffMultiplier, double wizardDiffMultiplier) {
        this.enemyDiffMultiplier = enemyDiffMultiplier;
        this.wizardDiffMultiplier = wizardDiffMultiplier;
    }

    private static double setEnemyMultiplier(double value) {
        return Math.log(value) / AbstractCharacter.maxLevel;
    }

    private static double setWizardMultiplier(double value) {
        return Math.log(value / AbstractCharacter.difficultyDifference) / AbstractCharacter.maxLevel;
    }

    public static List<String> getDifficultyList() {
        Difficulty[] difficultyValues = Difficulty.values();
        return EnumMethods.getEnumList(difficultyValues);
    }

    public static Difficulty setDifficulty(String difficulty) {
        HashMap<String, Difficulty> difficultyHashMap = new HashMap<>();
        Difficulty[] difficultyValues = Difficulty.values();

        for(Difficulty difficultyValue:difficultyValues) {
            difficultyHashMap.put(returnFormattedEnum(difficultyValue), difficultyValue);
        }
        return difficultyHashMap.get(difficulty);
    }
}
