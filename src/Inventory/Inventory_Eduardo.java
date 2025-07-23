package Inventory;

import java.util.ArrayList;
import java.util.List;
import object.SuperObject;

public class Inventory_Eduardo implements Inventory {

    private List<SuperObject> items;
    private final int maxSize;

    public Inventory_Eduardo(int maxSize) {
        this.maxSize = maxSize;
        this.items = new ArrayList<>();
    }

    @Override
    public boolean addItem(SuperObject item) {
        if (isFull()) {
            System.out.println("El inventario del jugador está lleno.");
            return false;
        }
        items.add(item);
        return true;
    }

    @Override
    public void removeItem(SuperObject item) {
        items.remove(item);
    }

    @Override
    public List<SuperObject> getItems() {
        return items;
    }

    @Override
    public boolean isFull() {
        return items.size() >= maxSize;
    }
}