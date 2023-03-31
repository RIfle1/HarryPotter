package project.AbstractClasses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractItem {
    public AbstractItem(String itemName, String itemDescription, double itemDropChance, String itemColor) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemDropChance = itemDropChance;
        this.itemColor = itemColor;
    }

    private String itemName;
    private String itemDescription;
    private double itemDropChance;
    private String itemColor;

}
