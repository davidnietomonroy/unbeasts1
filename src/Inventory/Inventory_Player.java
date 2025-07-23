package Inventory;

import java.util.ArrayList;
import java.util.List;
import object.SuperObject;

public class Inventory_Player {

    private List<SuperObject> items;
    private int maxSize;

    public Inventory_Player(int maxSize) {
        this.maxSize = maxSize;
        this.items = new ArrayList<>();
    }

    public boolean addItem(SuperObject item) {
        if (items.size() < maxSize) {
            items.add(item);
            return true;
        }
        return false;
    }

    public List<SuperObject> getItems() {
        return items;
    }

    public boolean containsItem(String name) {
        return items.stream().anyMatch(obj -> obj.name.equalsIgnoreCase(name));
    }

    public int getSize() {
        return items.size();
    }
}

