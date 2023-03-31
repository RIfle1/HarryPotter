package project.Classes;

import project.AbstractClasses.AbstractItem;
import project.Enums.PotionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Potion extends AbstractItem implements Cloneable {
    @Builder
    public Potion(String itemName, String itemDescription, double itemDropChance, String itemColor, PotionType potionType, double potionDuration, double potionValue) {
        super(itemName, itemDescription, itemDropChance, itemColor);
        this.potionType = potionType;
        this.potionDuration = potionDuration;
        this.potionValue = potionValue;
    }

    private PotionType potionType;
    private double potionDuration;
    private double potionValue;

    public static Potion minorHealthPotion = Potion.builder()
            .itemName("Minor Health Potion")
            .itemDescription("A minor healing potion which heals 40hp.")
            .itemDropChance(0.8)
            .itemColor(Color.ANSI_BLUE)
            .potionType(PotionType.HEALTH)
            .potionDuration(1)
            .potionValue(40)
            .build();

    public static Potion mediumHealthPotion = Potion.builder()
            .itemName("Medium Health Potion")
            .itemDescription("A medium healing potion which heals 70hp.")
            .itemDropChance(0.5)
            .itemColor(Color.ANSI_YELLOW)
            .potionType(PotionType.HEALTH)
            .potionDuration(1)
            .potionValue(70)
            .build();

    public static Potion highHealthPotion = Potion.builder()
            .itemName("High Health Potion")
            .itemDescription("A high healing potion which heals 100hp.")
            .itemDropChance(0.2)
            .itemColor(Color.ANSI_RED)
            .potionType(PotionType.HEALTH)
            .potionDuration(1)
            .potionValue(100)
            .build();

    public static Potion minorDefensePotion = Potion.builder()
            .itemName("Minor Defense Potion")
            .itemDescription("A minor defense potion which adds 40 defense.")
            .itemDropChance(0.8)
            .itemColor(Color.ANSI_BLUE)
            .potionType(PotionType.DEFENSE)
            .potionDuration(1)
            .potionValue(40)
            .build();

    public static Potion mediumDefensePotion = Potion.builder()
            .itemName("Medium Defense Potion")
            .itemDescription("A medium defense potion which adds 70 defense.")
            .itemDropChance(0.5)
            .itemColor(Color.ANSI_YELLOW)
            .potionType(PotionType.DEFENSE)
            .potionDuration(5)
            .potionValue(70)
            .build();

    public static Potion highDefensePotion = Potion.builder()
            .itemName("High Defense Potion")
            .itemDescription("A high defense potion which adds 100 defense.")
            .itemDropChance(0.2)
            .itemColor(Color.ANSI_RED)
            .potionType(PotionType.DEFENSE)
            .potionDuration(6)
            .potionValue(100)
            .build();

    public static Potion minorRegenerationPotion = Potion.builder()
            .itemName("Minor Regeneration Potion")
            .itemDescription("A minor regeneration potion which regenerates 10 hp per turn.")
            .itemDropChance(0.8)
            .itemColor(Color.ANSI_BLUE)
            .potionType(PotionType.REGENERATION)
            .potionDuration(3)
            .potionValue(10)
            .build();

    public static Potion mediumRegenerationPotion = Potion.builder()
            .itemName("Medium Regeneration Potion")
            .itemDescription("A minor regeneration potion which regenerates 15 hp per turn.")
            .itemDropChance(0.5)
            .itemColor(Color.ANSI_YELLOW)
            .potionType(PotionType.REGENERATION)
            .potionDuration(5)
            .potionValue(15)
            .build();

    public static Potion highRegenerationPotion = Potion.builder()
            .itemName("High Regeneration Potion")
            .itemDescription("A minor regeneration potion which regenerates 25 hp per turn.")
            .itemDropChance(0.2)
            .itemColor(Color.ANSI_RED)
            .potionType(PotionType.REGENERATION)
            .potionDuration(7)
            .potionValue(0.2)
            .build();

    public static Potion minorDamagePotion = Potion.builder()
            .itemName("Minor Damage Potion")
            .itemDescription("A minor damage potion which increases damage by 10%.")
            .itemDropChance(0.8)
            .itemColor(Color.ANSI_BLUE)
            .potionType(PotionType.DAMAGE)
            .potionDuration(2)
            .potionValue(0.1)
            .build();

    public static Potion mediumDamagePotion = Potion.builder()
            .itemName("Medium Damage Potion")
            .itemDescription("A minor damage potion which increases damage by 15%.")
            .itemDropChance(0.5)
            .itemColor(Color.ANSI_YELLOW)
            .potionType(PotionType.DAMAGE)
            .potionDuration(4)
            .potionValue(0.15)
            .build();

    public static Potion highDamagePotion = Potion.builder()
            .itemName("High Damage Potion")
            .itemDescription("A minor damage potion which increases damage by 25%.")
            .itemDropChance(0.2)
            .itemColor(Color.ANSI_RED)
            .potionType(PotionType.DAMAGE)
            .potionDuration(6)
            .potionValue(0.25)
            .build();

    public static Potion minorCooldownPotion = Potion.builder()
            .itemName("Minor Cooldown Potion")
            .itemDescription("A minor cooldown potion which reduces all spells cooldown by 2 turns.")
            .itemDropChance(0.8)
            .itemColor(Color.ANSI_BLUE)
            .potionType(PotionType.COOLDOWN)
            .potionDuration(1)
            .potionValue(2)
            .build();

    public static Potion mediumCooldownPotion = Potion.builder()
            .itemName("Medium Cooldown Potion")
            .itemDescription("A minor cooldown potion which reduces all spells cooldown by 4 turns.")
            .itemDropChance(0.5)
            .itemColor(Color.ANSI_YELLOW)
            .potionType(PotionType.COOLDOWN)
            .potionDuration(1)
            .potionValue(4)
            .build();

    public static Potion highCooldownPotion = Potion.builder()
            .itemName("High Cooldown Potion")
            .itemDescription("A minor cooldown potion which reduces all spells cooldown by 6 turns.")
            .itemDropChance(0.2)
            .itemColor(Color.ANSI_RED)
            .potionType(PotionType.COOLDOWN)
            .potionDuration(1)
            .potionValue(6)
            .build();

    public static Potion invincibilityPotion = Potion.builder()
            .itemName("Invincibility Potion")
            .itemDescription("An invincibility potion which makes you invincible to all attacks.")
            .itemDropChance(0)
            .itemColor(Color.ANSI_PURPLE)
            .potionType(PotionType.INVINCIBILITY)
            .potionDuration(1)
            .potionValue(-1)
            .build();

    public static List<Potion> returnAllPotions(){
        List<Potion> potionList = new ArrayList<>();
        Field[] declaredFields = Potion.class.getDeclaredFields();

        for(Field field:declaredFields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                if (Potion.class.isAssignableFrom(field.getType())) {
                    try {
                        potionList.add((Potion) field.get(null));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return potionList;
    }

    public static List<String> returnAllPotionsNamesList() {
        List<String> potionNameList = new ArrayList<>();

        for(Potion potion: returnAllPotions()) {
            potionNameList.add(potion.getItemName());
        }
        return potionNameList;
    }

    public static List<String> getPotionsNamesList(List<Potion> potionList) {
        List<String> potionNameList = new ArrayList<>();

        for(Potion potion: potionList) {
            potionNameList.add(potion.getItemName());
        }
        return potionNameList;
    }

    @Override
    public Potion clone() throws CloneNotSupportedException {
        return (Potion) super.clone();
    }
}
