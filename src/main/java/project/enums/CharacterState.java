package project.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;

import static project.enums.EnumMethods.returnFormattedEnum;

@Getter
public enum CharacterState {
    STANDING(0),
    LEVITATING(0.1),
    KNOCKED(0.15),
    STUNNED(0.2),
    FROZEN(0.5),
    ON_FIRE(0.4),
    CURSED(0.7),
    BLEEDING(0.3),
    INVINCIBLE(0);

    private final double damageMultiplier;

    CharacterState(double damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    public static List<String> getCharacterStateList() {
        CharacterState[] characterStateValues = CharacterState.values();
        return EnumMethods.getEnumList(characterStateValues);
    }

    public static CharacterState setCharacterState(String characterState) {
        HashMap<String, CharacterState> characterStateHashMap = new HashMap<>();
        CharacterState[] characterStateValues = CharacterState.values();

        for(CharacterState characterStateValue:characterStateValues) {
            characterStateHashMap.put(returnFormattedEnum(characterStateValue), characterStateValue);
        }
        return characterStateHashMap.get(characterState);
    }

}
