package AbstractClasses;

import Enums.ItemType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractItem {
    public AbstractItem(String itemName, String itemDescription, ItemType itemType, double itemDropChance) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemType = itemType;
        this.itemDropChance = itemDropChance;
    }

    private String itemName;
    private String itemDescription;
    private ItemType itemType;
    private double itemDropChance;

}
