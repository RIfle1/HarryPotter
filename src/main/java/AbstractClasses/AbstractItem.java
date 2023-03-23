package AbstractClasses;

import Enums.ItemType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractItem {
    public AbstractItem(String itemName, String itemDescription, ItemType itemType, double itemDropChance, String itemColor) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemType = itemType;
        this.itemDropChance = itemDropChance;
        this.itemColor = itemColor;
    }

    private String itemName;
    private String itemDescription;
    private ItemType itemType;
    private double itemDropChance;
    private String itemColor;

}
