package project.abstractClasses;

import lombok.Getter;
import lombok.Setter;
import project.classes.Enemy;
import project.classes.Potion;
import project.classes.Spell;
import project.classes.Wizard;
import project.enums.*;
import project.javafx.controllers.GameSceneController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.max;
import static project.classes.Color.*;
import static project.classes.Enemy.getEnemyNameMaxLength;
import static project.classes.Spell.*;
import static project.classes.Wizard.wizard;
import static project.enums.EnumMethods.returnFormattedEnum;
import static project.functions.ConsoleFunctions.printColumnSeparator;
import static project.functions.ConsoleFunctions.printTitle;
import static project.functions.GeneralFunctions.generateDoubleBetween;

@Getter
@Setter
public abstract class AbstractCharacter {
    public AbstractCharacter(String name, double healthPoints, double defensePoints, double maxHealthPoints, double maxDefensePoints, Difficulty difficulty, CharacterState characterState, List<Potion> potionList, List<Potion> activePotionsList, HashMap<String, Spell> spellsHashMap, List<String> spellsKeyList, double level) {
        this.name = name;
        this.healthPoints = healthPoints;
        this.defensePoints = defensePoints;
        this.maxHealthPoints = maxHealthPoints;
        this.maxDefensePoints = maxDefensePoints;
        this.difficulty = difficulty;
        this.characterState = characterState;
        this.potionList = potionList;
        this.activePotionsList = activePotionsList;
        this.spellsHashMap = spellsHashMap;
        this.spellsKeyList = spellsKeyList;
        this.level = level;
    }

    private String name;
    private double healthPoints;
    private double defensePoints;
    private double maxHealthPoints;
    private double maxDefensePoints;
    private Difficulty difficulty;
    private CharacterState characterState;
    private List<Potion> potionList;
    private List<Potion> activePotionsList;
    private HashMap<String, Spell> spellsHashMap;
    private List<String> spellsKeyList;
    private double level;

    public final static int maxLevel = 10;

    // DIFFICULTY DIFFERENCE BETWEEN PLAYER AND ENEMY
    // =1 SAME STATS AS ENEMY
    // >1 ENEMIES HAVE BETTER STATS THAN THE PLAYER
    // <1 PLAYER HAS BETTER STATS THAN THE ENEMIES
    public final static double difficultyDifference = 1.3;

    private final static int wizardParryDivider = 2;
    private final static int enemyParryDivider = 1;
    private final static int enemyDodgeDivider = 5;
    private final static int wizardDodgeDivider = 3;
    private final static double enemyDamageDivider = 1.7;
    private final static double wizardDamageDivider = 1;

    public String returnStatBar(String statLogo, String statLogoColor, double value, double maxValue) {
        final int statBarLength = 30;

        int stat = (int) ((value / maxValue) * statBarLength);
        int empty = statBarLength - stat;

        return returnColoredText(statLogo, statLogoColor) +
                returnColoredText("[", ANSI_CYAN) + returnColoredText("|", ANSI_GREEN).repeat(max(0, stat)) +
                returnColoredText("-", ANSI_RED).repeat(max(0, empty)) +
                returnColoredText("]", ANSI_CYAN) +
                returnColoredText(" " + (int) value + "/" + (int) maxValue, ANSI_RED);
    }

    public String returnStringStats(int extraNameLength) {
        String name;
        String color;
        int nameLength = max(getEnemyNameMaxLength(), wizard.getName().length()) + extraNameLength;

        if (this.getClass() == Wizard.class) {
            name = this.getName();
            color = ANSI_PURPLE;
        } else {
            name = returnFormattedEnum(((Enemy) this).getEnemyName());
            color = ANSI_RED;
        }

        String column1Format = "%-" + (nameLength + 1) + "s";
        String column2Format = "%-" + 10 + "s";
        String column3Format = "%-" + 351 + "s";
        String column4Format = "%-" + 12 + "s";
        String column5Format = "%-" + 12 + "s";


        String column1 = returnColoredText(String.format(column1Format, name), color);
        String column2 = returnColoredText(String.format(column2Format, "Level " + (int) this.getLevel()), ANSI_YELLOW);
        String column3 = String.format(column3Format, returnStatBar("❤", ANSI_RED, this.getHealthPoints(), this.getMaxHealthPoints()));
        String column4 = returnColoredText(String.format(column4Format, (int) this.getDefensePoints() + " Defense"), ANSI_BLUE);
        String column5 = "";

        if(this.getCharacterState() != CharacterState.STANDING) {
            column5 = printColumnSeparator("<>") +
                    returnColoredText(String.format(column5Format, (int) (this.getCharacterState().getDamageMultiplier() * 100) + "% More Damage."), ANSI_RED);
        }

        return column1 + printColumnSeparator("<>") +
                column2 + printColumnSeparator("<>") +
                column3 + printColumnSeparator("<>") +
                column4 +
                column5;
    }

    public List<String> returnItemNameList() {
        List<Potion> currentItemList = this.getPotionList();
        List<String> currentItemNameList = new ArrayList<>();
        for (AbstractItem currentItem : currentItemList) {
            currentItemNameList.add(currentItem.getItemName());
        }

        return currentItemNameList;
    }

    public Potion returnPotion(String potionName) {
        return (Potion) returnPotionsList().stream()
                .filter(AbstractItem -> AbstractItem.getItemName().equals(potionName))
                .findFirst()
                .orElse(null);
    }

    public List<Potion> returnPotionsList() {
            return this.getPotionList().stream().filter(AbstractItem -> AbstractItem.getClass() == Potion.class).toList();
    }

    public List<String> returnPotionNamesList() {
        List<String> potionNamesList = new ArrayList<>();
        this.returnPotionsList().forEach(AbstractItem -> potionNamesList.add(AbstractItem.getItemName()));
        return potionNamesList;
    }

    public List<String> returnActivePotionNamesList() {
        List<String> activePotionNamesList = new ArrayList<>();
        this.getActivePotionsList().forEach(AbstractItem -> activePotionNamesList.add(AbstractItem.getItemName()));
        return activePotionNamesList;
    }

    public void applyPotion(Potion potion) {
        List<Potion> activePotionsList = this.getActivePotionsList();

        // TODO - DO SOMETHING ABOUT INVINCIBILITY POTION

        if(potion.getPotionType() == PotionType.DEFENSE) {
            this.setDefensePoints(this.getDefensePoints() + potion.getPotionValue());
            activePotionsList.add(potion);
        }
        else if(potion.getPotionType() == PotionType.DAMAGE) {
            activePotionsList.add(potion);
        }
        else if(potion.getPotionType() == PotionType.REGENERATION) {
            activePotionsList.add(potion);
        }
        else if(potion.getPotionType() == PotionType.HEALTH) {
            double newHealth = this.getHealthPoints() + potion.getPotionValue();
            this.setHealthPoints(Math.min(newHealth, this.getMaxHealthPoints()));
        }
        else if(potion.getPotionType() == PotionType.COOLDOWN) {
            this.reduceSpellsCooldown((int) potion.getPotionValue());
        }

        deletePotion(potion);
    }

    private void deletePotion(Potion potion) {
        if(potion != null) {
            List<Potion> newList = this.getPotionList().stream().filter(pot -> !pot.toString().equals(potion.toString())).toList();
            this.setPotionList(newList);
        }
    }

    public void applyPotionEffect() {

        List<Potion> activePotionsList = this.getActivePotionsList();
        Potion potionToRemove = null;

        for(Potion potion:activePotionsList) {
            if(potion.getPotionType() == PotionType.DEFENSE && potion.getPotionDuration() == 0) {
                this.setDefensePoints(this.getDefensePoints() - potion.getPotionValue());
                potionToRemove = potion;
            }
            else if(potion.getPotionType() == PotionType.DAMAGE && potion.getPotionDuration() == 0) {
                potionToRemove = potion;
            }
            else if(potion.getPotionType() == PotionType.REGENERATION) {
                double newHealth = this.getHealthPoints() + potion.getPotionValue();
                this.setHealthPoints(Math.min(newHealth, this.getMaxHealthPoints()));
                if(potion.getPotionDuration() == 0) {
                    potionToRemove = potion;
                }
            }
            potion.setPotionDuration(potion.getPotionDuration() - 1);
        }
        deletePotion(potionToRemove);
    }



    public void drinkPotion(Potion potion) {
        final int maxPotionActiveAtOnce = 3;
        final int maxPotionOfOneType = 1;
        String text ="";

        List<Potion> currentItemList = this.getPotionList();
        boolean potionIsInInventory = currentItemList.stream().anyMatch(currentItem -> currentItem.equals(potion));

        List<Potion> activePotionList = this.getActivePotionsList();
        List<Potion> potionTypeActiveList = activePotionList
                .stream()
                .filter(activePotion -> activePotion.getPotionType().equals(potion.getPotionType()))
                .toList();


        // NEEDS TO BE OPTIMIZED FOR ALL TYPES OF POTIONS
        if (potionIsInInventory && potionTypeActiveList.toArray().length < maxPotionOfOneType && activePotionList.toArray().length < maxPotionActiveAtOnce) {
            // APPLY THE POTION'S VALUE IF IT'S HEALTH, REGENERATION OR DEFENSE
            this.applyPotion(potion);

            String subText;
            double potionValue = potion.getPotionValue();

            if(potionValue > 1) {
                subText = (int) potionValue + " points";
            }
            else {
                subText = ((int) (potionValue * 100)) + "%";
            }

            text = returnColoredText("You just drank " + potion.getItemName(), potion.getItemColor()) +
                    "\nYour " +
                    returnColoredText(returnFormattedEnum(potion.getPotionType()), potion.getItemColor()) +
                    " has been improved by " +
                    returnColoredText(subText, potion.getItemColor());

        } else if (potionTypeActiveList.toArray().length >= maxPotionOfOneType) {
            text = returnColoredText(returnFormattedEnum(potion.getPotionType()), potion.getItemColor()) +
                    returnColoredText(" potion is already active.", ANSI_YELLOW);
        } else if (activePotionList.toArray().length >= 3) {
            text = returnColoredText("Cannot drink ", ANSI_RED) +
                    returnColoredText(returnFormattedEnum(potion.getPotionType()), potion.getItemColor()) +
                    returnColoredText(" potion, you can only have 3 potions active at once.", ANSI_RED);
        }

        // PRINT IN CONSOLE
        printTitle(text);

        // PRINT IN GUI CONSOLE
        GameSceneController.updateConsoleTaStatic(text, true);
    }

    public Spell returnTypedSpellsFromInt(MoveType spellType, int number) {
        return this.returnAllSpells().stream().filter(spell -> spell.getSpellType().equals(spellType)).toList().get(number);
    }

    public void putSpellsHashMap(Spell spell) {
        this.spellsHashMap.put(spell.getSpellName(), spell);
    }

    public void updateSpellsKeyList(HashMap<String, Spell> spellsHashMap) {
        this.spellsKeyList = new ArrayList<>();
        spellsHashMap.forEach((key, value) -> this.spellsKeyList.add(key));
    }

    public void updateSpellsHashMap() {
        this.spellsHashMap = new HashMap<>();
        for (Spell spell : Spell.returnAllSpells()) {
            if (spell.getSpellLevelRequirement() >= 0 && spell.getSpellLevelRequirement() <= this.getLevel() && !this.getSpellsHashMap().containsKey(spell.getSpellName())) {
                try {
                    this.putSpellsHashMap(spell.clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }
        updateSpellsKeyList(this.getSpellsHashMap());
    }

    public double returnActivePotionValueSum(PotionType potionType) {
        List<Potion> activePotionList = this.activePotionsList;

        return activePotionList
                .stream()
                .filter(potion -> potion.getPotionType() == potionType)
                .map(Potion::getPotionValue)
                .reduce((double) 0, Double::sum);
    }

    public double returnAttackBaseDamage(double attackDamage, double divider) {
        return (Math.exp(this.getLevel() * getDifficulty().getWizardDiffMultiplier()) * attackDamage) / divider;
    }

    public double returnSpellDamage(Spell spell, AbstractCharacter attackedCharacter) {
        double[] spellDamage = spell.getSpellDamage();
        double spellRandomDamage;
        double spellBaseDamage = 0;

        double houseDamagePercent = 0;
        double housePotionPercent = 0;
        double strengthPercent = 0;

        if (spellDamage[0] != spellDamage[1]) {
            spellRandomDamage = generateDoubleBetween(spellDamage[0], spellDamage[1]);
        } else {
            spellRandomDamage = spellDamage[0];
        }

        // CHANGE SPELL BASE DAMAGE DEPENDING ON ENEMY OR PLAYER
        if (this.getClass() == Wizard.class) {
            // UPDATE WIZARD DAMAGE OR POTION EFFICIENCY BASED ON WHICH HOUSE THEY BELONG TO
            HouseName houseName = ((Wizard) this).getHouseName();
            if (houseName == HouseName.SLYTHERIN) {
                houseDamagePercent = houseName.getSpecValue();
            } else if (houseName == HouseName.HUFFLEPUFF) {
                housePotionPercent = houseName.getSpecValue();
            }
            strengthPercent = ((Wizard) this).returnWizardSpecsPercent().get("strength");

            spellBaseDamage = returnAttackBaseDamage(spellRandomDamage, wizardDamageDivider);
        } else if (this.getClass() == Enemy.class) {
            spellBaseDamage = returnAttackBaseDamage(spellRandomDamage, enemyDamageDivider);
        }

        // DAMAGE POTION ALREADY IMPLEMENTED HERE
        double damagePotionPercentIncrease = returnActivePotionValueSum(PotionType.DAMAGE) * (1 + housePotionPercent);
        double characterStateDamageMultiplier = attackedCharacter.getCharacterState().getDamageMultiplier();

        return spellBaseDamage
                * (1 + damagePotionPercentIncrease)
                * (1 + characterStateDamageMultiplier)
                * (1 + houseDamagePercent)
                * (1 + strengthPercent);
    }

    public double returnMeleeDamage(AbstractCharacter attackedCharacter) {
        double characterStateDamageMultiplier = attackedCharacter.getCharacterState().getDamageMultiplier();
        return returnAttackBaseDamage(((Enemy) this).getEnemyName().getEnemyCombat().getCombatDamage(), enemyDamageDivider) * (1 + characterStateDamageMultiplier);
    }

    public double takeDamage(double calculatedDamage) {
        this.healthPoints -= calculatedDamage;
        if(this.healthPoints < 0) {
            this.healthPoints = 0;
        }
        return calculatedDamage;
    }

    public boolean checkSpellReady(Spell spell) {
        return spell.getSpellReadyIn() == 0;
    }

    public void resetSpellReadyIn(Spell spell) {
        spell.setSpellReadyIn(spell.getSpellCooldown());
    }

    public List<Spell> returnAllSpells() {
        HashMap<String, Spell> characterSpellHashMap = this.getSpellsHashMap();
        List<Spell> characterSpellList = new ArrayList<>();

        characterSpellHashMap.forEach((key, value) -> characterSpellList.add(value));
        return characterSpellList;
    }

    public List<String> returnAllSpellNames() {
        HashMap<String, Spell> characterSpellHashMap = this.getSpellsHashMap();
        List<String> allSpellNames = new ArrayList<>();

        characterSpellHashMap.forEach((key, value) -> allSpellNames.add(value.getSpellName()));
        return allSpellNames;
    }

    public void printAllCharacterSpells() {
        int index = 1;
        for (Map.Entry<String, Spell> spell : this.getSpellsHashMap().entrySet()) {
            System.out.printf("%-15s", "("+returnColoredText(String.valueOf(index), ANSI_YELLOW)+")");
            System.out.println(spell.getValue().printStats());
            index++;
        }

    }

    public List<Spell> returnTypedSpellsList(MoveType spellType) {
        return this.returnAllSpells().stream().filter(spell -> spell.getSpellType().equals(spellType)).toList();
    }


    public void printTypedSpells(MoveType spellType) {
        int index = 1;
        List<Spell> typedSpellsList = returnTypedSpellsList(spellType);

        for (Spell spell : typedSpellsList) {
            System.out.printf("%-15s", "("+returnColoredText(String.valueOf(index), ANSI_YELLOW)+")");
            System.out.println(spell.printStats());
            index++;
        }
    }

    public void reduceSpellsCooldown(int cooldown) {
        HashMap<String, Spell> spellsHashMap = this.getSpellsHashMap();

        spellsHashMap.forEach((key, spell) -> {
            int newValue = spell.getSpellReadyIn() - cooldown;
            if (newValue < 0) {
                newValue = 0;
            }
            spell.setSpellReadyIn(newValue);
        });
    }

    public void reduceSpellsCooldown() {
        HashMap<String, Spell> characterSpellHashMap = this.getSpellsHashMap();
        characterSpellHashMap.forEach((key, value) -> {
            value.setSpellReadyIn(0);
        });
    }

    public double defenseReductionRatio() {
        return 1 - (this.maxDefensePoints / this.maxHealthPoints) * maxDefenseReductionPercent;
    }

    public double returnSpellCalculatedDamage(Spell spell, AbstractCharacter attackedCharacter) {
        // CALCULATIONS FOR SPELL DAMAGE
        return returnSpellDamage(spell, attackedCharacter) * defenseReductionRatio();
    }

    public double returnMeleeCalculatedDamage(AbstractCharacter attackedCharacter) {
        // CALCULATIONS FOR MELEE DAMAGE
        return returnMeleeDamage(attackedCharacter) * defenseReductionRatio();
    }

    public double returnSpellChance(Spell spell) {
        double luckPercent = 0;
        if (this.getClass() == Wizard.class) {
            luckPercent = ((Wizard) this).returnWizardSpecsPercent().get("luck");
        }
        return spell.getSpellChance() * (1 + luckPercent);
    }

    public boolean checkIfDead() {
        return !(this.healthPoints > 0);
    }

    public void castAttack(boolean attackSucceeded, CharacterState characterState, AbstractCharacter attackingCharacter, double calculatedDamage, String spellSpecialAttackLine) {
        String attackedCharacterName = "";
        String attackingCharacterName = "";

        if (this.getClass() == Enemy.class) {
            attackingCharacterName = returnFormattedEnum(((Enemy) this).getEnemyName());
            attackedCharacterName = attackingCharacter.getName();
        } else if (this.getClass() == Wizard.class) {
            attackingCharacterName = this.getName();
            attackedCharacterName = returnFormattedEnum(((Enemy) attackingCharacter).getEnemyName());
        }

        if (attackSucceeded) {
            // TAKE DAMAGE
            double damageTaken = attackingCharacter.takeDamage(calculatedDamage);

            // CHANGE CHARACTER STATE
            attackingCharacter.setCharacterState(characterState);

            // PRINT AFTER CAST STUFF INTO CONSOLE
            double healthPoints = attackingCharacter.getHealthPoints();
            double maxHealthPoints = attackingCharacter.getMaxHealthPoints();

            if(this.getClass() == Wizard.class) {
                GameSceneController.updateConsoleTaStatic(spellSpecialAttackLine, false);
                System.out.println(spellSpecialAttackLine);
            }

            attackAfterCast(attackingCharacter, attackedCharacterName, attackingCharacterName, (int) damageTaken, healthPoints, maxHealthPoints);

            // CHECK ENEMY'S CHARACTER AND ACT ACCORDINGLY (ADD XP AND ITEM DROPS)
            if (attackingCharacter.getClass() == Enemy.class) {
                ((Enemy) attackingCharacter).checkHealth();
            }

        } else {
            String text = attackingCharacterName + returnColoredText(" missed ", ANSI_YELLOW) + "their attack!";
            System.out.println(text);
            GameSceneController.updateConsoleTaStatic(text, true);

        }
    }

    private static void attackAfterCast(AbstractCharacter attackedCharacter, String attackedCharacterName, String attackingCharacterName, int damageTaken, double healthPoints, double maxHealthPoints) {
        String text1 = attackingCharacterName + " hit " + attackedCharacterName + " with " + returnColoredText(damageTaken + " damage", ANSI_RED) + "!";
        String text2 = attackedCharacterName + " is now " + returnColoredText(returnFormattedEnum(attackedCharacter.getCharacterState()), ANSI_BLUE) + ".";
        String text3 = attackedCharacterName + " has " + returnColoredText((int) healthPoints + "/" + (int) maxHealthPoints + " hp", ANSI_GREEN) + " left.";
        StringBuilder text4 = new StringBuilder();

        if (attackedCharacter.getClass() == Wizard.class) {
            text4.append(attackingCharacterName).append(" killed you.");
        } else if (attackedCharacter.getClass() == Enemy.class) {
            text4.append("You killed ")
                    .append(returnColoredText(attackedCharacterName, ANSI_PURPLE))
                    .append(".\nYou received ")
                    .append(returnColoredText((int) ((Enemy) attackedCharacter).getExperiencePoints() + " XP.", ANSI_BLUE));

            List<Potion> enemyItemList = attackedCharacter.getPotionList();

            if(enemyItemList.size() > 0) {
                text4.append("\nYou found:\n");
                enemyItemList.forEach(Item -> text4.append("- ").append(returnColoredText(Item.getItemName(), Item.getItemColor())).append("\n"));
                text4.replace(text4.length() - 1, text4.length(), "");
            }
        }
        // PRINT IN CONSOLE
        System.out.println(text1);
        // PRINT IN GUI
        GameSceneController.updateConsoleTaStatic(text1, false);

        if (healthPoints > 0) {
            if (attackedCharacter.getCharacterState() != CharacterState.STANDING) {
                // PRINT IN CONSOLE
                System.out.println(text2);
                // PRINT IN GUI
                GameSceneController.updateConsoleTaStatic(text2, false);
            }
            // PRINT IN CONSOLE
            System.out.println(text3);
            // PRINT IN GUI
            GameSceneController.updateConsoleTaStatic(text3, true);
        } else {
            System.out.println(text4);
            GameSceneController.updateConsoleTaStatic(text4.toString(), true);
        }
    }

    public boolean dodge(boolean dodgeSuccess, AbstractCharacter attackingCharacter) {
        String text1 = "";
        String text2 = "";

        if (this.getClass() == Wizard.class) {
            text1 = returnColoredText("You dodged " + returnFormattedEnum(((Enemy) attackingCharacter).getEnemyName()) + "'s attack.", ANSI_YELLOW);
            text2 = returnColoredText("You were unable to dodge " + returnFormattedEnum(((Enemy) attackingCharacter).getEnemyName()) + "'s attack.", ANSI_RED);
        } else if (this.getClass() == Enemy.class) {
            text1 = returnColoredText(returnFormattedEnum(((Enemy) this).getEnemyName()) + " dodged your attack.", ANSI_YELLOW);
            text2 = returnColoredText(returnFormattedEnum(((Enemy) this).getEnemyName()) + " was unable to dodge your attack.", ANSI_RED);
        }

        if (dodgeSuccess) {
            System.out.println(text1);
            GameSceneController.updateConsoleTaStatic(text1, true);
            return true;
        } else {
            System.out.println(text2);
            GameSceneController.updateConsoleTaStatic(text2, false);
            return false;
        }

    }

    public boolean parry(boolean parrySuccess, String parriedAttackName, AbstractCharacter attackingCharacter, double calculatedDamage) {
        String text1 = "";
        String text2 = "";

        double newDamage = calculatedDamage * (1 + parryMultiplier);

        if (this.getClass() == Wizard.class) {
            text1 = returnColoredText("You parried " + parriedAttackName + ".", ANSI_YELLOW);
            text2 = returnColoredText("You were unable to parry " + returnFormattedEnum(((Enemy) attackingCharacter).getEnemyName()) + "'s attack.", ANSI_RED);
        } else if (this.getClass() == Enemy.class) {
            text1 = returnColoredText(returnFormattedEnum(((Enemy) this).getEnemyName()) + " parried your attack and returned ", ANSI_YELLOW)
                    + returnColoredText((int) newDamage + " Damage.", ANSI_RED);
            text2 = returnColoredText(returnFormattedEnum(((Enemy) this).getEnemyName()) + " was unable to parry your attack.", ANSI_RED);
        }

        // THE ATTACK CAN BE PARRIED ONLY IF THE ATTACKED CHARACTER'S DEFENSE IS HIGHER THAN THE ATTACKING CHARACTER'S CALCULATED SPELL DAMAGE
        if (parrySuccess) {
            System.out.println(text1);
            GameSceneController.updateConsoleTaStatic(text1, false);

//            this.spellAttack(attackSucceeded, stupefy, attackingCharacter, newDamage);
            String spellSpecialAttackLine = returnColoredText(stupefy.getSpellSpecialAttackLine(), ANSI_PURPLE);
            this.castAttack(true, stupefy.getCharacterState(), attackingCharacter, calculatedDamage, spellSpecialAttackLine);

            return true;
        } else {
            System.out.println(text2);
            GameSceneController.updateConsoleTaStatic(text2, false);

            return false;
        }
    }

    public double returnDodgeChance(AbstractCharacter attackingCharacter, double dodgeChance) {

        if (this.getClass() == Wizard.class) {
            if(((Enemy) attackingCharacter).getEnemyName().getEnemyCombat() == EnemyCombat.MELEE) {
                dodgeChance = ((Enemy) attackingCharacter).getEnemyName().getEnemyCombat().getCombatChance();
            }

            double charismaPercent =  ((Wizard) this).returnWizardSpecsPercent().get("charisma");
            dodgeChance = (dodgeChance / wizardDodgeDivider) * (1 + charismaPercent);

        } else if (this.getClass() == Enemy.class) {
            dodgeChance = dodgeChance / enemyDodgeDivider;
        }

        return dodgeChance;
    }

    public double returnParryChance() {
        // THE ATTACK CAN BE PARRIED ONLY IF THE ATTACKED CHARACTER'S DEFENSE IS HIGHER THAN THE ATTACKING CHARACTER'S CALCULATED SPELL DAMAGE
        double defensePoints = this.getDefensePoints();

        if (this.getClass() == Wizard.class) {
            double intelligencePercent =  ((Wizard) this).returnWizardSpecsPercent().get("intelligence");
            defensePoints = (defensePoints / wizardParryDivider) * (1 + intelligencePercent);
        } else if (this.getClass() == Enemy.class) {
            defensePoints = defensePoints / enemyParryDivider;
        }

        return defensePoints;
    }
}
