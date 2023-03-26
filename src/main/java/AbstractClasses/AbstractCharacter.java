package AbstractClasses;

import Classes.*;
import Enums.*;
import lombok.*;

import java.util.*;

import static Classes.Color.*;
import static Classes.Spell.*;
import static Enums.EnumMethods.returnFormattedEnum;
import static Main.ConsoleFunctions.*;
import static Main.MechanicsFunctions.generateDoubleBetween;

@Getter
@Setter
public abstract class AbstractCharacter {
    public AbstractCharacter(String name, double healthPoints, double defensePoints, double maxHealthPoints, double maxDefensePoints, Difficulty difficulty, CharacterState characterState, List<AbstractItem> itemList, List<Potion> activePotionsList, HashMap<String, Spell> spellsHashMap, List<String> spellsKeyList, double level) {
        this.name = name;
        this.healthPoints = healthPoints;
        this.defensePoints = defensePoints;
        this.maxHealthPoints = maxHealthPoints;
        this.maxDefensePoints = maxDefensePoints;
        this.difficulty = difficulty;
        this.characterState = characterState;
        this.itemList = itemList;
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
    private List<AbstractItem> itemList;
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
    private final static int enemyParryDivider = 3;
    private final static int enemyDodgeDivider = 5;
    private final static int wizardDodgeDivider = 3;
    private final static double enemyDamageDivider = 1.7;
    private final static double wizardDamageDivider = 1;

    public String getStatBar(String statLogo, String statLogoColor, double value, double maxValue) {
        final int statBarLength = 30;

        int stat = (int) ((value / maxValue) * statBarLength);
        int empty = statBarLength - stat;

        return returnColoredText(statLogo, statLogoColor) +
                returnColoredText("[", Color.ANSI_CYAN) + returnColoredText("|", Color.ANSI_GREEN).repeat(Math.max(0, stat)) +
                returnColoredText("-", Color.ANSI_RED).repeat(Math.max(0, empty)) +
                returnColoredText("]", Color.ANSI_CYAN) +
                returnColoredText(" " + (int) value + "/" + (int) maxValue, ANSI_RED);
    }

    public String returnStringStats() {
        String name;
        int nameLength;

        if (this.getClass() == Wizard.class) {
            name = this.getName();
            nameLength = this.getName().length();
        } else {
            name = returnFormattedEnum(((Enemy) this).getEnemyName());
            nameLength = getSpellNameMaxLength();
        }

        String column1Format = "%-" + (nameLength + 1) + "s";
        String column2Format = "%-" + 10 + "s";
        String column3Format = "%-" + 20 + "s";
        String column4Format = "%-" + 12 + "s";
        String column5Format = "%-" + 12 + "s";


        String column1 = returnColoredText(String.format(column1Format, name), Color.ANSI_PURPLE);
        String column2 = returnColoredText(String.format(column2Format, "Level " + (int) this.getLevel()), Color.ANSI_YELLOW);
        String column3 = String.format(column3Format, getStatBar("❤", Color.ANSI_RED, this.getHealthPoints(), this.getMaxHealthPoints()));
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

    public List<String> getItemNameList() {
        List<AbstractItem> currentItemList = this.getItemList();
        List<String> currentItemNameList = new ArrayList<>();
        for (AbstractItem currentItem : currentItemList) {
            currentItemNameList.add(currentItem.getItemName());
        }

        return currentItemNameList;
    }

    public Potion getPotion(String potionName) {
        return (Potion) getPotionsList().stream()
                .filter(AbstractItem -> AbstractItem.getItemName().equals(potionName))
                .findFirst()
                .orElse(null);
    }

    public List<AbstractItem> getPotionsList() {
            return this.getItemList().stream().filter(AbstractItem -> AbstractItem.getClass() == Potion.class).toList();
    }

    public List<String> getPotionNamesList() {
        List<String> potionNamesList = new ArrayList<>();
        this.getPotionsList().forEach(AbstractItem -> potionNamesList.add(AbstractItem.getItemName()));
        return potionNamesList;
    }

    public List<String> getActivePotionNamesList() {
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

        this.getItemList().remove(potion);
    }

    public void applyPotionEffect() {
        // TODO - IDK CHECK THIS

        List<Potion> activePotionsList = this.getActivePotionsList();
        Potion potionToRemove = null;

        for(Potion potion:activePotionsList) {
            if(potion.getPotionType() == PotionType.DEFENSE && potion.getPotionDuration() == 0) {
                this.setDefensePoints(this.getDefensePoints() - potion.getPotionValue());
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
        activePotionsList.remove(potionToRemove);
    }



    public void drinkPotion(Potion potion) {
        final int maxPotionActiveAtOnce = 3;
        final int maxPotionOfOneType = 1;
        String text ="";

        List<AbstractItem> currentItemList = this.getItemList();
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

            text = returnColoredText("You just drank " + potion.getItemName(), potion.getItemColor()) +
                    "\nYour " +
                    returnColoredText(returnFormattedEnum(potion.getPotionType()), potion.getItemColor()) +
                    " has been improved by " +
                    returnColoredText(String.valueOf((int) potion.getPotionValue()), potion.getItemColor()) +
                    " points.";

        } else if (potionTypeActiveList.toArray().length >= maxPotionOfOneType) {
            text = returnColoredText(returnFormattedEnum(potion.getPotionType()), potion.getItemColor()) +
                    returnColoredText(" potion is already active.", ANSI_YELLOW);
        } else if (activePotionList.toArray().length >= 3) {
            text = returnColoredText("Cannot drink ", ANSI_RED) +
                    returnColoredText(returnFormattedEnum(potion.getPotionType()), potion.getItemColor()) +
                    returnColoredText(" potion, you can only have 3 potions active at once.", ANSI_RED);
        }

        printTitle(text);
        continuePromptExtra();
    }

    public Spell getSpellFromInt(int number) {
        return this.getSpellsHashMap().get(this.getSpellsKeyList().get(number));
    }

    public Spell getTypedSpellsFromInt(MoveType spellType, int number) {
        return this.getAllSpells().stream().filter(spell -> spell.getSpellType().equals(spellType)).toList().get(number);
    }

    public void putSpellsHashMap(Spell spell) {
        this.spellsHashMap.put(spell.getSpellName(), spell);
    }

    public void updateSpellsKeyList(HashMap<String, Spell> spellsHashMap) {
        spellsKeyList.clear();
        spellsHashMap.forEach((key, value) -> spellsKeyList.add(key));
    }

    public void updateSpellsHashMap() throws CloneNotSupportedException {
        for (Spell spell : Spell.getAllSpells()) {
            if (spell.getSpellLevelRequirement() >= 0 && spell.getSpellLevelRequirement() <= this.getLevel() && !this.getSpellsHashMap().containsKey(spell.getSpellName())) {
                this.putSpellsHashMap(spell.clone());
            }
        }
        updateSpellsKeyList(this.getSpellsHashMap());
    }

    public double getActivePotionValueSum(PotionType potionType) {
        List<Potion> activePotionList = this.activePotionsList;

        return activePotionList
                .stream()
                .filter(potion -> potion.getPotionType() == potionType)
                .map(Potion::getPotionValue)
                .reduce((double) 0, Double::sum);
    }

    public double getAttackBaseDamage(double attackDamage, double divider) {
        return (Math.exp(this.getLevel() * getDifficulty().getWizardDiffMultiplier()) * attackDamage) / divider;
    }

    public double getSpellDamage(Spell spell, AbstractCharacter attackedCharacter) {
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
            HouseName houseName = ((Wizard) this).getHouse().getHouseName();
            if (houseName == HouseName.SLYTHERIN) {
                houseDamagePercent = houseName.getSpecValue();
            } else if (houseName == HouseName.HUFFLEPUFF) {
                housePotionPercent = houseName.getSpecValue();
            }
            strengthPercent = ((Wizard) this).getWizardSpecsPercent().get("strength");

            spellBaseDamage = getAttackBaseDamage(spellRandomDamage, wizardDamageDivider);
        } else if (this.getClass() == Enemy.class) {
            spellBaseDamage = getAttackBaseDamage(spellRandomDamage, enemyDamageDivider);
        }

        // DAMAGE POTION ALREADY IMPLEMENTED HERE
        double damagePotionPercentIncrease = getActivePotionValueSum(PotionType.DAMAGE) * (1 + housePotionPercent);
        double characterStateDamageMultiplier = attackedCharacter.getCharacterState().getDamageMultiplier();

        return spellBaseDamage
                * (1 + damagePotionPercentIncrease)
                * (1 + characterStateDamageMultiplier)
                * (1 + houseDamagePercent)
                * (1 + strengthPercent);
    }

    public double getMeleeDamage(AbstractCharacter attackedCharacter) {
        double characterStateDamageMultiplier = attackedCharacter.getCharacterState().getDamageMultiplier();
        return getAttackBaseDamage(((Enemy) this).getEnemyName().getEnemyCombat().getCombatDamage(), enemyDamageDivider) * (1 + characterStateDamageMultiplier);
    }

    public double takeDamage(double calculatedDamage) {
        this.healthPoints -= calculatedDamage;
        return calculatedDamage;
    }

    public boolean checkSpellReady(Spell spell) {
        return spell.getSpellReadyIn() == 0;
    }

    public void setSpellReadyIn(Spell spell) {
        spell.setSpellReadyIn(spell.getSpellCooldown());
    }

    public List<Spell> getAllSpells() {
        HashMap<String, Spell> characterSpellHashMap = this.getSpellsHashMap();
        List<Spell> characterSpellList = new ArrayList<>();

        characterSpellHashMap.forEach((key, value) -> characterSpellList.add(value));
        return characterSpellList;
    }

    public List<String> getAllSpellNames() {
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

    public List<Spell> getTypedSpellsList(MoveType spellType) {
        return this.getAllSpells().stream().filter(spell -> spell.getSpellType().equals(spellType)).toList();
    }


    public void printTypedSpells(MoveType spellType) {
        int index = 1;
        List<Spell> typedSpellsList = getTypedSpellsList(spellType);

        for (Spell spell : typedSpellsList) {
            System.out.printf("%-15s", "("+returnColoredText(String.valueOf(index), ANSI_YELLOW)+")");
            System.out.println(spell.printStats());
            index++;
        }
    }

    public void reduceSpellsCooldown(int cooldown) {
        HashMap<String, Spell> spellsHashMap = this.getSpellsHashMap();

        spellsHashMap.forEach((key, value) -> {
            int newValue = value.getSpellReadyIn() - cooldown;
            if (newValue < 0) {
                newValue = 0;
            }
            value.setSpellReadyIn(newValue);
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

    public double getSpellCalculatedDamage(Spell spell, AbstractCharacter attackedCharacter) {
        // CALCULATIONS FOR SPELL DAMAGE
        return getSpellDamage(spell, attackedCharacter) * defenseReductionRatio();
    }

    public double getMeleeCalculatedDamage(AbstractCharacter attackedCharacter) {
        // CALCULATIONS FOR MELEE DAMAGE
        return getMeleeDamage(attackedCharacter) * defenseReductionRatio();
    }

    public double getSpellChance(Spell spell) {
        double luckPercent = 0;
        if (this.getClass() == Wizard.class) {
            luckPercent = ((Wizard) this).getWizardSpecsPercent().get("luck");
        }
        return spell.getSpellChance() * (1 + luckPercent);
    }

    public void castAttack(double attackChance, CharacterState characterState, AbstractCharacter attackedCharacter, double calculatedDamage, boolean attackAfterCast) {
        String attackedCharacterName = "";
        String attackingCharacterName = "";

        if (this.getClass() == Enemy.class) {
            attackingCharacterName = returnFormattedEnum(((Enemy) this).getEnemyName());
            attackedCharacterName = attackedCharacter.getName();
        } else if (this.getClass() == Wizard.class) {
            attackingCharacterName = this.getName();
            attackedCharacterName = returnFormattedEnum(((Enemy) attackedCharacter).getEnemyName());
        }

        double spellSuccess = Math.random();
        if (spellSuccess <= attackChance) {
            // TAKE DAMAGE
            double damageTaken = attackedCharacter.takeDamage(calculatedDamage);

            // CHANGE CHARACTER STATE
            attackedCharacter.setCharacterState(characterState);

            // PRINT AFTER CAST STUFF INTO CONSOLE
            double healthPoints = attackedCharacter.getHealthPoints();
            double maxHealthPoints = attackedCharacter.getMaxHealthPoints();
            if(attackAfterCast) {
                attackAfterCast(attackedCharacter, attackedCharacterName, attackingCharacterName, (int) damageTaken, healthPoints, maxHealthPoints);
            }

            // CHECK ENEMY'S CHARACTER AND ACT ACCORDINGLY (ADD XP AND ITEM DROPS)
            if (attackedCharacter.getClass() == Enemy.class) {
                assert this instanceof Wizard;
                ((Enemy) attackedCharacter).checkHealth((Wizard) this);
            }

        } else {
            System.out.println(attackingCharacterName + returnColoredText(" missed ", ANSI_YELLOW) + "their attack!");
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

            List<AbstractItem> enemyItemList = attackedCharacter.getItemList();

            if(enemyItemList.size() > 0) {
                text4.append("\nYou found:\n");
                enemyItemList.forEach(Item -> text4.append("- ").append(returnColoredText(Item.getItemName(), Item.getItemColor())).append("\n"));
            }
        }

        System.out.println(text1);
        if (healthPoints > 0) {
            if (attackedCharacter.getCharacterState() != CharacterState.STANDING) {
                System.out.println(text2);
            }
            System.out.println(text3);
        } else {
            System.out.println(text4);
        }
    }

    public void spellAttack(Spell spell, AbstractCharacter attackedCharacter, double calculatedDamage, boolean attackAfterCast) {
        String spellName = spell.getSpellName();

        // SPELL GETS USED AND PUT ON A COOLDOWN
        this.reduceSpellsCooldown(1);
        this.setSpellReadyIn(spell);

        // CONSOLE STUFF
        System.out.println(returnColoredText(spellName + "!", spell.getSpellColor()));
        if(this.getClass() == Wizard.class) {
            System.out.println(returnColoredText(spell.getSpellSpecialAttackLine(), ANSI_PURPLE));
        }
        castAttack(getSpellChance(spell), spell.getCharacterState(), attackedCharacter, calculatedDamage, attackAfterCast);
    }

    public boolean dodgeSpell(double dodgeChance, AbstractCharacter attackingCharacter) {
        double spellSuccess = Math.random();
        String text1 = "";
        String text2 = "";

        if (this.getClass() == Wizard.class) {
            text1 = returnColoredText("You dodged " + returnFormattedEnum(((Enemy) attackingCharacter).getEnemyName()) + "'s attack.", ANSI_YELLOW);
            text2 = returnColoredText("You were unable to dodge " + returnFormattedEnum(((Enemy) attackingCharacter).getEnemyName()) + "'s attack.", ANSI_RED);
            double charismaPercent =  ((Wizard) this).getWizardSpecsPercent().get("charisma");
            dodgeChance = (dodgeChance / wizardDodgeDivider) * (1 + charismaPercent);
        } else if (this.getClass() == Enemy.class) {
            text1 = returnColoredText(returnFormattedEnum(((Enemy) this).getEnemyName()) + " dodged your attack.", ANSI_YELLOW);
            text2 = returnColoredText(returnFormattedEnum(((Enemy) this).getEnemyName()) + " was unable to dodge your attack.", ANSI_RED);
            dodgeChance = dodgeChance / enemyDodgeDivider;
        }

        if (spellSuccess <= dodgeChance) {
            printTitle(text1);
            return true;
        } else {
            printTitle(text2);
            return false;
        }

    }

    public boolean parry(String parriedAttackName, AbstractCharacter attackingCharacter, double calculatedDamage) {
        String text1 = "";
        String text2 = "";
        double newDamage = calculatedDamage * (1 + parryMultiplier);
        double parryPoints = this.getDefensePoints();

        if (this.getClass() == Wizard.class) {
            text1 = returnColoredText("You parried " + parriedAttackName + ".", ANSI_YELLOW);
            text2 = returnColoredText("You were unable to parry " + returnFormattedEnum(((Enemy) attackingCharacter).getEnemyName()) + "'s attack.", ANSI_RED);
            double intelligencePercent =  ((Wizard) this).getWizardSpecsPercent().get("intelligence");
            parryPoints = (parryPoints / wizardParryDivider) * (1 + intelligencePercent);
        } else if (this.getClass() == Enemy.class) {
            text1 = returnColoredText(returnFormattedEnum(((Enemy) this).getEnemyName()) + " parried your attack and returned", ANSI_YELLOW)
                    + returnColoredText((int) newDamage + " Damage.", ANSI_RED);
            text2 = returnColoredText(returnFormattedEnum(((Enemy) this).getEnemyName()) + " was unable to parry your attack.", ANSI_RED);
            parryPoints = parryPoints / enemyParryDivider;
        }

        // THE ATTACK CAN BE PARRIED ONLY IF THE ATTACKED CHARACTER'S DEFENSE IS HIGHER THAN THE ATTACKING CHARACTER'S CALCULATED SPELL DAMAGE
        if (parryPoints > calculatedDamage) {
            printTitle(text1);
            this.spellAttack(stupefy, attackingCharacter, newDamage, true);
            return true;
        } else {
            printTitle(text2);
            return false;
        }
    }
}
