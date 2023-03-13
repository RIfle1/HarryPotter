package Enums;

import lombok.Getter;

@Getter
public enum Difficulty {
    EASY((-Math.log(6.5) + 10) / 10),
    NORMAL((-2 * Math.log(3) + 10) / 10),
    HARD((-Math.log(14) + 10) / 10),
    EXTREME((-Math.log(19) + 10) / 10),
    DEATH_WISH((-Math.log(29) + 10) / 10);

    private final double difficultyMultiplier;

    Difficulty(double multiplier) {
        this.difficultyMultiplier = multiplier;
    }
}
